package com.yz.edu.domain;

import java.math.BigDecimal;
import java.util.Comparator;


/**
 * 
 * @desc 收费订单明细
 * @author lingdian 
 *
 */
@SuppressWarnings("serial")
public class YzPayOrderItem implements java.io.Serializable,Comparable<YzPayOrderItem>{
	
	private String orderNo ; // 订单号 
	
	private String itemCode ; // 收费代码 
	
	private String itemName ; // 收费名称
	
	private int itemType ;  // 收费类型 
	
	private int itemYear; // 第几年费用
	
	private int status; // 状态 
	
	private int payStatus;  // 支付状态  0 未支付 1 支付 
	
	private BigDecimal cash ; // 现金 
	
	private BigDecimal zhimi; // 智米 
	
	private BigDecimal delay ; // 滞留
	
	private BigDecimal coupon ; // 优惠券
	
	private int zmCouponSeq; // 智米优惠券抵扣顺序
	
	private int delaySeq; // 滞留账户抵扣顺序
	
	private YzPayOrderOfferItem offerItem; // 优惠
	
	private YzPayOrderFeeItem feeItem ; // 收费
	
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	
	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}
	
	public int getPayStatus() {
		return payStatus;
	}
	
	public void setFeeItem(YzPayOrderFeeItem feeItem) {
		this.feeItem = feeItem;
	}
	
	public YzPayOrderFeeItem getFeeItem() {
		return feeItem;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public int getItemYear() {
		return itemYear;
	}

	public void setItemYear(int itemYear) {
		this.itemYear = itemYear;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getZmCouponSeq() {
		return zmCouponSeq;
	}

	public void setZmCouponSeq(int zmCouponSeq) {
		this.zmCouponSeq = zmCouponSeq;
	}

	public int getDelaySeq() {
		return delaySeq;
	}

	public void setDelaySeq(int delaySeq) {
		this.delaySeq = delaySeq;
	}
	
	public void setOfferItem(YzPayOrderOfferItem offerItem) {
		this.offerItem = offerItem;
	}
	
	public YzPayOrderOfferItem getOfferItem() {
		return offerItem;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public BigDecimal getZhimi() {
		return zhimi;
	}

	public void setZhimi(BigDecimal zhimi) {
		this.zhimi = zhimi;
	}

	public BigDecimal getDelay() {
		return delay;
	}

	public void setDelay(BigDecimal delay) {
		this.delay = delay;
	}

	public BigDecimal getCoupon() {
		return coupon;
	}

	public void setCoupon(BigDecimal coupon) {
		this.coupon = coupon;
	}
 

	@Override
	public int compareTo(YzPayOrderItem item) {
		  return (this.zmCouponSeq >=item.getZmCouponSeq())?1:-1;
	} 
	
	

}
