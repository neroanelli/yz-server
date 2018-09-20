package com.yz.network.examination.vo;

import java.io.Serializable;

public class LoginUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7808369757449516499L;
	/** 用户Id */
	private String userId;
	/** 用户名 */
	private String userName;
	private String realName;//真实姓名

	private String password;
	private String isCallNum;//是否叫号
	private String isSign;//是否签到
	private String isNetExam;//是否网报
	private String callNumCity;//叫号城市
	private String callNumCityCode;//叫号城市编码
	private String signCity;//签到城市
	private String signCityCode;//签到城市编码
	private String isBlock;//是否停用

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsCallNum() {
        return isCallNum;
    }

    public void setIsCallNum(String isCallNum) {
        this.isCallNum = isCallNum;
    }

    public String getIsSign() {
        return isSign;
    }

    public void setIsSign(String isSign) {
        this.isSign = isSign;
    }

    public String getIsNetExam() {
        return isNetExam;
    }

    public void setIsNetExam(String isNetExam) {
        this.isNetExam = isNetExam;
    }

    public String getCallNumCity() {
        return callNumCity;
    }

    public void setCallNumCity(String callNumCity) {
        this.callNumCity = callNumCity;
    }

    public String getCallNumCityCode() {
        return callNumCityCode;
    }

    public void setCallNumCityCode(String callNumCityCode) {
        this.callNumCityCode = callNumCityCode;
    }

    public String getSignCity() {
        return signCity;
    }

    public void setSignCity(String signCity) {
        this.signCity = signCity;
    }

    public String getSignCityCode() {
        return signCityCode;
    }

    public void setSignCityCode(String signCityCode) {
        this.signCityCode = signCityCode;
    }

    public String getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(String isBlock) {
        this.isBlock = isBlock;
    }
}
