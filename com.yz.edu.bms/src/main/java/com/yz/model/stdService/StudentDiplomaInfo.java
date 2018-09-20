package com.yz.model.stdService;

import java.io.Serializable;

/**
 * 毕业证发放
 * @author zlp
 * @date 2018年6月20日 上午10:44:55
 */
public class StudentDiplomaInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8689493393651009781L;
	
	private String followId;
	 private String taskTitle;
	private String taskId;
	private String learnId;
	private String recruitType;
    private String schoolRoll;
    private String stdNo;
    private String stdId;
    private String stdName;
    private String idCard;
    private String grade;
    private String unvsName;
    private String pfsnCode;
    private String pfsnName;
    private String pfsnLevel;
    private String stdStage;
    private String isView;
    private String viewTime;
    private String diplomaCode;
    private String isAffirm;
    private String configId;
    private String placeId;//毕业证发放地址ID
    private String placeName;//毕业证发放地址名称
    private String affirmStartTime;
    private String affirmEndTime;
    private String unconfirmReason;
    private String otherReason;
    private String receiveStatus;
    private String receiveProvince;//领取省份
    private String receiveCity;//领取城市
    private String receiveDistrict;//领取区
    private String receiveAddress;//确认地址
    private String receiveProvinceName;
    private String receiveCityName;
    private String receiveDistrictName;
    private String receiveTime;
    private String isMail;
    private String invoiceNo;
    private String addressee;//收件人
    private String mobile;//联系电话
    private String province;
    private String city;
    private String district;
    private String address;
    private String provinceName;//邮寄地址省市区名称
    private String logisticsNo;
    private String tutor;
    private String isReset;
    private String remark;
    
    private String diplomaId;
    
	public String getFollowId() {
		return followId;
	}
	public void setFollowId(String followId) {
		this.followId = followId;
	}
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getLearnId() {
		return learnId;
	}
	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}
	public String getSchoolRoll() {
		return schoolRoll;
	}
	public void setSchoolRoll(String schoolRoll) {
		this.schoolRoll = schoolRoll;
	}
	public String getStdNo() {
		return stdNo;
	}
	public void setStdNo(String stdNo) {
		this.stdNo = stdNo;
	}	
	public String getStdId() {
		return stdId;
	}
	public void setStdId(String stdId) {
		this.stdId = stdId;
	}
	public String getStdName() {
		return stdName;
	}
	public void setStdName(String stdName) {
		this.stdName = stdName;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getUnvsName() {
		return unvsName;
	}
	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}
	public String getPfsnCode() {
		return pfsnCode;
	}
	public void setPfsnCode(String pfsnCode) {
		this.pfsnCode = pfsnCode;
	}
	public String getPfsnName() {
		return pfsnName;
	}
	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}
	public String getPfsnLevel() {
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}
	public String getStdStage() {
		return stdStage;
	}
	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}
	public String getIsView() {
		return isView;
	}
	public void setIsView(String isView) {
		this.isView = isView;
	}
	public String getViewTime() {
		return viewTime;
	}
	public void setViewTime(String viewTime) {
		this.viewTime = viewTime;
	}
	public String getDiplomaCode() {
		return diplomaCode;
	}
	public void setDiplomaCode(String diplomaCode) {
		this.diplomaCode = diplomaCode;
	}
	public String getIsAffirm() {
		return isAffirm;
	}
	public void setIsAffirm(String isAffirm) {
		this.isAffirm = isAffirm;
	}
	
	
	public String getAffirmStartTime() {
		return affirmStartTime;
	}
	public void setAffirmStartTime(String affirmStartTime) {
		this.affirmStartTime = affirmStartTime;
	}
	public String getAffirmEndTime() {
		return affirmEndTime;
	}
	public void setAffirmEndTime(String affirmEndTime) {
		this.affirmEndTime = affirmEndTime;
	}
	public String getUnconfirmReason() {
		return unconfirmReason;
	}
	public void setUnconfirmReason(String unconfirmReason) {
		this.unconfirmReason = unconfirmReason;
	}
	public String getOtherReason() {
		return otherReason;
	}
	public void setOtherReason(String otherReason) {
		this.otherReason = otherReason;
	}
	public String getReceiveStatus() {
		return receiveStatus;
	}
	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
	public String getReceiveCity() {
		return receiveCity;
	}
	public void setReceiveCity(String receiveCity) {
		this.receiveCity = receiveCity;
	}
	public String getReceiveAddress() {
		return receiveAddress;
	}
	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getIsMail() {
		return isMail;
	}
	public void setIsMail(String isMail) {
		this.isMail = isMail;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getAddressee() {
		return addressee;
	}
	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLogisticsNo() {
		return logisticsNo;
	}
	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}
	public String getTutor() {
		return tutor;
	}
	public void setTutor(String tutor) {
		this.tutor = tutor;
	}
	public String getIsReset() {
		return isReset;
	}
	public void setIsReset(String isReset) {
		this.isReset = isReset;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getConfigId() {
		return configId;
	}
	public void setConfigId(String configId) {
		this.configId = configId;
	}
	public String getDiplomaId() {
		return diplomaId;
	}
	public void setDiplomaId(String diplomaId) {
		this.diplomaId = diplomaId;
	}
	public String getReceiveProvince() {
		return receiveProvince;
	}
	public void setReceiveProvince(String receiveProvince) {
		this.receiveProvince = receiveProvince;
	}
	public String getReceiveDistrict() {
		return receiveDistrict;
	}
	public void setReceiveDistrict(String receiveDistrict) {
		this.receiveDistrict = receiveDistrict;
	}
	public String getReceiveProvinceName() {
		return receiveProvinceName;
	}
	public void setReceiveProvinceName(String receiveProvinceName) {
		this.receiveProvinceName = receiveProvinceName;
	}
	public String getReceiveCityName() {
		return receiveCityName;
	}
	public void setReceiveCityName(String receiveCityName) {
		this.receiveCityName = receiveCityName;
	}
	public String getReceiveDistrictName() {
		return receiveDistrictName;
	}
	public void setReceiveDistrictName(String receiveDistrictName) {
		this.receiveDistrictName = receiveDistrictName;
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public String getRecruitType() {
		return recruitType;
	}
	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	
	
    
	
}
