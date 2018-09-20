package com.yz.model.operate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * 报读抽奖活动
 * @author lx
 * @date 2018年7月11日 下午4:18:06
 */
public class BdEnrollLotteryInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1869786988981593186L;
	
	private String lotteryId;
	private String lotteryName;
	private String lotteryNum;
	private String status;
	private String startTime;
	private String endTime;
	private String remark;
	private String lotteryUrl;
	private String ifLimitRegTime; 
	
	private List<Map<String, String>> attrList;
	
	private List<BdEnrollLotteryAttr> items;
	
	private String updateUser;
	private String updateUserId;
	
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
	public String getLotteryNum() {
		return lotteryNum;
	}
	public void setLotteryNum(String lotteryNum) {
		this.lotteryNum = lotteryNum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public List<Map<String, String>> getAttrList() {
		return attrList;
	}
	public void setAttrList(List<Map<String, String>> attrList) {
		this.attrList = attrList;
	}
	public List<BdEnrollLotteryAttr> getItems() {
		return items;
	}
	public void setItems(List<BdEnrollLotteryAttr> items) {
		this.items = items;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getLotteryUrl() {
		return lotteryUrl;
	}
	public void setLotteryUrl(String lotteryUrl) {
		this.lotteryUrl = lotteryUrl;
	}
	public String getIfLimitRegTime() {
		return ifLimitRegTime;
	}
	public void setIfLimitRegTime(String ifLimitRegTime) {
		this.ifLimitRegTime = ifLimitRegTime;
	}

}
