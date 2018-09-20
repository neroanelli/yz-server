package com.yz.network.examination.model;
/**
 * @Description: 现场确认学员信息
 * @Author: luxing
 * @Date 2018\8\3 0003 12:30
 **/
public class BdConfirmStudentInfo {
	private String confirmId;//现场确认id

	private String stdName;//学生姓名

    private String stdId;

	private String idCard;//身份证号

	private String pfsnLevel;//专业层次

	private String pfsnName;//专业名称

	private String unvsName;//院校名称

	private String taId;//考试县区id

	private String taName;//考试县区名称

	private String username;//预报名号

	private String registerNo;//预报名编号

	private String examPayStatus;//是否缴费(0：未缴费 1：已缴费)

	private Integer signStatus;//签到状态（0：未签到 1：已签到）

	private String signTime;//签到时间

    private String signUserId;//操作人id（签到）

	private String recruit;//招生老师姓名

	private String mobile;//招生老师电话号码

	private String empStatus;//招生老师状态 对应字典 empStatus 1-在职 2-离职 3-休假

	private String examNo;//考生号

	private Integer sceneConfirmStatus;//是否确认（成考网确认状态）（0：未确认 1：已确认）

    private String sceneConfirmTime;

	private String sceneRemark;//备注

	private String updateTime;//最后更新时间

	private String updateUserId;//最后更新人id

	private String updateUser;//最后更新人姓名

    private String startTime;//确认点开始时间

    private String cityCode;//城市编码

    private Integer webRegisterStatus;//网报状态(0：待网待 1：网报成功)

    private String pfsnCode;//专业编码

    private String workProve;//工作证明(0：无 1：有)

    private String cityName;//使用城市

    private String userAccount;//账号名称

    private String totalCount;//签到总数

    private String todayCount;//今天签到人数

    private String learnId;//学业id

    private String searchInfo;

    private String queueType;//排队类型A（专科），B（本科），D（未预约）

    private String no;//签到拿到的号码，跟类型组合展示

    private String waitNum;//等待人数

    private String scholarship;//优惠类型

    private String picCollectStatus;//相片采集状态（0：未采集 1：已采集）

    private String mobileBindStatus;//手机绑定状态（0：未绑定 1：已绑定）

    private String password;//预报名密码

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getPfsnCode() {
        return pfsnCode;
    }

    public void setPfsnCode(String pfsnCode) {
        this.pfsnCode = pfsnCode;
    }

    public String getWorkProve() {
        return workProve;
    }

    public void setWorkProve(String workProve) {
        this.workProve = workProve;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getTodayCount() {
        return todayCount;
    }

    public void setTodayCount(String todayCount) {
        this.todayCount = todayCount;
    }

    public String getLearnId() {
        return learnId;
    }

    public void setLearnId(String learnId) {
        this.learnId = learnId;
    }

    public String getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(String searchInfo) {
        this.searchInfo = searchInfo;
    }

    public String getQueueType() {
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getWaitNum() {
        return waitNum;
    }

    public void setWaitNum(String waitNum) {
        this.waitNum = waitNum;
    }

    public Integer getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Integer signStatus) {
        this.signStatus = signStatus;
    }

    public Integer getSceneConfirmStatus() {
        return sceneConfirmStatus;
    }

    public void setSceneConfirmStatus(Integer sceneConfirmStatus) {
        this.sceneConfirmStatus = sceneConfirmStatus;
    }

    public Integer getWebRegisterStatus() {
        return webRegisterStatus;
    }

    public void setWebRegisterStatus(Integer webRegisterStatus) {
        this.webRegisterStatus = webRegisterStatus;
    }

    public String getSceneConfirmTime() {
        return sceneConfirmTime;
    }

    public void setSceneConfirmTime(String sceneConfirmTime) {
        this.sceneConfirmTime = sceneConfirmTime;
    }

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId;
    }

    public String getScholarship() {
        return scholarship;
    }

    public void setScholarship(String scholarship) {
        this.scholarship = scholarship;
    }

    public String getSignUserId() {
        return signUserId;
    }

    public void setSignUserId(String signUserId) {
        this.signUserId = signUserId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
