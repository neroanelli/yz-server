package com.yz.edu.cmd;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.google.common.collect.Lists; 
import com.yz.edu.domain.YzCouponDomain;
import com.yz.model.NameValuePari;

@SuppressWarnings("serial")
/**
 * @desc 使用优惠券指令
 * @author Administrator
 *
 */
public class UseCouponCmd extends BaseCommand {

	private String couponId; // 优惠券ID

	private List<NameValuePari> itemCodeList;

	public UseCouponCmd() {
		setStep(2);
		this.itemCodeList = Lists.newArrayList();
	}

	/**
	 * @desc  
	 * @param itemCode 缴费代码
	 * @param amount 金额
	 */
	public void addCouponItem(String itemCode, BigDecimal amount) {
		NameValuePari item = new NameValuePari();
		item.setKey(itemCode);
		item.setValue(amount.setScale(0, RoundingMode.DOWN).toString());
		this.itemCodeList.add(item);
	}
	
	public List<NameValuePari> getItemCodeList() {
		return itemCodeList;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	@Override
	public Object getId() {
		return couponId;
	}

	@Override
	public String getMethodName() {
		return "toPay";
	}

	@Override
	public Class<?> getDomainCls() {
		return YzCouponDomain.class;
	}



}
