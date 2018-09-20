package com.yz.network.examination.form;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.process.NetWorkExamFlow.NetWorkExamFlowEnum;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import java.nio.charset.Charset;

/**
 * 
 * @desc link@CHECK_NETWORK_NAME
 * @author zhuliping
 *
 */
@YzNetWorkForm( 
		persistenceFrm =false,
		trigger = NetWorkExamFlowEnum.UN_BIND,
        compensateCmd=NetWorkExamConstants.RETRY_NETWORKEXAMCOMPENSATE,
		interceptor = NetWorkExamConstants.VAILCODE_NETWORKEXAMINATION_INTERCEPTOR)
public class CheckNetWorkExamForm extends BaseNetWorkExamForm {


	public CheckNetWorkExamForm() {
		super();
		this.setName(CHECK_NETWORK_NAME);

	}

	public CheckNetWorkExamForm(String id) {
		super(id);
		this.setName(CHECK_NETWORK_NAME);
	}


	@Override
	public HttpUriRequest getRequest() { 
		HttpPost reg = new HttpPost(CHECK_EXAM_NAME_ACTION);
		reg.setEntity(new UrlEncodedFormEntity(wrapperParm(), Charset.forName("gbk")));
		return reg;
	} 
	

	@Override
	 // 判断该学员是否可以进行网报 ？
	public boolean isStart() { 
		return true;
	}
}
