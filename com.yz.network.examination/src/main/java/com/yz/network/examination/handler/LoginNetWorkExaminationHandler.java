package com.yz.network.examination.handler;

import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.dao.RegNetWorkExamFrmMapper;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.util.StringUtil; 

@Component(value = NetWorkExamConstants.LOGIN_NETWORKEXAMINATION_HANDLER)
/**
 * @desc 针对于登录请求解析考试院返回结果
 * @author lingdian 
 *
 */
public class LoginNetWorkExaminationHandler implements NetWorkExaminationHandler{
	@Autowired
	private RegNetWorkExamFrmMapper regmapper;
	
	
	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form,HttpEntity entity) 
	{
		String result = toStringEntity(entity);
		logger.info("{}-{}.loginNameResult:{}",form.getName() ,form.getId(),result);
		NetWorkExamHandlerResult result2 = new NetWorkExamHandlerResult(); 
		result2.setOk(StringUtil.equalsIgnoreCase(result, "success")||StringUtil.equalsIgnoreCase(result, "confirm"));
		result2.setResult(result);
		form.setValue(result2);
		if(StringUtil.equalsIgnoreCase(result, "confirm")) {
			//报名确认成功：已现场确认状态的反写状态
			regmapper.updateStudentSceneConfirmStatus(form.getId());
		}
		
		
		return result2;
	} 
}
