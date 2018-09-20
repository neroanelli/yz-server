package com.yz.model.stdService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class StuDiplomaTC implements Serializable {

    private static final long serialVersionUID = -8890453207189701390L;
    private String configId;
    private String diplomaId;
    private String taskName;
    private String placeId;
    private String placeName;
    private String province;
    private String address;
    private String startTime;
    private String endTime;
    private String number;
    private String availableNumbers;
    private String status;
    private String updateTime;
    private String updateUser;
    private String updateUserId;
    private List<Map<String,Object>> stuDiplomaTCU;//院校配置

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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public List<Map<String, Object>> getStuDiplomaTCU() {
        return stuDiplomaTCU;
    }

    public void setStuDiplomaTCU(List<Map<String, Object>> stuDiplomaTCU) {
        this.stuDiplomaTCU = stuDiplomaTCU;
    }
}
