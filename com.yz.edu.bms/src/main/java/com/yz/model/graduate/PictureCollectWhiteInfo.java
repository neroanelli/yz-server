package com.yz.model.graduate;

import java.io.Serializable;

import com.yz.model.common.PubInfo;

/**
 * 毕业证图像采集-白名单
 * @author jyt
 */
public class PictureCollectWhiteInfo extends PubInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4640394888721590802L;
	
	private String stdId;              //学员id
	private String grade;              //年级
	private String learnId;            //学业编号
	private String stdStage;           //学员状态
	private String pfsnName;           //专业名称
	private String pfsnCode;           //专业代码
	private String pfsnLevel;          //专业层次
	private String unvsName;           //院校名称
	private String picCollectId;       //任务id
	private String stdName;            //学员姓名
	private String recruitType;        //招生类型
	private String tutor;              //辅导员
	private String idCard;
	private String tutorName;
	private String stdType;
	private String tutorStatus;

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
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

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}

	public String getPicCollectId() {
		return picCollectId;
	}

	public void setPicCollectId(String picCollectId) {
		this.picCollectId = picCollectId;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}

	public String getTutor() {
		return tutor;
	}

	public void setTutor(String tutor) {
		this.tutor = tutor;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getTutorName() {
		return tutorName;
	}

	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}

	public String getStdType() {
		return stdType;
	}

	public void setStdType(String stdType) {
		this.stdType = stdType;
	}

	public String getTutorStatus() {
		return tutorStatus;
	}

	public void setTutorStatus(String tutorStatus) {
		this.tutorStatus = tutorStatus;
	}
}
