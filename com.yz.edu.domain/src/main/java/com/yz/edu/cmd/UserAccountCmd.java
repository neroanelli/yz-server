package com.yz.edu.cmd;

import java.math.BigDecimal;

import com.yz.constants.FinanceConstants;
import com.yz.edu.domain.YzUserDomain;
import com.yz.task.YzTaskConstants;

public class UserAccountCmd extends BaseCommand {

	private String userId; // 用户Id

	private BigDecimal amount; // 兑换需要的智米

	private String type = FinanceConstants.ACC_ACTION_OUT; // 1 进账 2出账

	private String accountType = FinanceConstants.ACC_TYPE_ZHIMI; // 账户类型
	
	private boolean canCheckAccount=true;//是否校验账户够不够
	
	private String remark; // 备注

	private String mappingId;// mappingId ?

	public UserAccountCmd() {
		setStep(1);
		setAsyn(true);
		setTopic(YzTaskConstants.YZ_UPDATE_ACCOUNT_TASK);
	}

	public boolean isCanCheckAccount() {
		return canCheckAccount;
	}



	public void setCanCheckAccount(boolean canCheckAccount) {
		this.canCheckAccount = canCheckAccount;
	}



	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMappingId() {
		return mappingId;
	}

	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}

	@Override
	public Object getId() {
		return userId;
	}

	@Override
	public String getMethodName() {
		return "editZhimi";
	}

	@Override
	public Class<?> getDomainCls() {
		return YzUserDomain.class;
	}
}
