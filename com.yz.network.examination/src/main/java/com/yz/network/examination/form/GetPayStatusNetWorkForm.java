package com.yz.network.examination.form;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.yz.network.examination.process.NetWorkExamFlow.NetWorkExamFlowEnum;
import com.yz.redis.RedisService;

 
/**
 * 
 * @desc 获取当前报读专业的支付状态 
 * @author lingdian
 *
 */
@YzNetWorkForm(
		trigger = NetWorkExamFlowEnum.UN_PAY,
        persistenceFrm= false,
        syncCompensate=true,scheduleFrm=true )
public class GetPayStatusNetWorkForm extends BaseNetWorkExamForm {
	
	public GetPayStatusNetWorkForm(){super();}
	

	public GetPayStatusNetWorkForm(String id) {
		super(id); 
	}

	@Override
	public HttpUriRequest getRequest() {
		HttpGet httpGet = new HttpGet();
		return httpGet;
	}

	@Override
	public boolean isStart() {
		return RedisService.getRedisService().hexists(this.getId(), "networkCookie");
	}

}
