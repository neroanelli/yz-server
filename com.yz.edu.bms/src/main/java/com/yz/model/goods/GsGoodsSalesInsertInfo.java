package com.yz.model.goods;

import java.io.Serializable;

import com.yz.model.common.PubInfo;

public class GsGoodsSalesInsertInfo extends PubInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4375455598681410893L;
	private String salesId;
	private String goodsId;
	private String skuId;
	private String jdGoodsType;//京东商品类型
	private String salesName;
	private String salesType;
	private String salesPrice;
	private String showAfterOver;
	private String salesRules;
	private String startTime;
	private String endTime;
	private String salesStatus;
	private String isPlan;
	private String planId;
	private String planCount;
	
	private String interval;
	
	private String onceCount;
	
	private String salesCount;
	
	private String showSeq;
	
	public String getOnceCount() {
		return onceCount;
	}
	public void setOnceCount(String onceCount) {
		this.onceCount = onceCount;
	}
	public String getSalesId() {
		return salesId;
	}
	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getSalesName() {
		return salesName;
	}
	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}
	public String getSalesType() {
		return salesType;
	}
	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}
	public String getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(String salesPrice) {
		this.salesPrice = salesPrice;
	}
	public String getShowAfterOver() {
		return showAfterOver;
	}
	public void setShowAfterOver(String showAfterOver) {
		this.showAfterOver = showAfterOver;
	}
	public String getSalesRules() {
		return salesRules;
	}
	public void setSalesRules(String salesRules) {
		this.salesRules = salesRules;
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
	public String getSalesStatus() {
		return salesStatus;
	}
	public void setSalesStatus(String salesStatus) {
		this.salesStatus = salesStatus;
	}
	public String getIsPlan() {
		return isPlan;
	}
	public void setIsPlan(String isPlan) {
		this.isPlan = isPlan;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getPlanCount() {
		return planCount;
	}
	public void setPlanCount(String planCount) {
		this.planCount = planCount;
	}
	public String getInterval() {
		return interval;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
	public String getSalesCount()
	{
		return salesCount;
	}
	public void setSalesCount(String salesCount)
	{
		this.salesCount = salesCount;
	}
	public String getShowSeq()
	{
		return showSeq;
	}
	public void setShowSeq(String showSeq)
	{
		this.showSeq = showSeq;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getJdGoodsType() {
		return jdGoodsType;
	}
	public void setJdGoodsType(String jdGoodsType) {
		this.jdGoodsType = jdGoodsType;
	}
	
}
