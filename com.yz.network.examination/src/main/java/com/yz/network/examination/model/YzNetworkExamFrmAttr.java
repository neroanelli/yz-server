package com.yz.network.examination.model;

@SuppressWarnings("serial")
public class YzNetworkExamFrmAttr implements java.io.Serializable{
	
    private Integer id;

    private Integer frmId; // frmId

    private String attrName; // 属性名称

    private String attrVal; // 属性值

    private Integer attrSeq; // 属性序列 
  
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFrmId() {
        return frmId;
    }

    public void setFrmId(Integer frmId) {
        this.frmId = frmId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName == null ? null : attrName.trim();
    }

    public String getAttrVal() {
        return attrVal;
    }

    public void setAttrVal(String attrVal) {
        this.attrVal = attrVal == null ? null : attrVal.trim();
    }

    public Integer getAttrSeq() {
        return attrSeq;
    }

    public void setAttrSeq(Integer attrSeq) {
        this.attrSeq = attrSeq;
    } 
}