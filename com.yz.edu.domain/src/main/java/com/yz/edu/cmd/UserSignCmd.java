package com.yz.edu.cmd;

import java.math.RoundingMode;

import com.yz.constants.WechatMsgConstants;
import com.yz.edu.domain.YzUserDomain;
import com.yz.model.WechatMsgVo;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;

public class UserSignCmd extends UserAccountCmd {

	private transient WechatMsgVo vo;

	private String total;
	
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public UserSignCmd() {
		setStep(1);
		setAsyn(true);
		setTopic(YzTaskConstants.YZ_UPDATE_ACCOUNT_TASK);
		setPushMsg(true);
		vo = new WechatMsgVo();
		vo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_SIGN);
	}

	public void warpperPushMsgVo(String openId, String afterAmount) {
		vo.addData("afterAmount", afterAmount);
		vo.setTouser(openId);
		vo.addData("now", DateUtil.getNowDateAndTime());
		vo.addData("amount", this.getAmount().setScale(0, RoundingMode.DOWN).toString());
		vo.addData("total", total);
		vo.setIfUseTemplateUlr(true);
	}

	@Override
	public WechatMsgVo getPushMsgVo() {
		return vo;
	}

	@Override
	public String getMethodName() {
		return "sign";
	}

	@Override
	public Object getId() {
		return getUserId();
	}

	@Override
	public Class<?> getDomainCls() {
		return YzUserDomain.class;
	}
}
