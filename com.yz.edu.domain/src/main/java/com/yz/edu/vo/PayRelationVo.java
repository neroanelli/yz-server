package com.yz.edu.vo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps; 
import com.yz.model.NameValuePari;

/**
 * 
 * @desc 支付关联
 * @author Administrator
 *
 */
public class PayRelationVo implements java.io.Serializable {
	
	private String userId ; // 用户Id

	private String payNo; // 支付单号

	private String payType; // 支付类型

	private BigDecimal payAmount; // 支付金额

	private BigDecimal accDeduction; // 现金账户抵扣金额

	private BigDecimal zmDeduction; // 智米抵扣

	private BigDecimal couponDeduction; // 优惠券抵扣

	private List<String> itemCodeList; // 缴费代码列表
	
	private List<String>itemYears;  // 学年列表

	private Map<String, List<NameValuePari>> itemCodeCouponList; // 收费代码 - 优惠券列表
	
	private Map<String,List<NameValuePari>>couponItemCodeList ; // 优惠券ID - 收费代码列表
	 
	public PayRelationVo() {
		this.itemCodeList = Lists.newArrayList();
		this.itemYears = Lists.newArrayList();
		this.itemCodeCouponList = Maps.newHashMap();
	    this.couponItemCodeList = Maps.newHashMap();
	}
	
	public List<String> getItemYears() {
		return itemYears;
	} 
	
	public String[] toItemYears() {
		String[]arr = new String[this.itemYears.size()];
		this.itemYears.toArray(arr);
		return arr;
	} 
	 
	public void addItemYear(String year) {
		this.itemYears.add(year);
	} 
	
	public void addItemYears(List<String> years) {
		this.itemYears.addAll(years);
	} 
	
	
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}
	

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getAccDeduction() {
		return accDeduction;
	}

	public void setAccDeduction(BigDecimal accDeduction) {
		this.accDeduction = accDeduction;
	}

	public BigDecimal getZmDeduction() {
		return zmDeduction;
	}

	public void setZmDeduction(BigDecimal zmDeduction) {
		this.zmDeduction = zmDeduction;
	}

	public BigDecimal getCouponDeduction() {
		return couponDeduction;
	}

	public void setCouponDeduction(BigDecimal couponDeduction) {
		this.couponDeduction = couponDeduction;
	}

	public List<String> getItemCodeList() {
		return itemCodeList;
	}

	public void setItemCodeList(List<String> itemCodeList) {
		this.itemCodeList = itemCodeList;
	}
 
	public void addItemCodeCouponList(String itemCode, NameValuePari item) {
		if (this.itemCodeCouponList.containsKey(itemCode)) {
			this.itemCodeCouponList.get(itemCode).add(item);
			return;
		}
		this.itemCodeCouponList.put(itemCode, Lists.newArrayList(item));
	}

	public Map<String, List<NameValuePari>> getItemCodeCouponList() {
		return itemCodeCouponList;
	}

	public void setItemCodeCouponList(Map<String, List<NameValuePari>> itemCodeCouponList) {
		this.itemCodeCouponList = itemCodeCouponList;
	}

	public void addCouponItemCodeList(String couponId,NameValuePari item) 
	{
		if (this.couponItemCodeList.containsKey(couponId)) {
			this.couponItemCodeList.get(couponId).add(item);
			return;
		}
		this.couponItemCodeList.put(couponId, Lists.newArrayList(item));
	}
	
	public Map<String, List<NameValuePari>> getCouponItemCodeList() {
		return couponItemCodeList;
	} 
}
