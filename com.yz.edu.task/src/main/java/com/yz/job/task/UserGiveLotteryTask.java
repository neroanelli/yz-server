package com.yz.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.yz.job.common.AbstractSimpleTask;
import com.yz.job.common.YzJob;
import com.yz.job.service.UserGiveLotteryService;
import com.yz.model.UserLotteryEvent;
import com.yz.task.YzTaskConstants;

/**
 * 用户赠送抽奖机会事件
 * @author lx
 * @date 2018年7月13日 下午4:41:22
 */
@Component(value = "userGiveLotteryTask")
@YzJob(queueName = YzTaskConstants.YZ_USER_GIVE_LOTTERY_EVENT, 
taskDesc = YzTaskConstants.YZ_USER_GIVE_LOTTERY_EVENT_DESC, cron = "0/5 * * * * ?", shardingTotalCount = 3, targetCls = UserLotteryEvent.class,log= true)
public class UserGiveLotteryTask extends AbstractSimpleTask<UserLotteryEvent>{

	@Autowired
	private UserGiveLotteryService giveLotteryService;
	
	@Override
	public void executeRedis(ShardingContext shardingContext,UserLotteryEvent event) {
		giveLotteryService.giveLotteryTicket(event);
	}
}
