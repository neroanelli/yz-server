package com.yz.network.examination.form;

import java.nio.charset.Charset;

import org.apache.http.client.entity.UrlEncodedFormEntity; 
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import com.yz.network.examination.constants.NetWorkExamConstants; 

 
/**
 * 
 * @desc 网报获取短信验证码 
 * @author lingdian
 *
 */
@YzNetWorkForm(
		persistenceFrm= false,syncCompensate=true,
		handler = NetWorkExamConstants.GETSMSCODE_NETWORKEXAMINATION_HANDLER,
		compensateCmd = NetWorkExamConstants.RETRY_NETWORKEXAMCOMPENSATE)
public class GetSmsCodeNetWorkForm extends BaseNetWorkExamForm {
	
	public GetSmsCodeNetWorkForm(){super();this.setLogFrm(false);this.addParam("method","getSjyzm");}
	

	public GetSmsCodeNetWorkForm(String id) {
		super(id); 
		this.setLogFrm(false);
		this.addParam("method","getSjyzm");
	}

	@Override
	public HttpUriRequest getRequest() {
		HttpPost sendSmsHttp = new HttpPost(GETSMSCODE_NETWORK_EXAM_ACTION);
		sendSmsHttp.setEntity(new UrlEncodedFormEntity(wrapperParm(), Charset.forName("utf-8")));
		return sendSmsHttp;
	}

	@Override
	public boolean isStart() {
		return true;
	}
 
}
