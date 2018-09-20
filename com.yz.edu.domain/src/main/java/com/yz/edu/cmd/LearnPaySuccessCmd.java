package com.yz.edu.cmd;

import java.math.BigDecimal; 

/**
 * 
 * @desc 学业缴费订单支付成功指令
 * @author lingdian
 *
 */
public class LearnPaySuccessCmd extends LearnToPayCmd { 

	private BigDecimal payAmount; // 现金支付金额
	
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	
	public BigDecimal getPayAmount() {
		return payAmount;
	}

	@Override
	public String getMethodName() {
		return "paySuccess";
	}  
}
