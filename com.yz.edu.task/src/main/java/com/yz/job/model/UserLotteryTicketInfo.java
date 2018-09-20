package com.yz.job.model;

import java.io.Serializable;

/**
 * 用户的抽奖券
 * @author lx
 * @date 2018年7月13日 下午6:16:21
 */
@SuppressWarnings("serial")
public class UserLotteryTicketInfo implements Serializable{

	  private String ticketId;
	  private String lotteryId;
	  private String userId;
	  private String userName;
	  private String mobile;
	  private String yzCode;
	  private String triggerUserId;
	  private String triggerUserName;
	  private String triggerUserMobile;
	  private String triggerYzCode;
	  private String giveWayType;
	public String getTicketId() {
		return ticketId;
	}
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getYzCode() {
		return yzCode;
	}
	public void setYzCode(String yzCode) {
		this.yzCode = yzCode;
	}
	public String getTriggerUserId() {
		return triggerUserId;
	}
	public void setTriggerUserId(String triggerUserId) {
		this.triggerUserId = triggerUserId;
	}
	public String getTriggerUserName() {
		return triggerUserName;
	}
	public void setTriggerUserName(String triggerUserName) {
		this.triggerUserName = triggerUserName;
	}
	public String getTriggerUserMobile() {
		return triggerUserMobile;
	}
	public void setTriggerUserMobile(String triggerUserMobile) {
		this.triggerUserMobile = triggerUserMobile;
	}
	public String getTriggerYzCode() {
		return triggerYzCode;
	}
	public void setTriggerYzCode(String triggerYzCode) {
		this.triggerYzCode = triggerYzCode;
	}
	public String getGiveWayType() {
		return giveWayType;
	}
	public void setGiveWayType(String giveWayType) {
		this.giveWayType = giveWayType;
	}
}
