package com.yz.edu.domain;

import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("serial")
/**
 * 
 * @desc 收费标准订单
 * @author Administrator
 *
 */
public class YzPayOrderFeeItem implements java.io.Serializable{
	
	private BigDecimal defineAmount; // 收费金额 
	
	private BigDecimal turnoverAmount; //上缴金额 

	private Date expireTime; // 过期时间
	
	private int status; // 状态  

	public BigDecimal getDefineAmount() {
		return defineAmount;
	}

	public void setDefineAmount(BigDecimal defineAmount) {
		this.defineAmount = defineAmount;
	}

	public BigDecimal getTurnoverAmount() {
		return turnoverAmount;
	}

	public void setTurnoverAmount(BigDecimal turnoverAmount) {
		this.turnoverAmount = turnoverAmount;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	} 
}
