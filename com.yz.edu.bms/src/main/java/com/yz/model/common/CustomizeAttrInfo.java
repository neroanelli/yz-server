package com.yz.model.common;

/**
 * 自定义属性信息
 * @author lx
 * @date 2018年7月30日 下午4:23:31
 */
public class CustomizeAttrInfo {

	private int id;            //属性id
	private String defCatalog;    //所属目录
	private String defName;       //名称
	private String defLabel;      //描述
	private String attrValue;     //值
	private int attrSeq;
	private String defDs;
	private String defDefault;
	private String defDsType;
	private String refHandler;
	private String defType;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDefCatalog() {
		return defCatalog;
	}
	public void setDefCatalog(String defCatalog) {
		this.defCatalog = defCatalog;
	}
	public String getDefName() {
		return defName;
	}
	public void setDefName(String defName) {
		this.defName = defName;
	}
	public String getAttrValue() {
		return attrValue;
	}
	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}
	public int getAttrSeq() {
		return attrSeq;
	}
	public void setAttrSeq(int attrSeq) {
		this.attrSeq = attrSeq;
	}
	public String getDefLabel() {
		return defLabel;
	}
	public void setDefLabel(String defLabel) {
		this.defLabel = defLabel;
	}
	public String getDefDs() {
		return defDs;
	}
	public void setDefDs(String defDs) {
		this.defDs = defDs;
	}
	public String getDefDefault() {
		return defDefault;
	}
	public void setDefDefault(String defDefault) {
		this.defDefault = defDefault;
	}
	public String getDefDsType() {
		return defDsType;
	}
	public void setDefDsType(String defDsType) {
		this.defDsType = defDsType;
	}
	public String getRefHandler() {
		return refHandler;
	}
	public void setRefHandler(String refHandler) {
		this.refHandler = refHandler;
	}
	public String getDefType() {
		return defType;
	}
	public void setDefType(String defType) {
		this.defType = defType;
	}
}
