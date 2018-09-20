package com.yz.model.recruit;

import com.yz.model.common.PageCondition;

public class BdLearnAnnexType extends PageCondition {
    private String id;
    private String recruitType;
    private String annexTypeName;
    private String annexTypeValue;
    private String isRequire;
    private String isVisible;
    private String isUpload;
    private String updateTime;
    private String updateUser;
    private String updateUserId;
    private String createUserId;
    private String createTime;
    private String createUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRecruitType() {
        return recruitType;
    }

    public void setRecruitType(String recruitType) {
        this.recruitType = recruitType == null ? null : recruitType.trim();
    }

    public String getAnnexTypeName() {
        return annexTypeName;
    }

    public void setAnnexTypeName(String annexTypeName) {
        this.annexTypeName = annexTypeName == null ? null : annexTypeName.trim();
    }

    public String getAnnexTypeValue() {
        return annexTypeValue;
    }

    public void setAnnexTypeValue(String annexTypeValue) {
        this.annexTypeValue = annexTypeValue == null ? null : annexTypeValue.trim();
    }

    public String getIsRequire() {
        return isRequire;
    }

    public void setIsRequire(String isRequire) {
        this.isRequire = isRequire == null ? null : isRequire.trim();
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible == null ? null : isVisible.trim();
    }

    public String getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(String isUpload) {
        this.isUpload = isUpload == null ? null : isUpload.trim();
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId == null ? null : updateUserId.trim();
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}