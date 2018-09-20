package com.yz.network.examination.handler;

import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.exception.BusinessException;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.service.EducationCheckExamFrmService;
import com.yz.network.examination.starter.NetWorkExamStarter;

@Component(value = NetWorkExamConstants.EDUCATIONCHECK_NETWORKEXAMINATION_HANDLER)
public class EducationCheckExaminationHandler implements NetWorkExaminationHandler {

	@Autowired
	private EducationCheckExamFrmService service;

	@Autowired
	private NetWorkExamStarter netWorkExamStarter;

	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form, HttpEntity entity) throws BusinessException {
		String result = toStringEntity(entity);
		logger.info("{}-{}.educationCheckResult:{}", form.getName(), form.getId(), result);
		NetWorkExamHandlerResult result2 = new NetWorkExamHandlerResult();
		String resultString = "验证结果:";
		// 验证之后判断是否验证通过，不通过就保存相应的数据到数据库方便查询
		// 0-不合格/1-合格/2-通过（应届生）/3-往届待验/4-无需验证
		String status = "5";
		if (result.contains("验证专科学历通过") || result.contains("专科学历已验证完毕，请不要重复验证!")) {
			status = "1";
			result2.setOk(true);
			resultString = "验证专科学历通过/专科学历已验证完毕，请不要重复验证!";
		} else if (result.contains("验证专科学历失败，请核查姓名、证件号码、毕业证书编号是否有误!")) {
			result2.setOk(false);
			status = "0";
			// result2.setCompensateCmd(NetWorkExamConstants.CHECK_DIPLOMA_COMPENSATE);
			resultString = "验证专科学历失败，请核查姓名、证件号码、毕业证书编号是否有误!";
			result2.setResult(resultString);
			service.educationCheck(form.getId(), form.getParam("ybmh"), status, resultString);
			form.setValue(result2);
			return result2;

		} else if (result.contains("您尚未登录")) {
			// status = "0";
			result2.setOk(false);
			resultString = "登录失败";
			result2.setResult(resultString);
			result2.setResult(resultString);
			result2.setCompensateCmd(NetWorkExamConstants.UNLOGIN_NETWORKEXAMCOMPENSATE);
			result2.setCompensateCallBack(r -> {
				netWorkExamStarter.start(form);
			});
			form.setValue(result2);
			return result2;
		} else if (result.contains("网上验证只能验证专升本的往届生")) {
			status = "4";
			result2.setOk(false);
			resultString = "网上验证只能验证专升本的往届生";
			service.educationCheck(form.getId(), form.getParam("ybmh"), status, resultString);
		} else if (result.contains("204")) {// 身份证或姓名有误，或者是老学历
			status = "6";
			result2.setOk(false);
			resultString = "验证失败（204）:没有找到对应的学籍,请核对姓名和证件号码是否有误!";
			service.educationCheck(form.getId(), form.getParam("ybmh"), status, resultString);
		} else {
			service.educationCheck(form.getId(), form.getParam("ybmh"), status, result);
			result2.setOk(false);
		}
		result2.setResult(resultString);
		form.setValue(result2);
		service.educationCheck(form.getId(), status);
		return result2;
	}
}
