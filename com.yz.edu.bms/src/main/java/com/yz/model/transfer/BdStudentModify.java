package com.yz.model.transfer;

import com.yz.model.common.PubInfo;

public class BdStudentModify extends PubInfo {
	private String modifyId;
	private String learnId;
	private String stdId;
	private String crId;
	private String taName;
	private String ntaName;
	private String npfsnName;
	private String nunvsName;
	private String reason;
	private String grade;
	private String empName;
	private String stdStage;
	private String oldStdStage;
	private String bcruptime;
	private String unvsName;
	private String pfsnName;
	private String pfsnCode;
	private String pfsnLevel;
	private String modifyType;
	private String checkType;
	private String checkOrder;
	private String stdName;
	private String sex;
	private String idType;
	private String nation;
	private String idCard;
	private String unvsId;
	private String taId;
	private String pfsnId;
	private String sg;
	private String scholarship;
	private String newStdName;
	private String newSex;
	private String newIdType;
	private String newNation;
	private String newIdCard;
	private String newUnvsId;
	private String npfsnLevel;
	private String newTaId;
	private String newPfsnId;
	private String newSg;
	private String newScholarship;
	private String newStdStage;
	private String isDel;
	private String learnStage;
	private String recruitType;
	private String fileUrl;
	private String remark;
	private String isComplete;
	private String updateTime;
	private String createTime;
	private String mobile;
	private String feeId;
	private String offerId;
	private String ext1;
	private String ext2;
	private String ext3;
	
	//原学历信息
	private String graduateUnvsName;
	private String graduateTime;
	private String graduateEdcsType;
	private String graduateProfession; 
	private String graduateDiploma;
	
	private String newGraduateUnvsName;
	private String newGraduateTime;
	private String newGraduateEdcsType;
	private String newGraduateProfession; 
	private String newGraduateDiploma;
	
	private String examPayStatus;

	//新增户口所在地相关字段
	private String rprAddress;//户口详细地址
	private String newHkCode;//户口所在地编码（对应网报）
	private String newRprAddress;//变更户口详细地址

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public String getNpfsnLevel() {
		return npfsnLevel;
	}

	public void setNpfsnLevel(String npfsnLevel) {
		this.npfsnLevel = npfsnLevel;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}

	public String getLearnStage() {
		return learnStage;
	}

	public void setLearnStage(String learnStage) {
		this.learnStage = learnStage;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getReason() {
		return reason;
	}

	public String getCrId() {
		return crId;
	}

	public void setCrId(String crId) {
		this.crId = crId;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}

	public String getBcruptime() {
		return bcruptime;
	}

	public void setBcruptime(String bcruptime) {
		this.bcruptime = bcruptime;
	}

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}

