package com.yz.model.educational;

import java.io.Serializable;

//考场座位安排导出Excel类
public class ExamRoomSeatsExport implements Serializable{


    private String esNum;
    private String stdName;
    private String schoolRoll;
    private String grade;
    private String unvsName;
    private String pfsnLevel;
    private String pfsnName;
    private String examYear;
    private String provinceName;
    private String cityName;
    private String districtName;
    private String epName;
    private String time;
    private String placeName;
    private String eypCode;
    private String startTime;
    private String endTime;

    public String getEsNum() {
        return esNum;
    }

    public void setEsNum(String esNum) {
        this.esNum = esNum;
    }

    public String getStdName() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName = stdName;
    }

    public String getSchoolRoll() {
		return schoolRoll;
	}

	public void setSchoolRoll(String schoolRoll) {
		this.schoolRoll = schoolRoll;
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

    public String getExamYear() {
        return examYear;
    }

    public void setExamYear(String examYear) {
        this.examYear = examYear;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getEpName() {
        return epName;
    }

    public void setEpName(String epName) {
        this.epName = epName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getEypCode() {
        return eypCode;
    }

    public void setEypCode(String eypCode) {
        this.eypCode = eypCode;
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
}
