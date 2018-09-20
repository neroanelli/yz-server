package com.yz.model;

import java.util.List;

/**
 * 用户抽奖事件
 * @author lx
 * @date 2018年7月13日 下午4:14:22
 */
@SuppressWarnings("serial")
public class UserLotteryEvent extends BaseEvent {

	private String userId;          //用户id
	
	private String mobile;          //用户手机
	
	private List<String> payItems;  //缴费科目
	
	private String scholarship;     //优惠类型
	
	private String recruitType;     //报读类型
	
	private String operType;        //1:注册,2:绑定,3:缴费,4:邀约 

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<String> getPayItems() {
		return payItems;
	}

	public void setPayItems(List<String> payItems) {
		this.payItems = payItems;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}
}
