package com.yz.network.examination.model;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.yz.network.examination.format.YzNetWorkFormater;
import com.yz.util.StringUtil;

@SuppressWarnings("serial")
public class YzNetworkExamInfoFrm implements java.io.Serializable {

	private Integer id;

	private String frmId; // 表单id

	private String frmName; // 表单名称

	private String frmType; // 表单类型

	private Date frmDate; // 表单创建日期
	
	private String frmAddr ; // 表单创建地址

	private List<YzNetworkExamFrmAttr> attrs = null;

	public YzNetworkExamInfoFrm() {
		this.attrs = Lists.newArrayList();
	}

	public void addFrmAttr(String key, Object val) {
		this.addFrmAttr(key, val, YzNetWorkFormater.def);
	}

	public void addFrmAttr(String key, Object val, YzNetWorkFormater formater) {
		YzNetworkExamFrmAttr attr = new YzNetworkExamFrmAttr();
		attr.setAttrName(key);
		attr.setAttrVal(formater.formater(val, StringUtil.EMPTY));
		attr.setAttrSeq(this.attrs.size());
		this.attrs.add(attr);
	}

	public List<YzNetworkExamFrmAttr> getFrmAttrs() {
		this.attrs.parallelStream().forEach(v -> {
			v.setFrmId(this.getId());
		});
		return this.attrs;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFrmId() {
		return frmId;
	}

	public void setFrmId(String frmId) {
		this.frmId = frmId == null ? null : frmId.trim();
	}

	public String getFrmName() {
		return frmName;
	}

	public void setFrmName(String frmName) {
		this.frmName = frmName == null ? null : frmName.trim();
	}

	public String getFrmType() {
		return frmType;
	}

	public void setFrmType(String frmType) {
		this.frmType = frmType == null ? null : frmType.trim();
	}

	public Date getFrmDate() {
		return frmDate;
	}

	public void setFrmDate(Date frmDate) {
		this.frmDate = frmDate;
	}
	
	public void setFrmAddr(String frmAddr) {
		this.frmAddr = frmAddr;
	}
	
	
	public String getFrmAddr() {
		return frmAddr;
	}
}
