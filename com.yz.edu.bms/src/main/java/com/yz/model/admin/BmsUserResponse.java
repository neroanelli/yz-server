package com.yz.model.admin;

import java.util.ArrayList;
import java.util.List;

import com.yz.model.common.CustomizeAttrInfo;

public class BmsUserResponse extends BmsUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4477561889250418766L;

	private String[] roleArray;

	private List<BmsRole> roles = new ArrayList<BmsRole>();
	
	private String campusId;
	private String dpId;
	
	private List<CustomizeAttrInfo> attrList;      //自定义属性
	
	private String callNumCity;                    //叫号城市
	private String signCity;                       //签到城市
	private String netValue;                       //是否可网报
	private String signCount;                      //签到数量

	public String[] getRoleArray() {
		return roleArray;
	}

	public void setRoleArray(String[] roleArray) {
		this.roleArray = roleArray;
	}

	public List<BmsRole> getRoles() {
		return roles;
	}

	public void setRoles(List<BmsRole> roles) {
		this.roles = roles;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	public List<CustomizeAttrInfo> getAttrList() {
		return attrList;
	}

	public void setAttrList(List<CustomizeAttrInfo> attrList) {
		this.attrList = attrList;
	}

	public String getCallNumCity() {
		return callNumCity;
	}

	public void setCallNumCity(String callNumCity) {
		this.callNumCity = callNumCity;
	}

	public String getSignCity() {
		return signCity;
	}

	public void setSignCity(String signCity) {
		this.signCity = signCity;
	}

	public String getNetValue() {
		return netValue;
	}

	public void setNetValue(String netValue) {
		this.netValue = netValue;
	}

	public String getSignCount() {
		return signCount;
	}

	public void setSignCount(String signCount) {
		this.signCount = signCount;
	}
}