package com.yz.edu.cmd;

import com.yz.edu.context.DomainContext;
import com.yz.edu.domain.YzUserDomain;
import com.yz.edu.domain.hook.YzDomainExecuteHook;
import com.yz.task.YzTaskConstants;
import com.yz.util.CodeUtil;

@SuppressWarnings("serial")
public class UserExchangeCmd extends UserAccountCmd {

	private int exchangeCount; // 兑换数量

	public UserExchangeCmd() {
		setStep(1);
		setAsyn(true);
		setTopic(YzTaskConstants.YZ_UPDATE_ACCOUNT_TASK);
		addDomainExecHooks(new YzDomainExecuteHook() {
			public void postExecute(com.yz.edu.domain.YzBaseDomain domain, BaseCommand cmd) {
				YzUserDomain user = (YzUserDomain) domain;
				DomainContext.getInstance().setContextAttr("userName", user.getUserName());
				DomainContext.getInstance().setContextAttr("userMobile", user.getMobile());
				DomainContext.getInstance().setContextAttr("userHeadImg", user.getHeadImg());
			};
		});
	}

	public int getExchangeCount() {
		return exchangeCount;
	}

	public void setExchangeCount(int exchangeCount) {
		this.exchangeCount = exchangeCount;
	}

	@Override
	public String getMethodName() {
		return "exchange";
	}

	@Override
	public Class<?> getDomainCls() {
		return YzUserDomain.class;
	}

	@Override
	public Object getId() {
		return super.getId();
	}

}
