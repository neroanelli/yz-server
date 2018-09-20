package com.yz.network.examination.handler;

import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.croe.util.RegexUtil;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.form.GetEducationNetWorkForm;
import com.yz.network.examination.service.EducationCheckExamFrmService;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.util.StringUtil;

/**
 * @desc 获取报考基本信息返回处理
 * @author jiangyt
 *
 */
@Component(value = NetWorkExamConstants.GETEDUCATION_NETWORKEXAMINATION_HANDLER)
public class GetEducationNetWorkExaminationHandler implements NetWorkExaminationHandler {

	@Autowired
	private EducationCheckExamFrmService educationService;

	@Autowired
	private NetWorkExamStarter netWorkExamStarter;

	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form, HttpEntity entity) {
		String result = toStringEntity(entity);
		logger.info("GetEducationNetWorkForm.name{}.id:{},result:{}", form.getName(), form.getId(), result);
		if (StringUtil.isNotBlank(result) && !result.contains("您尚未登录")) {
			GetEducationNetWorkForm bindForm = (GetEducationNetWorkForm) form;
			String printHtml = getPrintHtml(result);
			String collectResult = RegexUtil.RegexMatch("学籍检查状态：</td>\\W+<td.*?>(.*?)</td>", printHtml);
			if(StringUtil.isBlank(collectResult))
				collectResult = RegexUtil.RegexMatch("证书检查状态：</td>\\W+<td.*?>(.*?)</td>", printHtml);
			NetWorkExamHandlerResult result2 = new NetWorkExamHandlerResult();
			// if (!collectResult.contains("未验证")) {
			educationService.educationGet(bindForm.getId(), "没有", collectResult, collectResult);
			logger.error("GetEducationNetWorkForm.name{}.id:{},result:{}", form.getName(), form.getId(), collectResult);
			// }
			result2.setOk(true);
			result2.setResult(collectResult);
			bindForm.setValue(result2);
			return result2;
		} else {
			if (StringUtil.contains(result, "您尚未登录")) {
				NetWorkExamHandlerResult result2 = new NetWorkExamHandlerResult();
				result2.setOk(false);
				result2.setCompensateCmd(NetWorkExamConstants.UNLOGIN_NETWORKEXAMCOMPENSATE);
				result2.setCompensateCallBack(r -> {
					netWorkExamStarter.start(form);
				});
				return form.setValue(result2);
			}
		}
		return form.setValue(new NetWorkExamHandlerResult().setOk(false).setResult("获取域报名号异常:result{" + result + "}"));
	}

	/**
	 * 获取打印html
	 * 
	 * @param result
	 * @return
	 */
	public String getPrintHtml(String result) {
		String html = StringUtil.EMPTY;
		try {
			String sprnstr = "<!--startprint-->";
			String eprnstr = "<!--endprint-->";

			html = result.substring(result.indexOf(sprnstr) + 17);
			html = html.substring(0, html.indexOf(eprnstr));
		} catch (Exception ex) {
			return null;
		}
		return html;
	}

}
