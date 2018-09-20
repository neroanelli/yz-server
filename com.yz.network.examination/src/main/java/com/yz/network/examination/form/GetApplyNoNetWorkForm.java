package com.yz.network.examination.form;

import java.nio.charset.Charset;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.handler.NetWorkExaminationHandler.NetWorkExamHandlerResult;
import com.yz.redis.RedisService;

/**
 * 获取预报名号
 * @author lx
 * @date 2018年8月31日 下午5:52:08
 */
@YzNetWorkForm(  persistenceFrm = false,handler = NetWorkExamConstants.GETAPPLYNO_NETWORKEXAMINATION_HANDLER )
public class GetApplyNoNetWorkForm extends BaseNetWorkExamForm{

	public GetApplyNoNetWorkForm() {
		super();
		this.setName(GETAPPLYNO_NETWORK_NAME);
		this.setNeedLogin(true);
	}

	public GetApplyNoNetWorkForm(String id) {
		super(id);
		this.setName(GETAPPLYNO_NETWORK_NAME);
		this.setNeedLogin(true);
		//this.setLoginFrm(true);
	}
	
	@Override
	public HttpUriRequest getRequest() {
		HttpPost getApplyNoHttp = new HttpPost(GETAPPLYNO_NETWORK_ACTION);
		getApplyNoHttp.setEntity(new UrlEncodedFormEntity(wrapperParm(), Charset.forName("utf-8")));
		return getApplyNoHttp;
	}

	@Override
	public boolean isStart() {
		return true;
	}

}
