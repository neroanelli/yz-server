package com.yz.model.stdService;

import java.io.Serializable;

/**
 * 毕业证发放配置
 */
public class StuDiplomaConfig implements Serializable {


    private static final long serialVersionUID = -1357058557277182685L;
    private String configId;
    private String diplomaId;
    private String placeId;
    private String date;
    private String startTime;
    private String endTime;
    private String number;
    private String availableNumbers;
    private String status;
    private String updateTime;
    private String updateUser;
    private String updateUserId;

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getDiplomaId() {
        return diplomaId;
    }

    public void setDiplomaId(String diplomaId) {
        this.diplomaId = diplomaId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAvailableNumbers() {
        return availableNumbers;
    }

    public void setAvailableNumbers(String availableNumbers) {
        this.availableNumbers = availableNumbers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
