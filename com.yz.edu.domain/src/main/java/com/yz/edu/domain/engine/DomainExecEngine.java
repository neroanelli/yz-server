package com.yz.edu.domain.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.yz.edu.cmd.BaseCommand;
import com.yz.edu.constants.YzDomainConstants;
import com.yz.edu.consumer.DomainConsumeVo;
import com.yz.edu.context.DomainContext;
import com.yz.edu.domain.YzBaseDomain;
import com.yz.edu.domain.callback.DomainCallBack;
import com.yz.edu.domain.executor.DomainExecutor;
import com.yz.edu.domain.loader.DomainLoaderFactory;
import com.yz.edu.trace.TraceTransfer;
import com.yz.exception.BusinessException;
import com.yz.exception.CustomException;
import com.yz.model.WechatMsgVo;
import com.yz.mq.Event;
import com.yz.mq.MQProducer;
import com.yz.redis.RedisService;
import com.yz.redis.RedisService.RedisTransactionExecutor;
import com.yz.serializ.FstSerializer;
import com.yz.task.YzTaskConstants;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;

/**
 * 
 * 
 * @desc domain 执行引擎
 * @author Administrator
 *
 */
@Component(value = "domainExecEngine")
public class DomainExecEngine {

	private Logger logger = LoggerFactory.getLogger(DomainExecEngine.class);

	private Logger dataLogger = LoggerFactory.getLogger("DOMAIN");

	private FstSerializer fstSerialize = FstSerializer.getInstance();

	@Value(value = "${dubbo.application:yz}")
	private String appName;

	private static int DOMAIN_DB_INDEX = 4;

	/**
	 * @param RedisCommandInvoker<?
	 *            extends YzBaseDomain> invoker
	 * @param key
	 *            执行的key
	 * @param callBack
	 *            触发的回调操作
	 * @desc 执行command指令
	 */
	public Object executeCmds(List<? extends BaseCommand> cmdInfos, DomainCallBack callBack) throws BusinessException {
		if (cmdInfos == null || cmdInfos.isEmpty()) {
			logger.error("cmds.isEmpty");
			throw new CustomException("cmds.isEmpty");
		}
		// 在一个批量操作去除重复的Command
		List<? extends BaseCommand> cmds = cmdInfos.stream().distinct().sorted().collect(Collectors.toList());
		logger.info("execute.cmds:{}", JsonUtil.object2String(cmds));
		byte[][] arr = new byte[cmds.size()][];
		final Map<byte[], BaseCommand> cmdMap = Maps.newHashMapWithExpectedSize(cmds.size());
		int index = 0;
		for (BaseCommand v : cmds) {
			arr[index] = fstSerialize.serialize(v.getId());
			cmdMap.put(arr[index], v);
			index++;
		}
		return RedisService.getRedisService().execStepCmds(new RedisTransactionDomainExecutor(cmdMap, callBack),
				DOMAIN_DB_INDEX, arr);
	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	private class RedisTransactionDomainExecutor implements RedisTransactionExecutor {

		private Map<byte[], BaseCommand> cmdMap;

		private DomainCallBack callBack;

		private Map<BaseCommand, YzBaseDomain> dataMap = Maps.newHashMap();

		public RedisTransactionDomainExecutor(Map<byte[], BaseCommand> cmdMap, DomainCallBack callBack) {
			this.cmdMap = cmdMap;
			this.callBack = Optional.ofNullable(callBack).orElse(DomainCallBack.empty);
		}

		@Override
		public Object execute(Object key, Object object) {
			BaseCommand cmd = cmdMap.get(key);
			YzBaseDomain domain = (YzBaseDomain) object;
			if (cmd.getOperaton() == YzDomainConstants.DOMAIN_OPERATION_ADD) {
				domain = createDomainInstance(cmd);
				if (domain == null) {
					logger.error("createDomain.error;");
					throw new CustomException("createDomain.key:{" + cmd.getId() + "}.error;");
				}
				domain.setId(key);
			} else {
				domain = DomainLoaderFactory.getInstance().loadDomain(cmd, domain);
				if (domain == null) {
					logger.error(cmd.getDomainCls().getName() + " . not found key:" + cmd.getId());
					throw new CustomException("not exist Id: " + cmd.getId());
				}
			}
			domain = DomainExecutor.execDomainCmd(domain, cmd);// 执行domain操作
			if (cmd.isSuccess())
				DomainContext.getInstance().setContext(domain).setContextAttr(cmd, domain);
			return domain;
		}

		@Override
		/**
		 * @desc 判断该节点是否数据收集节点 如果表示是数据收集节点 则不进行数据持久化操作
		 */
		public DomainExecResult postExecute(Object key, Object obj) {
			BaseCommand cmd = cmdMap.get(key);
			// 任务执行失败？
			if (!cmd.isSuccess()) {
				logger.error("executeCmd.error,cmdInfo:{}", JsonUtil.object2String(cmd));
				// 任务必须成功？ 抛出异常 终止该条操作
				if (cmd.isMustSuccess())
					throw new BusinessException(cmd.getErrorCode());
			}
			DomainExecResult result = new DomainExecResult();
			result.setStorage(!cmd.isDataCollectCmd());
			result.setTarget(obj);
			result.setSetExpire(cmd.getOperaton() == YzDomainConstants.DOMAIN_OPERATION_ADD);
			((YzBaseDomain) obj).setLastModified(System.currentTimeMillis());
			result.setExpire(((YzBaseDomain) obj).getExpire());
			if (!cmd.isDataCollectCmd()) {
				dataMap.put(cmd, (YzBaseDomain) obj);
			}
			return result;
		}

		@Override
		public void clearContext() {
			DomainContext.getInstance().removeContext();
		}

		@Override
		public boolean preExecute(Object key, Object obj) {
			logger.info("preExecute.invoke!");
			return true;
		}

		@Override
		public Object callSuccess() throws Exception {
			return ((DomainCallBack) () -> {
				return handlerResult(dataMap);
			}).andThen(callBack).callSuccess();
		}

		@Override
		public void callFailed() throws Exception {
			this.callBack.callFailed();
		}
	}

	/**
	 * @desc cmd -> domain 实体
	 * @desc 异步处理Command指令
	 */
	private boolean handlerResult(Map<BaseCommand, YzBaseDomain> dataMap) {
		if (dataMap != null && !dataMap.isEmpty()) {
			String traceId = TraceTransfer.getTrace().getId();
			List<String> wechatMsgs = new ArrayList<>();
			dataMap.entrySet().parallelStream().forEach(v -> {
				BaseCommand cmd = v.getKey();
				if (cmd.isSuccess()) {
					sendConsumerTopic(traceId, v);
					// 收集微信推送的数据
					if (cmd.isPushMsg()) {
						WechatMsgVo msg = cmd.getPushMsgVo();
						if (msg != null)
							wechatMsgs.add(JsonUtil.object2String(msg));
					}
				}
			});
			// 发送微信推送
			if (wechatMsgs.size() > 0) {
				String[] arr = new String[wechatMsgs.size()];
				wechatMsgs.toArray(arr);
				RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, arr);
			}
		}
		return true;
	}

