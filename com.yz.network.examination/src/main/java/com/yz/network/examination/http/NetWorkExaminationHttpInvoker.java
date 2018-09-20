package com.yz.network.examination.http;
 
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder; 
import com.yz.network.examination.cmd.NetWorkExamCmdBuilder;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;

/**
 * 
 * @desc YzNetWorkExamHttpInvoker
 * @author lingdian
 *
 */
@Component(value = "yzNetWorkExamHttpInvoker")
public class NetWorkExaminationHttpInvoker {

	private static Logger logger = LoggerFactory.getLogger(NetWorkExaminationHttpInvoker.class);

	@Autowired
	private NetWorkExamCmdBuilder netWorkExamCmdBuilder;

	private Semaphore semaphore = new Semaphore(50000, false); // 当前invoke最大的调用数量   // 每120s  500请求

	private Cache<String, AtomicInteger> compensateCache = CacheBuilder.newBuilder().softValues().initialCapacity(1000)
			.expireAfterAccess(120, TimeUnit.SECONDS).build();

	/**
	 * @param form
	 */
	public void startNetWorkExamFrm(Set<BaseNetWorkExamForm> forms) {
		if (forms == null || forms.isEmpty()) {
			logger.error("forms is null,please check config !");
			return;
		}
		forms.parallelStream().map(netWorkExamCmdBuilder::build).forEach(cmd -> {
			try {
				boolean isStart = true;
				if (cmd.isCompensateCmd()) // 补偿指令表单
				{
					AtomicInteger count = compensateCache.get(cmd.getId(), AtomicInteger::new);
					if (count.incrementAndGet() >= cmd.getYzNetWorkForm().compensateTimes()) {
						isStart = false;
						logger.error("reTry.times:{};Exceed limit:{}，compensate.abort!", count.get(),
								cmd.getYzNetWorkForm().compensateTimes());
					}
					logger.info("cmd.info:{}", JsonUtil.object2String(cmd));
				}
				if (isStart) {
					if (semaphore.tryAcquire(120, TimeUnit.SECONDS)) {
						cmd.execute();
					}
				}
			} catch (Exception e) {
				logger.error("startNetWorkExamFrm.error:{}", ExceptionUtil.getStackTrace(e));
			} finally {
				semaphore.release();
			}
		});
	}
}
