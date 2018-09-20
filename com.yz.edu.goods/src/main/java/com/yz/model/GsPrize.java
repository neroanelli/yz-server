package com.yz.model;

import java.io.Serializable;

public class GsPrize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String prizeId;

	private String prizeName;

	private String prizeType;

	private String couponId;

	private String upId;

	// 中奖概率
	private double probability;
	
	private String begin;  //池值开始
	 
	private String end;    //池值结束
	
	private String prizeCount;  //库存
	

	public String getUpId() {
		return upId;
	}

	public void setUpId(String upId) {
		this.upId = upId;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public String getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(String prizeId) {
		this.prizeId = prizeId;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
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

	public String getPrizeCount() {
		return prizeCount;
	}

	public void setPrizeCount(String prizeCount) {
		this.prizeCount = prizeCount;
	}

}
