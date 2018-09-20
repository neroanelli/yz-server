package com.yz.edu.cmd;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.yz.edu.domain.YzPayOrderDomain;

/**
 * 
 * @desc 学业缴费
 * @author lingdian
 *
 */
@SuppressWarnings("serial")
public class LearnToPayCmd extends BaseCommand {

	private String learnId; // 学业Id

	private BigDecimal allAmount = BigDecimal.ZERO;

	private BigDecimal amount = BigDecimal.ZERO; // 应缴金额

	private Map<String, BigDecimal> itemCoupon; // 收费代码- 优惠券金额

	private BigDecimal zmDeduction = BigDecimal.ZERO; // 智米抵扣金额

	private BigDecimal accDeduction = BigDecimal.ZERO; // 滞留金抵扣金额

	private BigDecimal couponDeduction = BigDecimal.ZERO; // 优惠券抵扣金额

	public LearnToPayCmd() {
		setStep(0);
		this.itemCoupon = Maps.newHashMap();
	}

	public void addAllAmount(BigDecimal amount) {
		this.allAmount = this.allAmount.add(amount);
	}

	public BigDecimal getAllAmount() {
		return allAmount;
	}

	public void addAmount(BigDecimal amount) {
		this.amount = this.amount.add(amount);
	}

	public BigDecimal getAmount() {
		return amount;
	}

	private void addCouponAmount(BigDecimal couponDeduction) {
		this.couponDeduction = this.couponDeduction.add(couponDeduction);
	}

	public void setCouponDeduction(BigDecimal couponDeduction) {
		this.couponDeduction = couponDeduction;
	}

	public BigDecimal getCouponDeduction() {
		return couponDeduction;
	}

	public void addItemCode(String itemCode) {
		this.addItemCode(itemCode, BigDecimal.ZERO);
	}

	public void addItemCode(String itemCode, BigDecimal amount) {
		this.addCouponAmount(amount);
		if (this.itemCoupon.containsKey(itemCode)) {
			BigDecimal sum = this.itemCoupon.get(itemCode).add(amount);
			this.itemCoupon.put(itemCode, sum);
			return;
		}
		this.itemCoupon.put(itemCode, amount);
	}

	public void addItemCodes(List<String> itemCodes) {
		if (itemCodes != null && !itemCodes.isEmpty()) {
			itemCodes.parallelStream().forEach(v -> {
				if (!itemCoupon.containsKey(v)) {
					addItemCode(v);
				} 
			});
		}
	}

	public Map<String, BigDecimal> getItemCoupon() {
		return itemCoupon;
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public BigDecimal getZmDeduction() {
		synchronized (this) {
			return zmDeduction;
		}
	}

	public void setZmDeduction(BigDecimal zmDeduction) {
		this.zmDeduction = zmDeduction;
	}

	public void deductingZmDeduction(BigDecimal amount) {
		synchronized (this) {
			if (zmDeduction.compareTo(amount) >= 0) {
				zmDeduction = zmDeduction.subtract(amount);
			}
		}
	}

	public BigDecimal getAccDeduction() {
		synchronized (this) {
			return accDeduction;
		}
	}

	/**
	 * 
	 * @param amount
	 */
	public void deductingAccDeduction(BigDecimal amount) {
		synchronized (this) {
			if (accDeduction.compareTo(amount) >= 0) {
				accDeduction = accDeduction.subtract(amount);
			}
		}
	}

	public void setAccDeduction(BigDecimal accDeduction) {
		this.accDeduction = accDeduction;
	}

	/**
	 * @desc 域聚合根实体对象
	 */
	public Object getId() {
		return this.learnId;
	}

	/**
	 * @desc 制定域内的具体方法
	 */
	public String getMethodName() {
		return "toPay";
	}

	/**
	 * @desc 指定域的类型
	 */
	public Class<?> getDomainCls() {
		return YzPayOrderDomain.class;
	}
}
