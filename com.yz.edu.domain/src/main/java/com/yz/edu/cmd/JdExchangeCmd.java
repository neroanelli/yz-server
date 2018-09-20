package com.yz.edu.cmd;

import java.util.Map;

import com.google.common.collect.Maps;
import com.yz.edu.domain.JdGoodsSalesDomain;
import com.yz.task.YzTaskConstants;

/**
 * 
 * @desc 京东兑换指令
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class JdExchangeCmd extends BaseCommand {

	private String salesId; // 兑换Id

	private String thirdOrder;// 兑换内部订单号

	private String jdAccessToken; // 京东api token

	private String jdEntityCardToken; // 京东实体卡下单 token

	private String name; // 收件人名称

	private String userId;// 兑换人Id 用于存储兑换记录
	
	private String userMobile;// 用户注册手机号码
	
	private String userName ; // 用户昵称
	
	private String headImgUrl; // 用户头像 

	private String provinceCode; // 一级地址

	private String cityCode; // 二级地址

	private String districtCode; // 三级地址

	private String streetCode; // 街道地址
	
	private String provinceName;
	
	private String cityName;
	
	private String districtName;
	
	private String streetName;

	private String address; // 收货人地址

	private String mobile; // 收货人电话

	private String email; // 邮箱地址

	private int exchangeCount; // 兑换数量

	private String submitState; // 京东提交所需参数

	private String regCode;// 京东提交所需参数

	private String invoicePhone;// 京东提交所需参数 发票人电话

	private String invoiceType;// 京东提交所需参数 发票类型 
	
	private Map<String, Object> extendAttr = Maps.newHashMap();

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public JdExchangeCmd() {
		setStep(2);
		setAsyn(true);
		setTopic(YzTaskConstants.JD_EXCHANGE_ORDER_TASK);
	}
	
	public Map<String, Object> getExtendAttr() {
		return extendAttr;
	}

	public void addExtendAttrs(Map<String, Object> attr) {
		extendAttr.putAll(attr);
	}

	public void addExtendAttr(String attrName, Object val) {
		extendAttr.put(attrName, val);
	}

	public String getThirdOrder() {
		return thirdOrder;
	}

	public void setThirdOrder(String thirdOrder) {
		this.thirdOrder = thirdOrder;
	}

	public String getSubmitState() {
		return submitState;
	}

	public void setSubmitState(String submitState) {
		this.submitState = submitState;
	}

	public String getRegCode() {
		return regCode;
	}

	public void setRegCode(String regCode) {
		this.regCode = regCode;
	}

	public String getInvoicePhone() {
		return invoicePhone;
	}

	public void setInvoicePhone(String invoicePhone) {
		this.invoicePhone = invoicePhone;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getJdAccessToken() {
		return jdAccessToken;
	}

	public void setJdAccessToken(String jdAccessToken) {
		this.jdAccessToken = jdAccessToken;
	}

	public String getJdEntityCardToken() {
		return jdEntityCardToken;
	}

	public void setJdEntityCardToken(String jdEntityCardToken) {
		this.jdEntityCardToken = jdEntityCardToken;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getStreetCode() {
		return streetCode;
	}

	public void setStreetCode(String streetCode) {
		this.streetCode = streetCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public Object getId() {
		return salesId;
	}

	@Override
	public String getMethodName() {
		return "exchange";
	}

	@Override
	public Class<?> getDomainCls() {
		return JdGoodsSalesDomain.class;
	}

	public String getSalesId() {
		return salesId;
	}

	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}

	public int getExchangeCount() {
		return exchangeCount;
	}

	public void setExchangeCount(int exchangeCount) {
		this.exchangeCount = exchangeCount;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	} 

}
