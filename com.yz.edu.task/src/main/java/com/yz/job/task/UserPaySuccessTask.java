package com.yz.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.yz.job.common.AbstractSimpleTask; 
import com.yz.job.common.YzJob;
import com.yz.job.service.UserPaySuccesService;
import com.yz.model.UserPaySuccessEvent; 
import com.yz.task.YzTaskConstants;
import com.yz.util.JsonUtil;


@Component(value = "userPaySuccessTask")
@YzJob(
		queueName = YzTaskConstants.YZ_USER_PAYSUCCESS_EVENT, taskDesc = YzTaskConstants.YZ_USER_PAYSUCCESS_EVENT_DESC,
        cron = "0/5 * * * * ?", shardingTotalCount = 5, targetCls = UserPaySuccessEvent.class, log = true)
/**
 * 
 * @desc 接受代理proxy发出支付成功事件  通过RPC请求调用相应的接口  
 * @author lingdian
 *
 */
public class UserPaySuccessTask extends AbstractSimpleTask<UserPaySuccessEvent>  {

	@Autowired
	private UserPaySuccesService userPaySuccesService;
	
	@Override
	public void executeRedis(ShardingContext shardingContext,UserPaySuccessEvent event) {
	   logger.info("UserPaySuccessTask.event:{}",JsonUtil.object2String(event));
	   userPaySuccesService.paySuccess(event);
	}
}
