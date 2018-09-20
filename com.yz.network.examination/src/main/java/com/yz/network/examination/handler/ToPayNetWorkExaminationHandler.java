package com.yz.network.examination.handler;
 

import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.croe.util.RegexUtil;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.util.StringUtil;

import scala.collection.mutable.StringBuilder;

/**
 * @desc 注册提交返回处理
 * @author lingdian
 *
 */
@Component(value = NetWorkExamConstants.TOPAY_NETWORKEXAMINATION_HANDLER)
public class ToPayNetWorkExaminationHandler implements NetWorkExaminationHandler {
	
	@Autowired
	private NetWorkExamStarter netWorkExamStarter;

	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form, HttpEntity entity) {
		String result = toStringEntity(entity);
		NetWorkExamHandlerResult frmResult = new NetWorkExamHandlerResult();
		logger.info("frm.id:{},name:{},result:{}",form.getId(),form.getName(),result);
		if(StringUtil.contains(result, "您尚未登录！"))
		{
			frmResult.setCompensateCmd(NetWorkExamConstants.UNLOGIN_NETWORKEXAMCOMPENSATE);
			frmResult.setOk(false);
			frmResult.setCompensateCallBack(r->{netWorkExamStarter.start(form);});
		}
		else
		{ 
			/*if(StringUtil.contains(result, "该考生已交费，请不要重复交费")) {
				frmResult.setResult("<script>alert('该考生已交费，请不要重复交费');window.history.go(-1);</script>");
			}
			else if(StringUtil.contains(result, "您尚未采集相片，无法缴费")) {
				frmResult.setResult("<script>alert('您尚未采集相片，无法缴费！');window.history.go(-1);</script>");
			}
			else*/ 
			if(StringUtil.contains(result, "<form name=\"epay\""))
			{
				String toPay = new StringBuilder("<form")
						.append(StringUtil.substringAfter(StringUtil.substringBefore(result, "</form>"),"<form"))
						.append("</form><script>document.epay.submit();</script>").toString();
				frmResult.setResult(StringUtil.replace(toPay, "_blank", "_self"));
				frmResult.setOk(true);
			}
			else if(StringUtil.contains(result, "parent.location = '../cgbm/ybcg.jsp'")){
				String alert = RegexUtil.RegexMatch("alert\\('(\\S+)\\'\\);",result);
				frmResult.setResult("<script>alert('"+alert+"');location.href='http://exam.yzou.cn/stdNrs/';</script>");
			}else 
			{
				frmResult.setResult("<script>alert('该考生网报状态异常，无法缴费！');location.href='http://exam.yzou.cn/stdNrs/';</script>");
			}
		} 
		logger.info("form.name{}.id:{},result:{}", form.getName(), form.getId(), result);
		return form.setValue(frmResult);
	}
	
	

}
