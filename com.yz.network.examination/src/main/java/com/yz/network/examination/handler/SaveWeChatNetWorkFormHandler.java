package com.yz.network.examination.handler;

 
import java.util.Map;

import org.apache.http.HttpEntity; 
import org.springframework.stereotype.Component;
 
import com.yz.exception.BusinessException;
import com.yz.network.examination.constants.NetWorkExamConstants; 
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.util.JsonUtil; 

@Component(value = NetWorkExamConstants.SAVEWECHAT_NETWORKEXAMINATION_HANDLER)
public class SaveWeChatNetWorkFormHandler implements NetWorkExaminationHandler {

 
	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form, HttpEntity entity) throws BusinessException {
		String result = toStringEntity(entity); 
		Map data = JsonUtil.str2Object(result, Map.class);
	    return form.setValue(new NetWorkExamHandlerResult().setOk(Boolean.parseBoolean(String.valueOf(data.get("success"))))).setResult(result);
	} 
}
