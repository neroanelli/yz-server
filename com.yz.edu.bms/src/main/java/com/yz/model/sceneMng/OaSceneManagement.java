package com.yz.model.sceneMng;

import java.io.Serializable;
import java.util.List;

import com.yz.model.stdService.StuDiplomaConfig;

public class OaSceneManagement implements Serializable{

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;
	
	private String confirmationId;   //ID
	private String confirmCity;   //确认城市
	private String taName;   //考试区县
	private String address;   //详细地址
	private String taId;   //关联考试区县表
	private String confirmName;   //确认点名称
	private String confirmAddressLevel;   //确认点专业层次
	private String confirmAddressLevels[];
	private String requiredMaterials;   //所需材料
	private String chargePerson;   //负责人
	private String chargePersonTel;  //负责人手机号码
	private String startTime;  //确认点开始时间
	private String endTime;   //确认点结束时间
	private String number;  //人数容量
	private String availableNumbers;   //可用人数
	private String remark;  //备注
	private String updateTime;		//最后更新时间
	private String updateUser;		//最后更新人
	private String updateUserId;    //最后更新人ID
	private String createUserId;	//创建人ID
	private String createTime;		//创建时间
	private String createUser;		//创建人
	private String date;
	private String isAllow;     //是否启用
	private String isFull;   //人数是否已满
	private List<OaSceneManagement> configs;//保存前端过来的多个时间配置
	public String getConfirmationId() {
		return confirmationId;
	}
	public void setConfirmationId(String confirmationId) {
		this.confirmationId = confirmationId;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getConfirmName() {
		return confirmName;
	}
	public void setConfirmName(String confirmName) {
		this.confirmName = confirmName;
	}
	public String getConfirmAddressLevel() {
		return confirmAddressLevel;
	}
	public void setConfirmAddressLevel(String confirmAddressLevel) {
		this.confirmAddressLevel = confirmAddressLevel;
	}
	public String getRequiredMaterials() {
		return requiredMaterials;
	}
	public void setRequiredMaterials(String requiredMaterials) {
		this.requiredMaterials = requiredMaterials;
	}
	public String getChargePerson() {
		return chargePerson;
	}
	public void setChargePerson(String chargePerson) {
		this.chargePerson = chargePerson;
	}
	public String getChargePersonTel() {
		return chargePersonTel;
	}
	public void setChargePersonTel(String chargePersonTel) {
		this.chargePersonTel = chargePersonTel;
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
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getAvailableNumbers() {
		return availableNumbers;
	}
	public void setAvailableNumbers(String availableNumbers) {
		this.availableNumbers = availableNumbers;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String[] getConfirmAddressLevels() {
		return confirmAddressLevels;
	}
	public void setConfirmAddressLevels(String[] confirmAddressLevels) {
		this.confirmAddressLevels = confirmAddressLevels;
	}
	public List<OaSceneManagement> getConfigs() {
		return configs;
	}
	public void setConfigs(List<OaSceneManagement> configs) {
		this.configs = configs;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getConfirmCity() {
		return confirmCity;
	}
	public void setConfirmCity(String confirmCity) {
		this.confirmCity = confirmCity;
	}
	public String getTaName() {
		return taName;
	}
	public void setTaName(String taName) {
		this.taName = taName;
	}
	public String getIsAllow() {
		return isAllow;
	}
	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow;
	}
	public String getTaId() {
		return taId;
	}
	public void setTaId(String taId) {
		this.taId = taId;
	}
	public String getIsFull() {
		return isFull;
	}
	public void setIsFull(String isFull) {
		this.isFull = isFull;
	}
	
}