	public String getPfsnName() {
		return pfsnName;
	}

	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}

	public String getPfsnCode() {
		return pfsnCode;
	}

	public void setPfsnCode(String pfsnCode) {
		this.pfsnCode = pfsnCode;
	}

	public String getTaName() {
		return taName;
	}

	public void setTaName(String taName) {
		this.taName = taName;
	}

	public String getNtaName() {
		return ntaName;
	}

	public void setNtaName(String ntaName) {
		this.ntaName = ntaName;
	}

	public String getNpfsnName() {
		return npfsnName;
	}

	public void setNpfsnName(String npfsnName) {
		this.npfsnName = npfsnName;
	}

	public String getNunvsName() {
		return nunvsName;
	}

	public void setNunvsName(String nunvsName) {
		this.nunvsName = nunvsName;
	}

	public String getCheckOrder() {
		return checkOrder;
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	public void setCheckOrder(String checkOrder) {
		this.checkOrder = checkOrder;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getModifyId() {
		return modifyId;
	}

	public void setModifyId(String modifyId) {
		this.modifyId = modifyId == null ? null : modifyId.trim();
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId == null ? null : learnId.trim();
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId == null ? null : stdId.trim();
	}

	public String getModifyType() {
		return modifyType;
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType == null ? null : modifyType.trim();
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName == null ? null : stdName.trim();
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex == null ? null : sex.trim();
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType == null ? null : idType.trim();
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation == null ? null : nation.trim();
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard == null ? null : idCard.trim();
	}

	public String getUnvsId() {
		return unvsId;
	}

	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId == null ? null : unvsId.trim();
	}

	public String getTaId() {
		return taId;
	}

	public void setTaId(String taId) {
		this.taId = taId == null ? null : taId.trim();
	}

	public String getPfsnId() {
		return pfsnId;
	}

	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId == null ? null : pfsnId.trim();
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship == null ? null : scholarship.trim();
	}

	public String getNewStdName() {
		return newStdName;
	}

	public void setNewStdName(String newStdName) {
		this.newStdName = newStdName == null ? null : newStdName.trim();
	}

	public String getNewSex() {
		return newSex;
	}

	public void setNewSex(String newSex) {
		this.newSex = newSex == null ? null : newSex.trim();
	}

	public String getNewIdType() {
		return newIdType;
	}

	public void setNewIdType(String newIdType) {
		this.newIdType = newIdType == null ? null : newIdType.trim();
	}

	public String getNewNation() {
		return newNation;
	}

	public void setNewNation(String newNation) {
		this.newNation = newNation == null ? null : newNation.trim();
	}

	public String getNewIdCard() {
		return newIdCard;
	}

	public void setNewIdCard(String newIdCard) {
		this.newIdCard = newIdCard == null ? null : newIdCard.trim();
	}

	public String getNewUnvsId() {
		return newUnvsId;
	}

	public void setNewUnvsId(String newUnvsId) {
		this.newUnvsId = newUnvsId == null ? null : newUnvsId.trim();
	}

	public String getNewTaId() {
		return newTaId;
	}

	public void setNewTaId(String newTaId) {
		this.newTaId = newTaId == null ? null : newTaId.trim();
	}

	public String getNewPfsnId() {
		return newPfsnId;
	}

	public void setNewPfsnId(String newPfsnId) {
		this.newPfsnId = newPfsnId == null ? null : newPfsnId.trim();
	}

	public String getNewScholarship() {
		return newScholarship;
	}

	public void setNewScholarship(String newScholarship) {
		this.newScholarship = newScholarship == null ? null : newScholarship.trim();
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel == null ? null : isDel.trim();
	}

	public String getSg() {
		return sg;
	}

	public void setSg(String sg) {
		this.sg = sg;
	}

	public String getNewSg() {
		return newSg;
	}

	public void setNewSg(String newSg) {
		this.newSg = newSg;
	}

	public String getNewStdStage() {
		return newStdStage;
	}

	public void setNewStdStage(String newStdStage) {
		this.newStdStage = newStdStage;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getOldStdStage() {
		return oldStdStage;
	}

	public void setOldStdStage(String oldStdStage) {
		this.oldStdStage = oldStdStage;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getGraduateUnvsName() {
		return graduateUnvsName;
	}

	public void setGraduateUnvsName(String graduateUnvsName) {
		this.graduateUnvsName = graduateUnvsName;
	}

	public String getGraduateTime() {
		return graduateTime;
	}

	public void setGraduateTime(String graduateTime) {
		this.graduateTime = graduateTime;
	}

	public String getGraduateEdcsType() {
		return graduateEdcsType;
	}

	public void setGraduateEdcsType(String graduateEdcsType) {
		this.graduateEdcsType = graduateEdcsType;
	}

	public String getGraduateProfession() {
		return graduateProfession;
	}

	public void setGraduateProfession(String graduateProfession) {
		this.graduateProfession = graduateProfession;
	}

	public String getGraduateDiploma() {
		return graduateDiploma;
	}

	public void setGraduateDiploma(String graduateDiploma) {
		this.graduateDiploma = graduateDiploma;
	}

	public String getNewGraduateUnvsName() {
		return newGraduateUnvsName;
	}

	public void setNewGraduateUnvsName(String newGraduateUnvsName) {
		this.newGraduateUnvsName = newGraduateUnvsName;
	}

	public String getNewGraduateTime() {
		return newGraduateTime;
	}

	public void setNewGraduateTime(String newGraduateTime) {
		this.newGraduateTime = newGraduateTime;
	}

	public String getNewGraduateEdcsType() {
		return newGraduateEdcsType;
	}

	public void setNewGraduateEdcsType(String newGraduateEdcsType) {
		this.newGraduateEdcsType = newGraduateEdcsType;
	}

	public String getNewGraduateProfession() {
		return newGraduateProfession;
	}

	public void setNewGraduateProfession(String newGraduateProfession) {
		this.newGraduateProfession = newGraduateProfession;
	}

	public String getNewGraduateDiploma() {
		return newGraduateDiploma;
	}

	public void setNewGraduateDiploma(String newGraduateDiploma) {
		this.newGraduateDiploma = newGraduateDiploma;
	}

	public String getExamPayStatus() {
		return examPayStatus;
	}

	public void setExamPayStatus(String examPayStatus) {
		this.examPayStatus = examPayStatus;
	}

	public String getRprAddress() {
		return rprAddress;
	}

	public void setRprAddress(String rprAddress) {
		this.rprAddress = rprAddress;
	}

	public String getNewRprAddress() {
		return newRprAddress;
	}

	public void setNewRprAddress(String newRprAddress) {
		this.newRprAddress = newRprAddress;
	}

	public String getNewHkCode() {
		return newHkCode;
	}

	public void setNewHkCode(String newHkCode) {
		this.newHkCode = newHkCode;
	}
	
}