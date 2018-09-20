package com.yz.network.examination.handler;

import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm; 


/**
 * 预约报名确认点
 * @author lx
 * @date 2018年8月27日 下午5:33:51
 */
@Component(value = NetWorkExamConstants.YYBMD_NETWORKEXAMINATION_HANDLER)
public class YybmdNetWorkExaminationHandler implements NetWorkExaminationHandler{

	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form,HttpEntity entity) 
	{
		String result = toStringEntity(entity);
		logger.info("{}-{}.yybmdNameResult:{}",form.getName() ,form.getId(),result);
		NetWorkExamHandlerResult result2 = new NetWorkExamHandlerResult();
		result2.setOk(true);
		result2.setResult(result);
		form.setValue(result2);
		return result2;
	} 
}
