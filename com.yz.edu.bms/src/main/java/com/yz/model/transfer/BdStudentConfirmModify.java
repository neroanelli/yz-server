package com.yz.model.transfer;

public class BdStudentConfirmModify{
	private String modifyId;
	private String learnId;
	private String stdId;
	private String crId;
	private String taName;   //考试区县名称
	private String nTaName;  //新考试区县名称
	private String pfsnName;//专业名称
	private String nPfsnName;//新专业名称
	private String unvsName;  //院校名称
	private String nUnvsName;  //新院校名称
	private String grade;   //年级
	private String sex;   //性别
	private String nSex;   //新性别
	private String nation;  //民族
	private String nNation; //新民族
	private String idCard;  //身份证
	private String nIdCard; //新身份证
	private String stdName; //学员名字
	private String newStdName;   //新学员名字
	private String pfsnLevel;  //专业层次
	private String npfsnLevel;	//新专业层次
	private String pfsnId;  //专业层次ID
	private String nPfsnId;  //新专业层次ID
	private String taId;  //考区Id
	private String nTaId;   //新考区Id
	private String scholarship;  //优惠类型
	private String nScholarship; //新优惠类型
	private String stdStage;  //学员阶段

	private String examPayStatus;//缴费状态
	private String pfsnCode;  //专业编码
	private String nPfsnCode;  //新专业编码

	private String educationType;  //原学历类型
	private String nEducationType;  //新学历类型
	private String graduationUnvs;   //原毕业院校
	private String nGraduationUnvs;		//新毕业院校
	private String graduationTime;  //原毕业时间
	private String nGraduationTime;   //新毕业时间
	private String graduationPfsn;  //原毕业专业
	private String nGraduationPfsn;  //新毕业专业
	private String graduationNo;  //原毕业编号
	private String nGraduationNo;  //新毕业编号
	
	private String createUser;  //创建人
	private String createTime;  //创建时间
	private String remark; //备注
	//新增户口所在地相关字段
	private String rprAddress;//户口详细地址
	private String newRprAddress;//变更户口详细地址
	public String getModifyId() {
		return modifyId;
	}

	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
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

	public String getCrId() {
		return crId;
	}

	public void setCrId(String crId) {
		this.crId = crId;
	}

	public String getTaName() {
		return taName;
	}

	public void setTaName(String taName) {
		this.taName = taName;
	}

	public String getnTaName() {
		return nTaName;
	}

	public void setnTaName(String nTaName) {
		this.nTaName = nTaName;
	}

	public String getPfsnName() {
		return pfsnName;
	}

	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}

	public String getnPfsnName() {
		return nPfsnName;
	}

	public void setnPfsnName(String nPfsnName) {
		this.nPfsnName = nPfsnName;
	}

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}

	public String getnUnvsName() {
		return nUnvsName;
	}

	public void setnUnvsName(String nUnvsName) {
		this.nUnvsName = nUnvsName;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getnSex() {
		return nSex;
	}

	public void setnSex(String nSex) {
		this.nSex = nSex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getnNation() {
		return nNation;
	}

	public void setnNation(String nNation) {
		this.nNation = nNation;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getnIdCard() {
		return nIdCard;
	}

	public void setnIdCard(String nIdCard) {
		this.nIdCard = nIdCard;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getNewStdName() {
		return newStdName;
	}

	public void setNewStdName(String newStdName) {
		this.newStdName = newStdName;
	}

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}

	public String getNpfsnLevel() {
		return npfsnLevel;
	}

	public void setNpfsnLevel(String npfsnLevel) {
		this.npfsnLevel = npfsnLevel;
	}

	public String getPfsnId() {
		return pfsnId;
	}

	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId;
	}

	public String getnPfsnId() {
		return nPfsnId;
	}

	public void setnPfsnId(String nPfsnId) {
		this.nPfsnId = nPfsnId;
	}

	public String getTaId() {
		return taId;
	}

	public void setTaId(String taId) {
		this.taId = taId;
	}

	public String getnTaId() {
		return nTaId;
	}

	public void setnTaId(String nTaId) {
		this.nTaId = nTaId;
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	public String getnScholarship() {
		return nScholarship;
	}

	public void setnScholarship(String nScholarship) {
		this.nScholarship = nScholarship;
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	public String getPfsnCode() {
		return pfsnCode;
	}

	public void setPfsnCode(String pfsnCode) {
		this.pfsnCode = pfsnCode;
	}

	public String getnPfsnCode() {
		return nPfsnCode;
	}

	public void setnPfsnCode(String nPfsnCode) {
		this.nPfsnCode = nPfsnCode;
	}

	public String getEducationType() {
		return educationType;
	}

	public void setEducationType(String educationType) {
		this.educationType = educationType;
	}

	public String getnEducationType() {
		return nEducationType;
	}

	public void setnEducationType(String nEducationType) {
		this.nEducationType = nEducationType;
	}

	public String getGraduationUnvs() {
		return graduationUnvs;
	}

	public void setGraduationUnvs(String graduationUnvs) {
		this.graduationUnvs = graduationUnvs;
	}

	public String getnGraduationUnvs() {
		return nGraduationUnvs;
	}

	public void setnGraduationUnvs(String nGraduationUnvs) {
		this.nGraduationUnvs = nGraduationUnvs;
	}

	public String getGraduationTime() {
		return graduationTime;
	}

	public void setGraduationTime(String graduationTime) {
		this.graduationTime = graduationTime;
	}

	public String getnGraduationTime() {
		return nGraduationTime;
	}

	public void setnGraduationTime(String nGraduationTime) {
		this.nGraduationTime = nGraduationTime;
	}

	public String getGraduationPfsn() {
		return graduationPfsn;
	}

	public void setGraduationPfsn(String graduationPfsn) {
		this.graduationPfsn = graduationPfsn;
	}

	public String getnGraduationPfsn() {
		return nGraduationPfsn;
	}

	public void setnGraduationPfsn(String nGraduationPfsn) {
		this.nGraduationPfsn = nGraduationPfsn;
	}

	public String getGraduationNo() {
		return graduationNo;
	}

	public void setGraduationNo(String graduationNo) {
		this.graduationNo = graduationNo;
	}

	public String getnGraduationNo() {
		return nGraduationNo;
	}

	public void setnGraduationNo(String nGraduationNo) {
		this.nGraduationNo = nGraduationNo;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
}