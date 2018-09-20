package com.yz.model.graduate;

import java.io.Serializable;

public class OaDiplomaTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3635140192387917479L;
	
	private String diplomaId;	//发放任务ID'
	private String taskName;	//发放任务名称
	private String warmTips;	//温馨提示
	private String warmReminder;	//温馨提醒
	private String updateTime;		//最后更新时间
	private String updateUser;		//最后更新人
	private String updateUserId;    //最后更新人ID
	private String createUserId;	//创建人ID
	private String createTime;		//创建时间
	private String createUser;		//创建人
	private String startTime;       // 创建开始时间
	private String endTime;         //创建截止时间
	public String getDiplomaId() {
		return diplomaId;
	}
	public void setDiplomaId(String diplomaId) {
		this.diplomaId = diplomaId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getWarmTips() {
		return warmTips;
	}
	public void setWarmTips(String warmTips) {
		this.warmTips = warmTips;
	}
	public String getWarmReminder() {
		return warmReminder;
	}
	public void setWarmReminder(String warmReminder) {
		this.warmReminder = warmReminder;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
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
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
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
	
}
