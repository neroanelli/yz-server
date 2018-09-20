package com.yz.model;

public class StudentHistory {

	private String unvsName;
	private String profession;
	private String graduateTime;
	private String diploma;
	private String edcsType;
	private String isOpenUnvs;//是否电大毕业
	private String studyType;//原学历学习类型
	private String materialType;//原学历证明材料类型
	private String materialCode;//证明材料编号
	private String learnId;
	private String stdId;

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName == null ? null : unvsName.trim();
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession == null ? null : profession.trim();
	}

	public String getGraduateTime() {
		return graduateTime;
	}

	public void setGraduateTime(String graduateTime) {
		this.graduateTime = graduateTime == null ? null : graduateTime.trim();
	}

	public String getDiploma() {
		return diploma;
	}

	public void setDiploma(String diploma) {
		this.diploma = diploma == null ? null : diploma.trim();
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId == null ? null : learnId.trim();
	}

	public String getEdcsType() {
		return edcsType;
	}

	public void setEdcsType(String edcsType) {
		this.edcsType = edcsType == null ? null : edcsType.trim();
	}

	public String getIsOpenUnvs() {
		return isOpenUnvs;
	}

	public void setIsOpenUnvs(String isOpenUnvs) {
		this.isOpenUnvs = isOpenUnvs;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getStudyType() {
		return studyType;
	}

	public void setStudyType(String studyType) {
		this.studyType = studyType;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	
	
}
