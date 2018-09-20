package com.yz.network.examination.handler;

import org.apache.http.HttpEntity; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm; 
import com.yz.network.examination.starter.NetWorkExamStarter;  
import com.yz.util.StringUtil;

/**
 * @desc 绑定手机号提交返回处理 
 * @author zhuliping
 *
 */
@Component(value = NetWorkExamConstants.GETSMSCODE_NETWORKEXAMINATION_HANDLER)
public class GetSmsCodeNetWorkExaminationHandler implements NetWorkExaminationHandler {

	@Autowired
	private NetWorkExamStarter netWorkExamStarter;

	
	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form ,HttpEntity entity ) 
	{
		String result = toStringEntity(entity);
		logger.info("regForm.name{}.id:{},result:{}", form.getName(), form.getId(), result);
		if(StringUtil.isNotBlank(result)){
			NetWorkExamHandlerResult result2 = new NetWorkExamHandlerResult();
			result2.setResult(result);
			//如果发送验证码失败，做补偿操作
			if(StringUtil.contains(result, "重新登录")) {
				result2.setOk(false);
				result2.setCompensateCmd(NetWorkExamConstants.UNLOGIN_NETWORKEXAMCOMPENSATE);
				result2.setCompensateCallBack( r->{ netWorkExamStarter.start(form);});
				return form.setValue(result2);	 
			}
			result2.setOk(true); 
			form.setValue(result2);
			return result2;
		}		
		return form.setValue(new NetWorkExamHandlerResult().setOk(false).setResult("获取域报名号异常:result{" + result +"}"));
	}

}
