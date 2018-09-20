package com.yz.model.operate;

/**
 * 抽奖记录
 */
public class DrawRecord {

	private String upId;//中奖奖品信息ID
	private String prizeName;//奖品名称
	private String userId;//登录ID
	private String realName;//中奖人
	private String mobile;//手机号
	private String winningTime;//中奖时间
	private String lotteryName;//中奖活动名称
	private String lotteryId;//中奖活动ID

	public String getUpId() {
		return upId;
	}

	public void setUpId(String upId) {
		this.upId = upId;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getWinningTime() {
		return winningTime;
	}

	public void setWinningTime(String winningTime) {
		this.winningTime = winningTime;
	}

	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}
}
