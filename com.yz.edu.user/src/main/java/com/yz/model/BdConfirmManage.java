package com.yz.model;

/**
 * 现场确认点管理
 */
public class BdConfirmManage {

	private String confirmationId;//主键id
	
	private String taId;//考试县区id
	
	private String taName;//考试县区
	
	private String confirmCity;//确认城市
	
	private String address;//详细地址
	
	private String confirmName;//现场确认点名称
	
	private String confirmAddressLevel;// 1:代表专升本  3：专升本/高起专  5：代表高起专
	
	private String requiredMaterials;//所需材料
	
	private String chargePerson;//负责人
	
	private String chargePersonTel;//负责人手机号
	
	private String startTime;//确认点起始时间
	
	private String endTime;//确定点结束时间
	
	private String confirmTime;//确认日期
	
	private String number;//人数容量
	
	private String availableNumbers;//可用人数

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

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getTaId() {
		return taId;
	}

	public void setTaId(String taId) {
		this.taId = taId;
	}

	public String getTaName() {
		return taName;
	}

	public void setTaName(String taName) {
		this.taName = taName;
	}

	public String getConfirmCity() {
		return confirmCity;
	}

	public void setConfirmCity(String confirmCity) {
		this.confirmCity = confirmCity;
	}

}
