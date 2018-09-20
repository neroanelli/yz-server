package com.yz.model.statistics;

public class SceneConfirmStatQuery{

	private String unvsId;
	private String pfsnLevel;
	private String pfsnName;
	private String grade;

	private String recruit;
	private String taCityCode;
	private String campusId;
	private String dpId;
	private String scholarship;
	
	private String stdStage;
    private String[] stdStageArray;
    private String statGroup;
    private String pfsnId;
    private String empStatus;

	/**
	 * 1		按总人数降序、
	 * 2		按报考信息确认完成率降序、
	 * 3		按预约完成率降序、
	 * 4		按网报成功率降序、
	 * 5		按网报缴费完成率降序、
	 * 6		按签到完成率降序、
	 * 7		按确认完成率降序、
	 * 8		按考生号占比率降序
	 */
	private String orderBy;//帅选排序条件

	public String getUnvsId() {
		return unvsId;
	}

	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId;
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

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getRecruit() {
		return recruit;
	}

	public void setRecruit(String recruit) {
		this.recruit = recruit;
	}

	public String getTaCityCode() {
		return taCityCode;
	}

	public void setTaCityCode(String taCityCode) {
		this.taCityCode = taCityCode;
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

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	public String[] getStdStageArray() {
		return stdStageArray;
	}

	public void setStdStageArray(String[] stdStageArray) {
		this.stdStageArray = stdStageArray;
	}

	public String getStatGroup() {
		return statGroup;
	}

	public void setStatGroup(String statGroup) {
		this.statGroup = statGroup;
	}

	public String getPfsnId() {
		return pfsnId;
	}

	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getEmpStatus() {
		return empStatus;
	}

	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}
	
}
