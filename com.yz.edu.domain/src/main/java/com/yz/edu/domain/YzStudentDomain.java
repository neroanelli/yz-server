package com.yz.edu.domain;

public class YzStudentDomain extends YzUserDomain {

	private String stdName;// 学员姓名
	private String idType;// 证件类型
	private String idCard;// 证件号
	private String sex;// 性别 1:男 2：女
	private String birthday;// 生日
	private String nation;// 民族
	private String politicalStatus;// 政治面貌
	private String edcsType;// 当前学历
	private String rprType;// 户口类型
	private String rprProvinceCode;// 户口所在省份
	private String rprCityCode;// 户口所在市
	private String rprDistrictCode;// 户口所在县/区
	private String zipCode;// 邮政编码
	private String nowProvinceCode;// 收教材地址省
	private String nowCityCode;// 收教材地址市
	private String nowDistrictCode;// 收教材地址县/区
	private String address;// 收教材地址地详细地址
	private String addressEditTime;// 收教材地址县/区
	private String jobType;// 职业类型 --- 对应字典：jobType
	private String jobStatus;// 在职状况
	private String wpProvinceCode;// 在职单位所在省
	private String wpCityCode;// 在职单位所在市
	private String wpDistrictCode;// 在职单位所在区/县
	private String wpAddress;// 在职单位所在地址
	private String wpTime;// 入职时间
	private String wpTelephone;// 在职单位电话
	private String emergencyContact;// 紧急电话
	private String myAnnexStatus;// 附件审核状态 1-未上传 2-待审核 3-已通过 4-未通过
	private String stdSource;// 学员来源 : D:钉钉引用 X学员系统 W:微信 A:app

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getPoliticalStatus() {
		return politicalStatus;
	}

	public void setPoliticalStatus(String politicalStatus) {
		this.politicalStatus = politicalStatus;
	}

	public String getEdcsType() {
		return edcsType;
	}

	public void setEdcsType(String edcsType) {
		this.edcsType = edcsType;
	}

	public String getRprType() {
		return rprType;
	}

	public void setRprType(String rprType) {
		this.rprType = rprType;
	}

	public String getRprProvinceCode() {
		return rprProvinceCode;
	}

	public void setRprProvinceCode(String rprProvinceCode) {
		this.rprProvinceCode = rprProvinceCode;
	}

	public String getRprCityCode() {
		return rprCityCode;
	}

	public void setRprCityCode(String rprCityCode) {
		this.rprCityCode = rprCityCode;
	}

	public String getRprDistrictCode() {
		return rprDistrictCode;
	}

	public void setRprDistrictCode(String rprDistrictCode) {
		this.rprDistrictCode = rprDistrictCode;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getNowProvinceCode() {
		return nowProvinceCode;
	}

	public void setNowProvinceCode(String nowProvinceCode) {
		this.nowProvinceCode = nowProvinceCode;
	}

	public String getNowCityCode() {
		return nowCityCode;
	}

	public void setNowCityCode(String nowCityCode) {
		this.nowCityCode = nowCityCode;
	}

	public String getNowDistrictCode() {
		return nowDistrictCode;
	}

	public void setNowDistrictCode(String nowDistrictCode) {
		this.nowDistrictCode = nowDistrictCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressEditTime() {
		return addressEditTime;
	}

	public void setAddressEditTime(String addressEditTime) {
		this.addressEditTime = addressEditTime;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getWpProvinceCode() {
		return wpProvinceCode;
	}

	public void setWpProvinceCode(String wpProvinceCode) {
		this.wpProvinceCode = wpProvinceCode;
	}

	public String getWpCityCode() {
		return wpCityCode;
	}

	public void setWpCityCode(String wpCityCode) {
		this.wpCityCode = wpCityCode;
	}

	public String getWpDistrictCode() {
		return wpDistrictCode;
	}

	public void setWpDistrictCode(String wpDistrictCode) {
		this.wpDistrictCode = wpDistrictCode;
	}

	public String getWpAddress() {
		return wpAddress;
	}

	public void setWpAddress(String wpAddress) {
		this.wpAddress = wpAddress;
	}

	public String getWpTime() {
		return wpTime;
	}

	public void setWpTime(String wpTime) {
		this.wpTime = wpTime;
	}

	public String getWpTelephone() {
		return wpTelephone;
	}

	public void setWpTelephone(String wpTelephone) {
		this.wpTelephone = wpTelephone;
	}

	public String getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public String getMyAnnexStatus() {
		return myAnnexStatus;
	}

	public void setMyAnnexStatus(String myAnnexStatus) {
		this.myAnnexStatus = myAnnexStatus;
	}

	public String getStdSource() {
		return stdSource;
	}

	public void setStdSource(String stdSource) {
		this.stdSource = stdSource;
	}
	
	@Override
	public void setId(Object key) {
		this.setStdId(String.valueOf(key));
	}

	@Override
	public Object getId() {
		return this.getStdId();
	}
}
