package com.yz.model.stdService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class StuDiplomaTCInfo implements Serializable {

    private static final long serialVersionUID = -4887327669522872258L;
    private String configId;
    private String diplomaId;
    private String placeId;
    private String status;
    private String[] delIds;//前端删除关联记录
    private List<StuDiplomaConfigUnvis> stuDiplomaConfigUnvis;//保存前端过来的院校限定配置
    private List<StuDiplomaConfig> StuDiplomaConfigs;//保存前端过来的多个时间配置

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getDelIds() {
        return delIds;
    }

    public void setDelIds(String[] delIds) {
        this.delIds = delIds;
    }

    public List<StuDiplomaConfigUnvis> getStuDiplomaConfigUnvis() {
        return stuDiplomaConfigUnvis;
    }

    public void setStuDiplomaConfigUnvis(List<StuDiplomaConfigUnvis> stuDiplomaConfigUnvis) {
        this.stuDiplomaConfigUnvis = stuDiplomaConfigUnvis;
    }

    public List<StuDiplomaConfig> getStuDiplomaConfigs() {
        return StuDiplomaConfigs;
    }

    public void setStuDiplomaConfigs(List<StuDiplomaConfig> stuDiplomaConfigs) {
        StuDiplomaConfigs = stuDiplomaConfigs;
    }
}
