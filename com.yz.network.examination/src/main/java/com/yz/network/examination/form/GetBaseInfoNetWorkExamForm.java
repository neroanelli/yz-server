package com.yz.network.examination.form;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.process.NetWorkExamFlow.NetWorkExamFlowEnum;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import java.nio.charset.Charset;

/**
 * 
 * @desc 网报系统-获取报考基本信息
 * @author jiangyt
 *
 */
@YzNetWorkForm( 
		 persistenceFrm = false,
		trigger = NetWorkExamFlowEnum.UN_COLLECTION,
		handler = NetWorkExamConstants.GETBASEINFO_NETWORKEXAMINATION_HANDLER ,
        compensateCmd=NetWorkExamConstants.RETRY_NETWORKEXAMCOMPENSATE)
public class GetBaseInfoNetWorkExamForm extends BaseNetWorkExamForm {


	public GetBaseInfoNetWorkExamForm() {
		super();
		this.setName(BASEINFO_NETWORK_EXAM_NAME);
	}

	public GetBaseInfoNetWorkExamForm(String id) {
		super(id);
		this.setName(BASEINFO_NETWORK_EXAM_NAME);
	}


	@Override
	public HttpUriRequest getRequest() { 
		HttpGet baseinfo = new HttpGet(BASEINFO_NETWORK_EXAM_ACTION);
		return baseinfo;
	} 
	

	@Override
	 // 判断该学员是否可以进行网报 ？
	public boolean isStart() { 
		return true;
	}
}
