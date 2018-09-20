package com.yz.job.model;

import java.io.Serializable;

/**
 * 商品兑换记录
 * 
 * @author lx
 * @date 2017年7月24日 下午5:53:16
 */
public class GsExChangePart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1138601262367384237L;

 
	private String userName;
	private String mobile;
	private String exchangeCount;
	private String exchangeTime;
	private String salesId;
	private String userId;
	private String headImgUrl;
	private String orderNo;
	private int status; // 状态

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getExchangeCount() {
		return exchangeCount;
	}

	public void setExchangeCount(String exchangeCount) {
		this.exchangeCount = exchangeCount;
	}

	public String getExchangeTime() {
		return exchangeTime;
	}

	public void setExchangeTime(String exchangeTime) {
		this.exchangeTime = exchangeTime;
	}
 

	public String getSalesId() {
		return salesId;
	}

	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
}
