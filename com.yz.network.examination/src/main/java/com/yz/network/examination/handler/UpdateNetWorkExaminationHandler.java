package com.yz.network.examination.handler;

import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.service.RegNetWorkExamFrmService;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.util.StringUtil;

/**
 * @desc 注册提交返回处理
 * @author lingdian
 *
 */
@Component(value = NetWorkExamConstants.UPDATE_NETWORKEXAMINATION_HANDLER)
public class UpdateNetWorkExaminationHandler implements NetWorkExaminationHandler {

	@Autowired
	private NetWorkExamStarter start;

	@Autowired
	private RegNetWorkExamFrmService regFrmService;

	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form, HttpEntity entity) {
		String result = toStringEntity(entity);
		logger.error("regForm.name{}.id:{},result:{}", form.getName(), form.getId(), result);
		if (StringUtil.isNotBlank(result)) {
			NetWorkExamHandlerResult result2 = new NetWorkExamHandlerResult();
			if (result.contains("window.history")) { // 失败
				String returnResult = StringUtil.substringBefore(result, "');window.history");

				String rrr2 = StringUtil.substringAfter(returnResult, "<script>alert('");
				result2.setOk(false);
				result2.setResult(rrr2);
				form.setValue(result2);

				logger.info("form.id:{},reg.fail,reason:{}", form.getId(), rrr2);

				regFrmService.insertSuccess(form.getId(), form.getParam("username"), "失败", "result");
				return result2;
			} else if (result.contains("您尚未登录！")) {
				result2.setOk(false);
				result2.setCompensateCmd(NetWorkExamConstants.UNLOGIN_NETWORKEXAMCOMPENSATE);
				result2.setCompensateCallBack(r -> {
					start.start(form);
				});
				return result2;
			} else { // 成功
				regFrmService.insertSuccess(form.getId(), form.getParam("username"), "成功", "result");
				result2.setOk(true);
				result2.setResult(result);
				return result2;
				// 跳转获取报名号
				// GetApplyNoNetWorkForm getApplyNoFrm = new
				// GetApplyNoNetWorkForm(form.getId());
				// getApplyNoFrm.addParam("pwd",form.getParam("pwd"));
				// getApplyNoFrm.addParam("xqshort",form.getParam("xqshort"));
				// getApplyNoFrm.addParam("zjdm",form.getParam("zjdm"));
				// getApplyNoFrm.addParam("xm",form.getParam("xm"));
				// start.start(getApplyNoFrm);
				// logger.info("start.get.applyno............{}"+JsonUtil.object2String(getApplyNoFrm));
				// return form.setValue(new
				// NetWorkExamHandlerResult().setOk(true).setResult("next-》网报成功，获取预报名号！"));
			}
		}
		return form.setValue(new NetWorkExamHandlerResult().setOk(false).setResult("result:{" + result + "}"));
	}

}
