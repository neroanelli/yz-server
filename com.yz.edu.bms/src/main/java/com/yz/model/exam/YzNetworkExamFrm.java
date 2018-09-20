package com.yz.model.exam;

import java.io.Serializable;

/**
 * @ Author     ：林建彬.
 * @ Date       ：Created in 16:01 2018/9/2
 * @ Description：网报表单
 */
public class YzNetworkExamFrm implements Serializable {

    private static final long serialVersionUID = 1L;
    Long id;
    String frmId;
    String frmName;
    String frmType;
    String frmDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrmId() {
        return frmId;
    }

    public void setFrmId(String frmId) {
        this.frmId = frmId;
    }

    public String getFrmName() {
        return frmName;
    }

    public void setFrmName(String frmName) {
        this.frmName = frmName;
    }

    public String getFrmType() {
        return frmType;
    }

    public void setFrmType(String frmType) {
        this.frmType = frmType;
    }

    public String getFrmDate() {
        return frmDate;
    }

    public void setFrmDate(String frmDate) {
        this.frmDate = frmDate;
    }
}
