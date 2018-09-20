package com.yz.model;

import java.util.Date;

/**
 * @Description:学员上传回执类
 * @Author: luxing
 * @Date 2018\8\17 0017 14:44
 **/
public class BdConfirmReceipt {

    private String stdId;//学员id

    private String learnId;//学业id

    private String examNo;//考生号

    private String annexId;//附件id

    private String annexType;//附件类型

    private String annexName;//附件名称

    private String annexUrl;//附件url

    private String annexStatus;//附件状态

    private String uploadUser;//上传人姓名

    private String uploadUserId;//上传人用户ID

    private Date uploadTime;//上传时间

    private Date updateTime;//更新时间

    private String updateUser;

    private String updateUserId;

    private String isRequire;//是否必传

    private String operateNum;//学员操作机会，默认为1（学员只能提交一次）

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId;
    }

    public String getLearnId() {
        return learnId;
    }

    public void setLearnId(String learnId) {
        this.learnId = learnId;
    }

    public String getExamNo() {
        return examNo;
    }

    public void setExamNo(String examNo) {
        this.examNo = examNo;
    }

    public String getAnnexType() {
        return annexType;
    }

    public void setAnnexType(String annexType) {
        this.annexType = annexType;
    }

    public String getAnnexName() {
        return annexName;
    }

    public void setAnnexName(String annexName) {
        this.annexName = annexName;
    }

    public String getAnnexUrl() {
        return annexUrl;
    }

    public void setAnnexUrl(String annexUrl) {
        this.annexUrl = annexUrl;
    }

    public String getAnnexStatus() {
        return annexStatus;
    }

    public void setAnnexStatus(String annexStatus) {
        this.annexStatus = annexStatus;
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    public String getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(String uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsRequire() {
        return isRequire;
    }

    public void setIsRequire(String isRequire) {
        this.isRequire = isRequire;
    }

    public String getAnnexId() {
        return annexId;
    }

    public void setAnnexId(String annexId) {
        this.annexId = annexId;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getOperateNum() {
        return operateNum;
    }

    public void setOperateNum(String operateNum) {
        this.operateNum = operateNum;
    }
}
