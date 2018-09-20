package com.yz.model.operate;

import java.io.Serializable;
/**
 * 抽奖奖品
 * @author lx
 * @date 2018年7月12日 上午11:39:25
 */
public class BdLotteryPrizeInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5223628155613352999L;
	private String prizeId;
	private String lotteryId;
	private String prizeName;
	private String prizeType;
	private String prizeDesc;
	private String prizeCount;
	private String couponId;
	private String isCarousel;
	private String isAllow;
	private String orderNum;
	private String originalPrice;
	private String unit = "2";
	private String costPrice;
	private String updateUser;
	private String updateUserId;
	private String initPrizeCount;
	private String remark;
	private String begin;
	private String end;
	private String probability;
	
	private String lotteryName;

	public String getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(String prizeId) {
		this.prizeId = prizeId;
	}

	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public String getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
	}

	public String getPrizeDesc() {
		return prizeDesc;
	}

	public void setPrizeDesc(String prizeDesc) {
		this.prizeDesc = prizeDesc;
	}

	public String getPrizeCount() {
		return prizeCount;
	}

	public void setPrizeCount(String prizeCount) {
		this.prizeCount = prizeCount;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getIsCarousel() {
		return isCarousel;
	}

	public void setIsCarousel(String isCarousel) {
		this.isCarousel = isCarousel;
	}

	public String getIsAllow() {
		return isAllow;
	}

	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public String getInitPrizeCount() {
		return initPrizeCount;
	}

	public void setInitPrizeCount(String initPrizeCount) {
		this.initPrizeCount = initPrizeCount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getProbability() {
		return probability;
	}

	public void setProbability(String probability) {
		this.probability = probability;
	}
	

}
