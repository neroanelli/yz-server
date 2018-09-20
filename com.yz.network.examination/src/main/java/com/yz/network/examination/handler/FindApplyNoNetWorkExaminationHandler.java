package com.yz.network.examination.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.croe.util.RegexUtil;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.form.FindApplyNoNetWorkForm;
import com.yz.network.examination.form.LoginNetWorkExamForm;
import com.yz.network.examination.model.YzNetworkExamInfoFrm;
import com.yz.network.examination.service.NetWorkExamFrmInfoService;
import com.yz.network.examination.service.RegNetWorkExamFrmService;
import com.yz.redis.RedisService;
import com.yz.util.StringUtil;

/**
 * 找回预报名号
 * 
 * @author lx
 * @date 2018年9月1日 上午11:05:36
 */
@Component(value = NetWorkExamConstants.FINDAPPLYNO_NETWORKEXAMINATION_HANDLER)
public class FindApplyNoNetWorkExaminationHandler implements NetWorkExaminationHandler {

	@Autowired
	private RegNetWorkExamFrmService regFrmService;

	@Autowired
	private NetWorkExamFrmInfoService netWorkExamFrmInfoService;

	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form, HttpEntity entity) throws BusinessException {
		String result = toStringEntity(entity);
		logger.error("regForm.name{}.id:{},result:{}", form.getName(), form.getId(), result);
		if (StringUtil.isNotBlank(result)) {
			NetWorkExamHandlerResult result2 = new NetWorkExamHandlerResult();
			FindApplyNoNetWorkForm applyForm = (FindApplyNoNetWorkForm) form;
			String applyNo = RegexUtil.RegexMatch("报名号：(\\d+)", result);
			logger.info("{}:get.apply.no:{}", form.getId(), applyNo);
			//如果出现查询不到考生信息情况，直接赋值为空
			// 如果有存在预报名号则做以下操作
			if (StringUtil.hasValue(applyNo)) {
				// 解析出返回的预报名号
				result2.setOk(true);
				result2.setResult(result);

				// 如果非第一次注册,则把上次注册的信息改为无效网报信息
				int regSuccessNum = regFrmService.studentSceneRegisterNum(applyForm.getId());
				if (regSuccessNum > 0) { // 改为无效
					regFrmService.updateStudentRegisterStatus(applyForm.getId());
				}
				// 预报名号 和 预报名密码进行落库
				String xqShort = applyForm.getParam("xqshort");
				if (StringUtil.isEmpty(xqShort)) {
					xqShort = "YZJY";
				}
				Map<String, String> regInfo = new HashMap<>();
				regInfo.put("registerId", IDGenerator.generatorId());
				regInfo.put("learnId", applyForm.getId());
				regInfo.put("username", applyNo);
				regInfo.put("password", form.getParam("pwd"));
				regInfo.put("registerNo", xqShort + RedisService.getRedisService().incrBy(xqShort, 1).toString());

				regFrmService.insertStudentSceneRegister(regInfo);

				// 更新学员的网报状态
				regFrmService.updateStudentWebRegisterStatus(applyForm.getId());

				// 更改为注册成功
				regFrmService.updateReportStatus("1", applyForm.getId());

				YzNetworkExamInfoFrm loginFrm = new YzNetworkExamInfoFrm();
				loginFrm.setFrmId(applyForm.getId());
				loginFrm.setFrmName(LoginNetWorkExamForm.class.getSimpleName());
				loginFrm.setFrmType(LoginNetWorkExamForm.class.getSimpleName());
				loginFrm.addFrmAttr("dlfs", "1");
				loginFrm.addFrmAttr("id", applyNo);
				loginFrm.addFrmAttr("pwd", form.getParam("pwd"));
				netWorkExamFrmInfoService.saveNetWorkExamFrm(loginFrm);
				return form.setValue(result2);
			} else {
				result2.setOk(false);
				result2.setResult(result);
			}
		}
		return form.setValue(new NetWorkExamHandlerResult().setOk(false).setResult("获取域报名号异常:result{" + result + "}"));
	}

}
