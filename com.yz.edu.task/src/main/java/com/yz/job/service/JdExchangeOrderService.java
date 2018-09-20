package com.yz.job.service;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service;

import com.yz.edu.cmd.JdExchangeCmd; 
import com.yz.edu.domain.JdGoodsSalesDomain;
import com.yz.job.common.YzDomainConsumer; 
import com.yz.task.YzTaskConstants;

/**
 * 
 * @desc 京东兑换订单任务
 * @author Administrator
 *
 */
@Service(value = YzTaskConstants.JD_EXCHANGE_ORDER_TASK) 
public class JdExchangeOrderService implements YzDomainConsumer<JdExchangeCmd, JdGoodsSalesDomain> {

	@Autowired
	private BsOrderService bsOrderService;
 
	@Override
	public void consumer(JdExchangeCmd cmd, JdGoodsSalesDomain domain) {
		bsOrderService.addJDNewBsOrder(domain, cmd);		
	}
}