	/***
	 * @dec 发送EventSource数据
	 * @param cmd
	 */
	private void sendEventSource(DomainConsumeVo data) {
		Event event = new Event(YzDomainConstants.DOMAIN_EVNET_SOURCE_TOPIC, data);
		logger.info("sendEventSource.topic:{},cmd.topic:{}",YzDomainConstants.DOMAIN_EVNET_SOURCE_TOPIC,data.getCmd().getTopic()  );
		MQProducer.getMQProducer().send(event);
	}

	/**
	 * @dec 发送comsumerTopic数据
	 * @param traceId
	 * @param DomainExecEngine
	 */
	private DomainExecEngine sendConsumerTopic(String traceId, Entry<BaseCommand, YzBaseDomain> v) {
		BaseCommand cmd = v.getKey();
		if (!cmd.isDataCollectCmd()) {
			DomainConsumeVo vo = new DomainConsumeVo();
			vo.setTrace(cmd.isTrace());
			vo.setTraceId(traceId);
			vo.setCmd(cmd);
			vo.setDomain(v.getValue());
			vo.setAppName(appName);  
			sendEventSource(vo); 
			dataLogger.info("eventSourceData:{}",JsonUtil.object2String(vo));
		}
		return this;
	}

	/**
	 *
	 *
	 * @param cmd
	 * @return
	 */
	private YzBaseDomain createDomainInstance(BaseCommand cmd) {
		try {
			return (YzBaseDomain) cmd.getDomainCls().newInstance();
		} catch (Exception e) {
			logger.error("createDomainInstance.error:{}", ExceptionUtil.getStackTrace(e));
			return null;
		}
	}
}
