package com.yz.network.examination.form;
 

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
 
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.handler.NetWorkExaminationHandler.NetWorkExamHandlerResult;
import com.yz.network.examination.process.NetWorkExamFlow.NetWorkExamFlowEnum;
import com.yz.redis.RedisService;

/**
 * @desc 去支付网络表单  
 *     @step 1  根据身份证号码获取域报名号以及密码进行模拟登录 
 * @author lingdian
 *
 */
@YzNetWorkForm(
		trigger = NetWorkExamFlowEnum.UN_PAY,
		persistenceFrm= false,syncCompensate=true,
		handler = NetWorkExamConstants.TOPAY_NETWORKEXAMINATION_HANDLER)
public class ToPayNetWorkExamForm extends BaseNetWorkExamForm{
	 
	
	public ToPayNetWorkExamForm() {
		setNeedLogin(true);
		setName(TOPAY_NETWORK_EXAM_NAME);
	}
	
	public ToPayNetWorkExamForm(String id ) {
		super(id);
		setNeedLogin(true);
		setName(TOPAY_NETWORK_EXAM_NAME);
	}

	@Override
	public HttpUriRequest getRequest() {
		HttpGet get = new HttpGet(TOPAY_NETWORK_EXAM_ACTION); 
		return get;
	}

	@Override
	public boolean isStart() {
		boolean bol =  RedisService.getRedisService().hexists(this.getId(), "networkCookie");
		if(!bol) // 暂未设置学业ID登录表单
		{
			NetWorkExamHandlerResult result = new NetWorkExamHandlerResult();
			result.setCompensate(true);
			result.setCompensateCmd(NetWorkExamConstants.UNLOGIN_NETWORKEXAMCOMPENSATE);
			this.setValue(result);
			return false;
		}
		return true;
		
	}

}
