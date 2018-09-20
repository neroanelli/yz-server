package com.yz.network.examination.form;

import java.nio.charset.Charset;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import com.yz.network.examination.constants.NetWorkExamConstants;
/**
 * 找回预报名号
 * @author lx
 * @date 2018年9月1日 上午11:01:46
 */
@YzNetWorkForm( persistenceFrm = false, handler = NetWorkExamConstants.FINDAPPLYNO_NETWORKEXAMINATION_HANDLER )
public class FindApplyNoNetWorkForm extends BaseNetWorkExamForm{

	public FindApplyNoNetWorkForm() {
		super();
	}
	
	public FindApplyNoNetWorkForm(String id){
		super(id);
		setName(FINDAPPLYNO_NETWORK_NAME);
	}
	
	@Override
	public HttpUriRequest getRequest() {
		HttpPost findApplyNoHttp = new HttpPost(FINDAPPLYNO_NETWORK_ACTION);
		findApplyNoHttp.setEntity(new UrlEncodedFormEntity(wrapperParm(), Charset.forName("gbk")));
		return findApplyNoHttp;
	}

	@Override
	public boolean isStart() {
		return true;
	}

}
