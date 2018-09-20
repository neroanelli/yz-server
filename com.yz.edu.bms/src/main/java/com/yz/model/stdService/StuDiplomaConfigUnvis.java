package com.yz.model.stdService;

import java.io.Serializable;

/**
 * 毕业证发放配置关联
 */
public class StuDiplomaConfigUnvis implements Serializable {

    private static final long serialVersionUID = -6665097041222909667L;
    private String id;
    private String configId;
    private String grade;
    private String unvsId;
    private String pfsnLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getUnvsId() {
        return unvsId;
    }

    public void setUnvsId(String unvsId) {
        this.unvsId = unvsId;
    }

    public String getPfsnLevel() {
        return pfsnLevel;
    }

    public void setPfsnLevel(String pfsnLevel) {
        this.pfsnLevel = pfsnLevel;
    }
}
