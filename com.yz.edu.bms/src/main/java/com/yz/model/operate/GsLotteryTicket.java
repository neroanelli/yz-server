package com.yz.model.operate;

/**
 * 抽奖机会管理
 * @Description: 
 * @author luxing
 * @date 2018年7月12日 下午3:26:27
 */
public class GsLotteryTicket {

	private String ticketId;//抽奖券id
	
	private String lotteryId;//活动id
	
	private String isUsed;//是否使用 1-已使用 0-未使用
	
	private String usedTime;//使用时间
	
	private String giveWayType;//赠送方式1:注册,2:绑定,3:缴费,4:邀约
	
	private String lotteryName;//活动名称
	
	private String startTime;//活动起始时间
	
	private String endTime;//活动结束时间
	
	private String userName;//用户姓名
	
	private String mobile;//电话号码
	
	private String yzCode;//用户远智编码
	
	private String createTime;//获取时间
	
	private String triggerUserName;//触发人名称
	
	private String triggerUserMobile;//触发人手机号
	
	private String triggerYzCode;//触发人远智编码

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

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

	public String getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(String usedTime) {
		this.usedTime = usedTime;
	}

	public String getGiveWayType() {
		return giveWayType;
	}

	public void setGiveWayType(String giveWayType) {
		this.giveWayType = giveWayType;
	}

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	
}
