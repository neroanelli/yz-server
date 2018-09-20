package com.yz.edu.cmd;

import java.math.BigDecimal;

import com.yz.edu.domain.YzUserDomain;

/**
 * 
 * @desc 学员缴费  去支付  
 *       用户域
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class UserToPayCmd extends BaseCommand {

	private String userId ;  // 用户id
	
	private BigDecimal zmDeduction ;// 智米抵扣 
	
	private BigDecimal accDeduction ; // 滞留金抵扣 
	
	
    public UserToPayCmd() {
    	setStep(1);
	}
	
	@Override
	public Object getId() { 
		return userId;
	}

	@Override
	public String getMethodName() { 
		return "toPay";
	}

	@Override
	public Class<?> getDomainCls() { 
		return YzUserDomain.class;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getZmDeduction() {
		return zmDeduction;
	}

	public void setZmDeduction(BigDecimal zmDeduction) {
		this.zmDeduction = zmDeduction;
	}

	public BigDecimal getAccDeduction() {
		return accDeduction;
	}

	public void setAccDeduction(BigDecimal accDeduction) {
		this.accDeduction = accDeduction;
	} 

}
