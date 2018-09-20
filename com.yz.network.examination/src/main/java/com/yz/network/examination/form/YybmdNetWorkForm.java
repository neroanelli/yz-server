package com.yz.network.examination.form;

import java.nio.charset.Charset;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.handler.NetWorkExaminationHandler.NetWorkExamHandlerResult;
import com.yz.redis.RedisService;

/**
 * 
 * 预约报名确认点
 * @author lx
 * @date 2018年8月27日 下午5:26:51
 */
@YzNetWorkForm( handler = NetWorkExamConstants.YYBMD_NETWORKEXAMINATION_HANDLER )
public class YybmdNetWorkForm extends BaseNetWorkExamForm{

	public YybmdNetWorkForm() {
		super();
	}

	public YybmdNetWorkForm(String id) {
		super(id);
		this.setName(YYBMD_NETWORK_EXAM_NAME);
		this.setNeedLogin(true);
		this.setLoginFrm(true);
	}
	
	@Override
	public HttpUriRequest getRequest() {
		HttpPost yybmdHttp = new HttpPost(YYBMD_NETWORK_EXAM_NAME_ACTION);
		yybmdHttp.setEntity(new UrlEncodedFormEntity(wrapperParm(), Charset.forName("utf-8")));
		return yybmdHttp;
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
