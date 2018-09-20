package com.yz.model.annex;

import java.io.Serializable;

public class BdsLearnAnnexTypeInfo implements Serializable {

    private static final long serialVersionUID = 9136805738562883472L;

    private String recruitType;
    private String annexTypeName;
    private String annexType;
    private String isRequire;
    private String isVisible;
    private String isUpload;
    private String updateTime;
    private String updateUser;
    private String updateUserId;
    private String createUserId;
    private String createTime;
    private String createUser;
    private String annexUrl;
    private String annexStatus;

    public String getRecruitType() {
        return recruitType;
    }

    public void setRecruitType(String recruitType) {
        this.recruitType = recruitType;
    }

    public String getAnnexTypeName() {
        return annexTypeName;
    }

    public void setAnnexTypeName(String annexTypeName) {
        this.annexTypeName = annexTypeName;
    }

    public String getAnnexType() {
        return annexType;
    }

    public void setAnnexType(String annexType) {
        this.annexType = annexType;
    }

    public String getIsRequire() {
        return isRequire;
    }

    public void setIsRequire(String isRequire) {
        this.isRequire = isRequire;
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
    }

    public String getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(String isUpload) {
        this.isUpload = isUpload;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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
}
