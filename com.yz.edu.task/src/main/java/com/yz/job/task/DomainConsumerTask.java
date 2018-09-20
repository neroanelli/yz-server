package com.yz.job.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.google.common.collect.Lists;
import com.yz.edu.cmd.BaseCommand;
import com.yz.edu.constants.YzDomainConstants;
import com.yz.edu.consumer.DomainConsumeVo;
import com.yz.edu.domain.YzBaseDomain;
import com.yz.edu.trace.TraceTransfer;
import com.yz.edu.trace.constant.TraceConstant;
import com.yz.job.common.AbstractSimpleTask;
import com.yz.job.common.YzDomainConsumer;
import com.yz.job.common.YzJob;
import com.yz.job.common.YzJobInfo;
import com.yz.job.mq.MQConsumer;
import com.yz.job.mq.MQConsumerCallBack;
import com.yz.mq.Event;
import com.yz.task.YzTaskConstants;
import com.yz.trace.sender.TraceSender;
import com.yz.util.ApplicationContextUtil;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;
import com.yz.util.OSUtil;

import net.sf.ehcache.util.NamedThreadFactory;

/**
 * 
 * @desc 接收domain发出的异步命令
 * @author Administrator
 *
 */
@Component(value = "domainConsumerTask")
@YzJob(taskDesc = YzTaskConstants.YZ_EVENT_SOURCE_TASK_DESC, cron = "0/1 * * * * ?", shardingTotalCount = 1, log = true)
public class DomainConsumerTask<K extends BaseCommand, V extends YzBaseDomain>
		extends AbstractSimpleTask<DomainConsumeVo> {

	private ExecutorService executors = Executors.newFixedThreadPool(8, new NamedThreadFactory("event-source-thread"));

	@Autowired
	private MQConsumer mqConsumer;

	@PostConstruct
	public void subscribe() {
		mqConsumer.subscribe(Lists.newArrayList(YzDomainConstants.DOMAIN_EVNET_SOURCE_TOPIC));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void executeOther(ShardingContext shardingContext) {
		YzJobInfo job = this.getYzJobInfo(shardingContext);
		DomainMQConsumerCallBack callBack = new DomainMQConsumerCallBack(job);
		mqConsumer.consumer(callBack);
	}

	private class DomainMQConsumerCallBack implements MQConsumerCallBack<Event> {

		YzJobInfo job;

		public DomainMQConsumerCallBack(YzJobInfo job) {
			this.job = job;
		}

		@Override
		public void execute(Event event) {
			executors.execute(() -> { 
				try
				{
					DomainConsumeVo vo = event.cast(event.getEvent(), DomainConsumeVo.class);
					BaseCommand cmd = vo.getCmd();
					if (cmd.isAsyn()) {
						YzBaseDomain domain = vo.getDomain();
						if (vo.isTrace()) {
							TraceTransfer.getTrace().getTraceSpan(vo.getTraceId(), appName, job.getQueueName(),
									OSUtil.getIp(), TraceConstant.TRACE_SPAN_ASYNC).setRemark(job.getTaskDesc()); 
						}
						logger.info("BaseCommand:{}  YzBaseDomain:{}", JsonUtil.object2String(cmd),
								JsonUtil.object2String(domain));
						YzDomainConsumer<BaseCommand, YzBaseDomain> consumer = ApplicationContextUtil
								.getBeanIgnoreEx(cmd.getTopic());
						if (consumer != null) 
							consumer.consumer(cmd, domain);
					}
				}catch(Exception ex)
				{
					logger.error("executeOther.error:{}",ExceptionUtil.getStackTrace(ex));
				}finally
				{
					TraceSender.sendSpan(TraceTransfer.getTrace().getCurrentSpan());
					TraceTransfer.getTrace().remove(); 
				}
			
			});
		} 
	} 
}
