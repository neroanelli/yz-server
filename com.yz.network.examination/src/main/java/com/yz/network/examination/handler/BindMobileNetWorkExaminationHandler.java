package com.yz.network.examination.handler;

import java.util.Map;
import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.network.examination.cmd.UnLoginCompensate;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.form.BindMobileNetWorkExamForm;
import com.yz.network.examination.form.LoginNetWorkExamForm;
import com.yz.network.examination.handler.NetWorkExaminationHandler.NetWorkExamHandlerResult;
import com.yz.network.examination.service.RegNetWorkExamFrmService;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * @desc 绑定手机号提交返回处理 
 * @author zhuliping
 *
 */
@Component(value = NetWorkExamConstants.BINDMOBILE_NETWORKEXAMINATION_HANDLER)
public class BindMobileNetWorkExaminationHandler implements NetWorkExaminationHandler {

	@Autowired
	private RegNetWorkExamFrmService regFrmService;
	
	@Autowired
	private NetWorkExamStarter netWorkExamStarter;
	
	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form ,HttpEntity entity ) 
	{
		
		String result = toStringEntity(entity);
		logger.info("regForm.name{}.id:{},result:{}", form.getName(), form.getId(), result);
		if(StringUtil.isNotBlank(result)){
			BindMobileNetWorkExamForm bindForm = (BindMobileNetWorkExamForm)form;
			NetWorkExamHandlerResult result2 = new NetWorkExamHandlerResult();
			Map<String, String> data = JsonUtil.str2Object(result, Map.class);
			result2.setOk(true);
			result2.setResult(result);
			form.setValue(result2);
			if(StringUtil.equalsIgnoreCase(StringUtil.obj2String(data.get("type")),"1")) {
				//更新手机绑定状态
				regFrmService.updateMobileBindStatus("1",bindForm.getId());
				//更新网报步骤
				regFrmService.updateNetWorkStatus("2", bindForm.getId());
			}
			if(StringUtil.contains(result, "重新登录")) {
				result2.setOk(false);
				result2.setCompensateCmd(NetWorkExamConstants.UNLOGIN_NETWORKEXAMCOMPENSATE);
				result2.setCompensateCallBack( r->{ netWorkExamStarter.start(form);});
				return form.setValue(result2);	 
			}
			
			return result2;
		}
		return form.setValue(new NetWorkExamHandlerResult().setOk(false).setResult("绑定手机号:result{" + result +"}"));
		
	}

}
