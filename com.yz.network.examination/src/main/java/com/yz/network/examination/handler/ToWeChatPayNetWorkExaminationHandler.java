package com.yz.network.examination.handler;
 

import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.sql.dialect.mysql.ast.MysqlForeignKey.On;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.croe.util.RegexUtil;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.form.GdOnlinePayForm;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.util.StringUtil;

import joptsimple.util.RegexMatcher;
import scala.collection.mutable.StringBuilder;

/**
 * @desc 注册提交返回处理
 * @author lingdian
 *
 */
@Component(value = NetWorkExamConstants.WECHAT_PAY_NETWORKEXAMINATION_HANDLER)
public class ToWeChatPayNetWorkExaminationHandler implements NetWorkExaminationHandler {
	
	@Autowired
	private NetWorkExamStarter netWorkExamStarter;

	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form, HttpEntity entity) {
		String result = toStringEntity(entity);
		NetWorkExamHandlerResult frmResult = new NetWorkExamHandlerResult();
		if(StringUtil.contains(result, "您尚未登录！"))
		{
			frmResult.setCompensateCmd(NetWorkExamConstants.UNLOGIN_NETWORKEXAMCOMPENSATE);
			frmResult.setOk(false);
			frmResult.setCompensateCallBack(r->{netWorkExamStarter.start(form);});
		}
		else
		{ 
			if(StringUtil.contains("result", "该考生已交费，请不要重复交费")) {
				frmResult.setResult(" 该考生已交费，请不要重复交费");
				frmResult.setOk(false); 
			}else {
				String toPay = new StringBuilder("<form")
						.append(StringUtil.substringAfter(StringUtil.substringBefore(result, "</form>"),"<form"))
						.append("</form>").toString();
				String action = RegexUtil.RegexMatch("action=\"(\\S+)\"",toPay);
				GdOnlinePayForm onlinePayForm = new GdOnlinePayForm();
				onlinePayForm.setFrmAction(action);
				onlinePayForm.addParam("siteCode", RegexUtil.RegexMatch("<input type=hidden name=\"siteCode\" value=\"(\\S+)\"/>",toPay));
				onlinePayForm.addParam("version", RegexUtil.RegexMatch("<input type=hidden name=\"version\" value=\"(\\S+)\"/>",toPay));
				onlinePayForm.addParam("deviceType", RegexUtil.RegexMatch("<input type=hidden name=\"deviceType\" value=\"(\\S+)\"/>",toPay));
				onlinePayForm.addParam("transacCode", RegexUtil.RegexMatch("<input type=hidden name=\"transacCode\" value=\"(\\S+)\"/>",toPay));
				onlinePayForm.addParam("signature", RegexUtil.RegexMatch("<input type=hidden name=\"signature\" value=\"(\\S+)\">",toPay));
				onlinePayForm.addParam("reqdata", RegexUtil.RegexMatch("<input type=hidden name=\"reqdata\" value=\"(\\S+)\">",toPay));
				onlinePayForm.addParam("charset", RegexUtil.RegexMatch("<input type=hidden name=\"charset\" value=\"(\\S+)\">",toPay));
				netWorkExamStarter.start(onlinePayForm);
				frmResult.setOk(true); 
				return form.setValue(onlinePayForm.getValue());
			}
		} 
		logger.info("form.name{}.id:{},result:{}", form.getName(), form.getId(), result);
		return form.setValue(frmResult);
	}

}
