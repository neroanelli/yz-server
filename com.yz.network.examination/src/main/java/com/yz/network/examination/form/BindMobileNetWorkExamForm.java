package com.yz.network.examination.form;

import java.nio.charset.Charset;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.process.NetWorkExamFlow.NetWorkExamFlowEnum;

/**
 * 
 * @desc 网报系统-绑定手机号
 * @author zhuliping
 *
 */
@YzNetWorkForm( 
		persistenceFrm =false,
		trigger = NetWorkExamFlowEnum.UN_BIND,
		handler = NetWorkExamConstants.BINDMOBILE_NETWORKEXAMINATION_HANDLER ,
        compensateCmd=NetWorkExamConstants.RETRY_NETWORKEXAMCOMPENSATE)
public class BindMobileNetWorkExamForm extends BaseNetWorkExamForm {

	
	public BindMobileNetWorkExamForm() {
		super();
		this.setNeedLogin(true);
		this.setName(BINDMOBILE_NETWORK_EXAM_NAME); 
		this.addParam("method","kssjbd");
	}
	
	public BindMobileNetWorkExamForm(String id) {
		super(id);
		this.setNeedLogin(true);
		this.setName(BINDMOBILE_NETWORK_EXAM_NAME); 
		this.addParam("method","kssjbd");
	}


	@Override
	public HttpUriRequest getRequest() { 
		HttpPost reg = new HttpPost(BINDMOBILE_NETWORK_EXAM_ACTION);
		reg.setEntity(new UrlEncodedFormEntity(wrapperParm(), Charset.forName("utf-8")));
		return reg;
	} 
	

	@Override
	 // 判断该学员是否可以进行网报 ？
	public boolean isStart() { 
		return true;
	}
}
