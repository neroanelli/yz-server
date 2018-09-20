package com.yz.edu.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.yz.constants.FeeConstants;
import com.yz.edu.cmd.LearnPaySuccessCmd;
import com.yz.edu.cmd.LearnToPayCmd;
import com.yz.edu.context.DomainContext;
import com.yz.exception.BusinessException;
import com.yz.util.AmountUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @desc 学业订单域
 * @author Administrator
 *
 */
public class YzPayOrderDomain extends YzBaseDomain {

	private String learnId; // 学业Id

	private String stdId; // 学员Id

	private String stdName; // 学员名称

	private String mobile; // 学员联系方式

	private Date createTime; // 创建时间

	private String userId; // 用户ID

	private String grade; // 年级

	private int isCompleted; // 是否已结业

	private int unvsId; // 院校ID

	private String unvsName; // 院校名称

	private int pfsnId; // 专业Id

	private String pfsnName; // 专业名称

	private String pfsnLevel;// 专业层次

	private String recruitType;// 招生类型

	private String scholarship;// 优惠类型

	private String stdStage; // 学员阶段

	private int taId; // 考区Id

	private String taName; // 考区名称

	private String recruit; // 招生老师Id

	private String campusId; // 校区Id

	private String campusName; // 校区名称

	private String financeNo; // 财务编码

	private BigDecimal amount; // 订单总额

	private BigDecimal discount; // 折扣金额

	private BigDecimal payable; // 应缴金额

	private BigDecimal paidAoumt; // 已缴金额

	private List<YzPayOrderItem> feeItems; // 收费明细

	public String getRecruit() {
		return recruit;
	}

