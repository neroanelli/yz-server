package com.yz.edu.domain;

import java.math.BigDecimal;

import com.yz.constants.FinanceConstants;
import com.yz.constants.JDConstants;
import com.yz.edu.cmd.UserAccountCmd;
import com.yz.edu.cmd.UserExchangeCmd;
import com.yz.edu.cmd.UserPaySuccessCmd;
import com.yz.edu.cmd.UserSignCmd;
import com.yz.edu.cmd.UserToPayCmd;
import com.yz.edu.context.DomainContext;
import com.yz.exception.BusinessException;

/**
 * 
 * @desc yz 用户域
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class YzUserDomain extends YzBaseDomain {

	private String userId; // 用户Id

	private String headImg; // 用户图像

	private String userName; // 用户昵称

	private String realName; // 用户姓名

	private String yzCode; // 远智编码

	private BigDecimal zhimi; // 智米账号

	private BigDecimal retention; // 滞留金账号

	private BigDecimal cash; // 现金账号

	private String parentId; // 推荐人Id

	private String mobile; // 联系方式

	private int relation = 0;// 类型 4 学员 2员工 （多重身份用|计算）

	private String stdId; // 学员Id

	private String empId; // 员工Id

	private String status; // 用户状态

	private String openId; // 微信openId

	public YzUserDomain() {
		setExpire(3600 * 24 * 30); // 设置用户过期时间为30
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getYzCode() {
		return yzCode;
	}

	public void setYzCode(String yzCode) {
		this.yzCode = yzCode;
	}

	public BigDecimal getZhimi() {
		return zhimi;
	}

	public void setZhimi(BigDecimal zhimi) {
		this.zhimi = zhimi;
	}

	public BigDecimal getRetention() {
		return retention;
	}

	public void setRetention(BigDecimal retention) {
		this.retention = retention;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentId() {
		return parentId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	/**
	 * 智米账户操作
	 * 
	 * @param cmd
	 * @return
	 * @throws BusinessException
	 */
	public YzUserDomain editZhimi(UserAccountCmd cmd) throws BusinessException {
		if (zhimi == null) {
			zhimi = new BigDecimal("0.00");
		}
		logger.info("beforZhiMi:{}type:{}", zhimi, cmd.getType());
		if (cmd.getType().equals(FinanceConstants.ACC_ACTION_OUT)) {// 出账
			if (cmd.isCanCheckAccount()) {//是否校验账户金额够不够
				if (cmd.getAmount().compareTo(zhimi) > 0) {
					cmd.setErrorCode("E200022");
					return this;
				}
			}
			zhimi = zhimi.subtract(cmd.getAmount());
		} else {
			zhimi = zhimi.add(cmd.getAmount());// 增加智米
		}
		logger.info("afterZhiMi:{}", zhimi);
		return this;
	}

	/**
	 * @desc 签到赠送智米
	 * @param cmd
	 * @return
	 * @throws BusinessException
	 */
	public YzUserDomain sign(UserSignCmd cmd) {
		// 判断是否操作的智米
		if (cmd.getAccountType().equals(FinanceConstants.ACC_TYPE_ZHIMI)) {
			// 增加智米
			zhimi = zhimi.add(cmd.getAmount());// 增加智米
			String afterAmount = zhimi.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			cmd.warpperPushMsgVo(openId, afterAmount);
		}
		return this;
	}

	/**
	 * @desc 京东兑换
	 * @param cmd
	 * @return
	 */
	public YzUserDomain exchange(UserExchangeCmd cmd) {
		BigDecimal salesPrice = new BigDecimal(
				String.valueOf(DomainContext.getInstance().getContextAttr("salesPrice", 0)));
		BigDecimal amount = salesPrice.multiply(new BigDecimal(cmd.getExchangeCount())); // 兑换所需的智米数量
		if (amount.compareTo(zhimi) > 0) {
			cmd.setErrorCode("E200022");
			return this;
		}
		cmd.setRemark(String.format(JDConstants.JD_EXCHANGE_REMARK,
				String.valueOf(DomainContext.getInstance().getContextAttr("salesName"))));
		cmd.setAmount(amount); // 设置本次兑换所需要的智米
		zhimi = zhimi.subtract(amount);
		return this;
	}

	/**
	 * 
	 * @desc 去支付
	 * @param cmd
	 * @return
	 */
	public YzUserDomain toPay(UserToPayCmd cmd) {
		// 校验滞留账号
		if (retention.compareTo(cmd.getAccDeduction()) < 0) {
			cmd.setErrorCode("E60013");
			return this;
		}
		// 校验智米账号
		if (zhimi.compareTo(cmd.getZmDeduction()) < 0) {
			cmd.setErrorCode("E60013");
			return this;
		}
		// 员工类型 不能使用智米
		if ((relation & 2) > 0 && zhimi.compareTo(cmd.getZmDeduction()) > 0) {
			cmd.setErrorCode("E60026");
			return this;
		}
		boolean bol = (Boolean) DomainContext.getInstance().getContextAttr("PAY_STATUS", false);
		if (bol) {
			this.retention = this.retention.subtract(cmd.getAccDeduction());
			this.zhimi = this.zhimi.subtract(cmd.getZmDeduction());
		}
		return this;
	}

	/**
	 * 
	 * @desc 支付成功
	 * @param cmd
	 * @return
	 */
	public YzUserDomain paySuccess(UserPaySuccessCmd cmd) {
		// 校验滞留账号
		if (retention.compareTo(cmd.getAccDeduction()) < 0) {
			cmd.setErrorCode("E60013");
			return this;
		}
		// 校验智米账号
		if (zhimi.compareTo(cmd.getZmDeduction()) < 0) {
			cmd.setErrorCode("E60013");
			return this;
		}
		// 员工类型 不能使用智米
		if ((relation & 2) > 0 && zhimi.compareTo(cmd.getZmDeduction()) > 0) {
			cmd.setErrorCode("E60026");
			return this;
		}
		this.retention = this.retention.subtract(cmd.getAccDeduction());
		this.zhimi = this.zhimi.subtract(cmd.getZmDeduction());
		return this;
	}

	@Override
	public void setId(Object key) {
		this.userId = String.valueOf(key);
	}

	@Override
	public Object getId() {
		return this.userId;
	}

}
