package com.yz.network.examination.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @ Author     ：林建彬.
 * @ Date       ：Created in 10:47 2018/8/8
 * @ Description：签到排号表对应的实体bean
 */
public class BdLearnQueueInfo implements Serializable {

    private String queueId;
    private String queueType;
    private String no;
    private String pfsnLevel;
    private String learnId;
    private String stdName;
    private String idCard;
    private String queueStatus;
    private String placeConfirmTime;
    private String queueNo;
    private String cityCode;
    private String signUserId;
    private Date signTime;

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
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

    public String getPfsnLevel() {
        return pfsnLevel;
    }

    public void setPfsnLevel(String pfsnLevel) {
        this.pfsnLevel = pfsnLevel;
    }

    public String getLearnId() {
        return learnId;
    }

    public void setLearnId(String learnId) {
        this.learnId = learnId;
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

    public String getQueueStatus() {
        return queueStatus;
    }

    public void setQueueStatus(String queueStatus) {
        this.queueStatus = queueStatus;
    }

    public String getPlaceConfirmTime() {
        return placeConfirmTime;
    }

    public void setPlaceConfirmTime(String placeConfirmTime) {
        this.placeConfirmTime = placeConfirmTime;
    }

    public String getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(String queueNo) {
        this.queueNo = queueNo;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getSignUserId() {
        return signUserId;
    }

    public void setSignUserId(String signUserId) {
        this.signUserId = signUserId;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }
}
