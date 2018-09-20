package com.yz.network.examination.handler;

import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.network.examination.cmd.ReTryCompensate;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.form.VailCodeNetWorkForm;
import com.yz.redis.RedisService;
import com.yz.util.StringUtil;
import com.yz.yunsu.DiscernBean;

/**
 * @desc 注册码处理
 * @author lingdian
 *
 */
@Component(value = NetWorkExamConstants.VAILCODE_NETWORKEXAMINATION_HANDLER)
public class VailCodeNetWorkExaminationHandler implements NetWorkExaminationHandler {

	@Autowired
	private ReTryCompensate reTryCompensate;
	 

	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form, HttpEntity entity) {
		boolean bol = RedisService.getRedisService().hexistsarr(form.getId(), "networkCookie"); // 如果cookie不存在  
		if (!bol) // 验证码未识别？
		{
			reTryCompensate.doCompensate(form);
			return form.setValue(new NetWorkExamHandlerResult().setOk(false).setResult("获取验证码cookie！！"));
		}
		String code = DiscernBean.getCode(toByte(entity)); // 通过云速接口解析验证码
		if(StringUtil.isBlank(code))
		{
			reTryCompensate.doCompensate(form);
			return form.setValue(new NetWorkExamHandlerResult().setOk(false).setResult("获取验证码失败！！"));
		}
		logger.info("VailCodeNetWorkExaminationHandler.code:{}", code);
		VailCodeNetWorkForm vailCodeNetWorkForm = (VailCodeNetWorkForm) form;
		NetWorkExamHandlerResult result2 = new NetWorkExamHandlerResult();
		result2.setLogFrm(false);
		result2.setResult(code);
		vailCodeNetWorkForm.setValue(result2);
		return result2;
	
	}

}
