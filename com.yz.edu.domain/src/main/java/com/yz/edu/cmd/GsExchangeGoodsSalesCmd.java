package com.yz.edu.cmd;
import com.yz.edu.domain.JdGoodsSalesDomain;
/**
 * 
 * @desc 京东兑换指令
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class GsExchangeGoodsSalesCmd extends BaseCommand {

	private String salesId;//商品活动ID
	private String skuId;//京东商品ID
	private String jdGoodsType;//京东商品类型
	private String goodsId;//商品ID
	private String salesName;//商品活动名称
	private String goodsName;//商品名称
	private String goodsCount;//活动商品数量
	private String costPrice;//成本价
	private String originalPrice;//原价
	private String salesPrice;//销售价
	private String startTime;//活动结束时间
	private String endTime;//活动起始时间
	private String salesStatus;//活动状态 --- 对应字典：salesStatus 1-下架 2-正常 3-暂停销售
	private String goodsType;//商品类型 --- 对应字典：goodsType 1-普通商品 2-课程商品 3-活动商品
	private String goodsDesc;//商品概述
	private String onceCount;//每次可兑换
	private String goodsImg;//附件URL
	
	public GsExchangeGoodsSalesCmd() {
		setStep(0);
	}
	
	public String getSalesId() {
		return salesId;
	}


	public void setSalesId(String salesId) {
		this.salesId = salesId;
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


	public String getGoodsName() {
		return goodsName;
	}


	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}


	public String getGoodsCount() {
		return goodsCount;
	}


	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}


	public String getCostPrice() {
		return costPrice;
	}


	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}


	public String getOriginalPrice() {
		return originalPrice;
	}


	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}


	public String getSalesPrice() {
		return salesPrice;
	}


	public void setSalesPrice(String salesPrice) {
		this.salesPrice = salesPrice;
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


	public String getGoodsType() {
		return goodsType;
	}


	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}


	public String getGoodsDesc() {
		return goodsDesc;
	}


	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}


	public String getOnceCount() {
		return onceCount;
	}


	public void setOnceCount(String onceCount) {
		this.onceCount = onceCount;
	}

	public String getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}

	@Override
	public Object getId() {
		return salesId;
	}

	@Override
	public String getMethodName() {
		return "updateExchangeGoodsSales";
	}

	@Override
	public Class<?> getDomainCls() {
		return JdGoodsSalesDomain.class;
	}

	
}
