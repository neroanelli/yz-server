package com.yz.network.examination.handler;

import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm; 

@Component(value = NetWorkExamConstants.GETPAY_NETWORKEXAMINATION_HANDLER)
/**
 * @desc 针对于登录请求解析考试院返回结果
 * @author lingdian 
 *
 */
public class GetPayStatusNetWorkExaminationHandler implements NetWorkExaminationHandler{

	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form,HttpEntity entity) 
	{
		String result = toStringEntity(entity);
		logger.info("{}-{}.loginNameResult:{}",form.getName() ,form.getId(),result);
		NetWorkExamHandlerResult result2 = new NetWorkExamHandlerResult();
		result2.setOk(true);
		result2.setResult(result);
		form.setValue(result2);
		return result2;
	} 
}
