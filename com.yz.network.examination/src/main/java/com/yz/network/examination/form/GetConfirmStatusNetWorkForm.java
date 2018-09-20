package com.yz.network.examination.form;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.yz.network.examination.process.NetWorkExamFlow.NetWorkExamFlowEnum;
import com.yz.redis.RedisService;

 
/**
 * 
 * @desc 获取网报确认状态
 * @author lingdian
 *
 */
@YzNetWorkForm(
		syncCompensate=false,
		scheduleFrm=true,
		persistenceFrm=false,
		trigger = NetWorkExamFlowEnum.UN_CONFIRM)
public class GetConfirmStatusNetWorkForm extends BaseNetWorkExamForm {
	
	public GetConfirmStatusNetWorkForm(){super();}
	

	public GetConfirmStatusNetWorkForm(String id) {
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
