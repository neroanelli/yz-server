package com.yz.model.transfer;

public class BdStudentOutRemark {
	private String remarkId;	//id
	private String remark;    //备注
	private String createrUser;   //创建人
	private String createUserId;
	private String createTime;  //创建时间
	private String outId;  //外键
	public String getRemarkId() {
		return remarkId;
	}
	public void setRemarkId(String remarkId) {
		this.remarkId = remarkId;
	}

	public String getCreaterUser() {
		return createrUser;
	}
	public void setCreaterUser(String createrUser) {
		this.createrUser = createrUser;
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
	public String getOutId() {
		return outId;
	}
	public void setOutId(String outId) {
		this.outId = outId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
