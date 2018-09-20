package com.yz.model;
/**
 * @Description: 现场确认学员信息
 * @Author: luxing
 * @Date 2018\8\3 0003 12:30
 **/
public class BdConfirmStudentInfo {
	private String confirmId;//现场确认id

	private String stdName;//学生姓名

	private String idCard;//身份证号

	private String pfsnLevel;//专业层次

	private String pfsnName;//专业名称

	private String unvsName;//院校名称

	private String taId;//考试县区id

	private String taName;//考试县区名称

	private String username;//预报名号

    private String password;//预报名密码

	private String registerNo;//预报名编号

	private String examPayStatus;//是否缴费(0：未缴费 1：已缴费)

	private Integer signStatus;//签到状态（0：未签到 1：已签到）

	private String signTime;//签到时间

    private String signUserId;//操作人（签到）

	private String recruit;//招生老师姓名

	private String mobile;//招生老师电话号码

	private String empStatus;//招生老师状态 对应字典 empStatus 1-在职 2-离职 3-休假

	private String examNo;//考生号

	private String sceneConfirmStatus;//是否确认（成考网确认状态）（0：未确认 1：已确认）

	private String sceneRemark;//备注

	private String updateTime;//最后更新时间

	private String updateUserId;//最后更新人id

	private String updateUser;//最后更新人姓名

    private Integer webRegisterStatus;//网报状态(0：待网待 1：网报成功)

    private String learnId;

    private String stdId;

    private String picCollectStatus;//相片采集状态（0：未采集 1：已采集）

    private String mobileBindStatus;//手机绑定状态（0：未绑定 1：已绑定）

    public String getConfirmId() {
        return confirmId;
    }

    public void setConfirmId(String confirmId) {
        this.confirmId = confirmId;
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

    public String getPfsnLevel() {
        return pfsnLevel;
    }

    public void setPfsnLevel(String pfsnLevel) {
        this.pfsnLevel = pfsnLevel;
    }

    public String getPfsnName() {
        return pfsnName;
    }

    public void setPfsnName(String pfsnName) {
        this.pfsnName = pfsnName;
    }

    public String getUnvsName() {
        return unvsName;
    }

    public void setUnvsName(String unvsName) {
        this.unvsName = unvsName;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getExamPayStatus() {
        return examPayStatus;
    }

    public void setExamPayStatus(String examPayStatus) {
        this.examPayStatus = examPayStatus;
    }

    public Integer getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Integer signStatus) {
        this.signStatus = signStatus;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getRecruit() {
        return recruit;
    }

    public void setRecruit(String recruit) {
        this.recruit = recruit;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(String empStatus) {
        this.empStatus = empStatus;
    }

    public String getExamNo() {
        return examNo;
    }

    public void setExamNo(String examNo) {
        this.examNo = examNo;
    }

    public String getSceneConfirmStatus() {
        return sceneConfirmStatus;
    }

    public void setSceneConfirmStatus(String sceneConfirmStatus) {
        this.sceneConfirmStatus = sceneConfirmStatus;
    }

    public String getSceneRemark() {
        return sceneRemark;
    }

    public void setSceneRemark(String sceneRemark) {
        this.sceneRemark = sceneRemark;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getSignUserId() {
        return signUserId;
    }

    public void setSignUserId(String signUserId) {
        this.signUserId = signUserId;
    }

    public Integer getWebRegisterStatus() {
        return webRegisterStatus;
    }

    public void setWebRegisterStatus(Integer webRegisterStatus) {
        this.webRegisterStatus = webRegisterStatus;
    }

    public String getLearnId() {
        return learnId;
    }

    public void setLearnId(String learnId) {
        this.learnId = learnId;
    }

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicCollectStatus() {
        return picCollectStatus;
    }

    public void setPicCollectStatus(String picCollectStatus) {
        this.picCollectStatus = picCollectStatus;
    }

    public String getMobileBindStatus() {
        return mobileBindStatus;
    }

    public void setMobileBindStatus(String mobileBindStatus) {
        this.mobileBindStatus = mobileBindStatus;
    }
}
