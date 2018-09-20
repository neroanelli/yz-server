package com.yz.edu.domain;

import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("serial")
/**
 * 
 * @desc 优惠定义
 * @author Administrator
 *
 */
public class YzPayOrderOfferItem implements java.io.Serializable {

	private BigDecimal defineAmount; // 收费金额 

	private Date expireTime; // 过期时间
	
	private int status; // 状态  
	
	private int discountType; // 优惠类型 1-减免 2-全免 3-折扣

	public BigDecimal getDefineAmount() {
		return defineAmount;
	}

	public void setDefineAmount(BigDecimal defineAmount) {
		this.defineAmount = defineAmount;
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

	public int getDiscountType() {
		return discountType;
	}

	public void setDiscountType(int discountType) {
		this.discountType = discountType;
	} 
}