	public void setRecruit(String recruit) {
		this.recruit = recruit;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public String getFinanceNo() {
		return financeNo;
	}

	public void setFinanceNo(String financeNo) {
		this.financeNo = financeNo;
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public int getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(int isCompleted) {
		this.isCompleted = isCompleted;
	}

	public int getUnvsId() {
		return unvsId;
	}

	public void setUnvsId(int unvsId) {
		this.unvsId = unvsId;
	}

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}

	public int getPfsnId() {
		return pfsnId;
	}

	public void setPfsnId(int pfsnId) {
		this.pfsnId = pfsnId;
	}

	public String getPfsnName() {
		return pfsnName;
	}

	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	public int getTaId() {
		return taId;
	}

	public void setTaId(int taId) {
		this.taId = taId;
	}

	public String getTaName() {
		return taName;
	}

	public void setTaName(String taName) {
		this.taName = taName;
	}

	public YzPayOrderDomain() {
		setExpire(3600 * 24 * 30 * 6); // 半年的存活期
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}

	public List<YzPayOrderItem> getFeeItems() {
		return feeItems;
	}

	public void setFeeItems(List<YzPayOrderItem> feeItems) {
		this.feeItems = feeItems;
	}

	@Override
	public void setId(Object key) {
		this.learnId = String.valueOf(key);
	}

	@Override
	public Object getId() {
		return this.learnId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getPayable() {
		return payable;
	}

	public void setPayable(BigDecimal payable) {
		this.payable = payable;
	}

	public BigDecimal getPaidAoumt() {
		return paidAoumt;
	}

	public void setPaidAoumt(BigDecimal paidAoumt) {
		this.paidAoumt = paidAoumt;
	}

	/**
	 * 
	 * @desc 去支付
	 * @param cmd
	 * @return
	 */
	public YzPayOrderDomain toPay(LearnToPayCmd cmd) {
		checkItemCode(cmd); // 校验收费代码
		if (cmd.isSuccess()) {
			checkAmount(cmd); // 校验缴费金额
		}
		return this;
	}

	/**
	 * 
	 * @desc 支付成功
	 * @param cmd
	 * @return
	 */
	public YzPayOrderDomain paySuccess(LearnPaySuccessCmd cmd) {
		checkItemCode(cmd); // 校验收费代码
		if (cmd.isSuccess()) {
			BigDecimal amDecimal = cmd.getPayAmount().add(cmd.getCouponDeduction()).add(cmd.getAccDeduction())
					.add(cmd.getZmDeduction());// 实缴金额
			if (cmd.getAmount().compareTo(amDecimal) != 0) {
				logger.error("--------------------------在线支付回调金额与付款金额不匹配，流水:{}", cmd.getPayAmount());
				cmd.setErrorCode("");
				return this;
			}
			shareAmount(cmd);
			payable = payable.subtract(amDecimal); // 扣减应付金额
			paidAoumt = paidAoumt.add(amDecimal); // 增加实付金额
		}
		return this;
	}

	/**
	 * @desc 校验实收金额
	 * @desc 校验缴费金额
	 */
	private void checkAmount(LearnToPayCmd cmd) {
		BigDecimal amount = cmd.getZmDeduction().add(cmd.getAccDeduction()).add(cmd.getCouponDeduction());
		if (cmd.getAmount().compareTo(amount) <= 0) // 应缴金额 大约实缴金额
		{
			payable = payable.subtract(amount); // 扣减应付金额
			paidAoumt = paidAoumt.add(amount); // 增加实付金额
			DomainContext.getInstance().setContextAttr("PAY_STATUS", true); // 滞留金，优惠券，智米将剩余学费缴清
			shareAmount(cmd);
			return;
		}
		cmd.addAmount(cmd.getAllAmount().subtract(amount)); // 计算应付金额
		if (payable.compareTo(amount) < 0) {
			cmd.setErrorCode("E60013");
		}
	}

	/**
	 * 
	 * @desc 支付完成 金额分摊
	 * @param cmd
	 */
	private void shareAmount(LearnToPayCmd cmd) {
		shareZhimiAmount(cmd);
		// 分摊滞留
		shareDelayAmount(cmd);
	}

	/**
	 * @desc 分摊滞留
	 * @param cmd
	 */
	private void shareDelayAmount(LearnToPayCmd cmd) {
		// 分摊滞留
		this.feeItems.parallelStream().sorted((x, y) -> {
			return x.getDelaySeq() >= y.getDelaySeq() ? 1 : -1;
		}).forEach(v -> {
			BigDecimal amount = v.getFeeItem().getDefineAmount().subtract(v.getOfferItem().getDefineAmount())
					.subtract(v.getCoupon()).subtract(v.getZhimi());
			if (amount.compareTo(BigDecimal.ZERO) > 0 && cmd.getAccDeduction().compareTo(BigDecimal.ZERO) > 0) {
				if (cmd.getAccDeduction().compareTo(amount) >= 0) // 如果智米抵扣金额大余额
				{
					v.setDelay(amount);
					cmd.deductingAccDeduction(amount);
				} else {
					v.setDelay(cmd.getZmDeduction());
					cmd.deductingAccDeduction(cmd.getZmDeduction());
				}
			}
			logger.info("shareDelayAmount.itemCode:{},amount:{},couponItem:{}", v.getItemCode(), amount, v.getCoupon());
		});
	}

	/**
	 * @desc 分摊智米
	 * @param cmd
	 */
	private void shareZhimiAmount(LearnToPayCmd cmd) {
		Map<String, BigDecimal> couponItem = cmd.getItemCoupon();
		// 分摊智米
		this.feeItems.parallelStream().sorted().forEach(v -> {
			// 该收费项目应缴金额
			BigDecimal amount = v.getFeeItem().getDefineAmount().subtract(v.getOfferItem().getDefineAmount());
			v.setCoupon(couponItem.get(v.getItemCode())); // 优惠券抵扣
			amount = amount.subtract(v.getCoupon());
			v.setCash(BigDecimal.ZERO); // 现金设置为0
			if (amount.compareTo(BigDecimal.ZERO) > 0 && cmd.getZmDeduction().compareTo(BigDecimal.ZERO) > 0) {
				if (cmd.getZmDeduction().compareTo(amount) >= 0) // 如果智米抵扣金额大余额
				{
					v.setZhimi(amount);
					cmd.deductingZmDeduction(amount);
				} else {
					v.setZhimi(cmd.getZmDeduction());
					cmd.deductingZmDeduction(cmd.getZmDeduction());
				}
			}
			logger.info("shareZhimiAmount.itemCode:{},amount:{},couponItem:{}", v.getItemCode(), amount, v.getCoupon());
		});
	}

	/**
	 * 
	 * @desc 校验缴费代码是否正常
	 * @param cmd
	 */
	private void checkItemCode(LearnToPayCmd cmd) {
		// 校验收费代码是否正常
		for (String itemCode : cmd.getItemCoupon().keySet()) {
			Optional<YzPayOrderItem> orderItem = feeItems.parallelStream().map(v -> {
				cmd.addAllAmount(v.getFeeItem().getDefineAmount().subtract(v.getOfferItem().getDefineAmount()));
				if (StringUtil.equalsIgnoreCase(v.getItemCode(), itemCode)) {
					return v;
				}
				return null;
			}).filter(Objects::nonNull).findFirst();
			if (orderItem != null && orderItem.isPresent()) {
				YzPayOrderItem item = orderItem.get();
				if (item.getPayStatus() == 0) {
					// 收费代码下的应缴金额
					cmd.addAmount(item.getFeeItem().getDefineAmount().subtract(item.getOfferItem().getDefineAmount()));
					if (FeeConstants.FEE_ITEM_TYPE_COACH.equals(item.getItemType())
							&& cmd.getZmDeduction().compareTo(BigDecimal.ZERO) > 0) {
						cmd.setErrorCode("E60025"); // 智米不能抵扣辅导费用
						return;
					}
					continue;
				}
			}
			cmd.setErrorCode("E60039"); // 校验收费代码收交过费
			return;
		}
	}

}
