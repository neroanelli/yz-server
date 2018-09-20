package com.yz.network.examination.form;

import java.nio.charset.Charset;

import org.apache.http.client.entity.UrlEncodedFormEntity; 
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import com.yz.network.examination.constants.NetWorkExamConstants; 

/**
 * 
 * @author Administrator
 *
 */

@YzNetWorkForm(  
		persistenceFrm =false,
		handler = NetWorkExamConstants.ONLINEPAY_NETWORKEXAMINATION_HANDLER )
public class GdOnlinePayForm extends BaseNetWorkExamForm {
	
	private String frmAction ;
	
	
	public void setFrmAction(String frmAction) {
		this.frmAction = frmAction;
	}
	
	public String getFrmAction() {
		return frmAction;
	}


	@Override
	public HttpUriRequest getRequest() {
		HttpPost onlinePay = new HttpPost(this.frmAction);
		onlinePay.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		onlinePay.setEntity(new UrlEncodedFormEntity(wrapperParm(), Charset.forName("utf-8")));
		return onlinePay;
	}

	@Override
	public boolean isStart() { 
		return true;
	}

}
