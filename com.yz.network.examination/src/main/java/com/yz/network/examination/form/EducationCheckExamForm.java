package com.yz.network.examination.form;

import java.nio.charset.Charset;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import com.yz.network.examination.constants.NetWorkExamConstants;

@YzNetWorkForm(
        persistenceFrm = false,
		handler = NetWorkExamConstants.EDUCATIONCHECK_NETWORKEXAMINATION_HANDLER,
compensateCmd=NetWorkExamConstants.CHECK_DIPLOMA_COMPENSATE)
public class EducationCheckExamForm  extends BaseNetWorkExamForm {

	public EducationCheckExamForm() {
		super();
	}

	public EducationCheckExamForm(String id) {
		super(id);
		this.setName(EDUCATIONCHECK_NETWORK_EXAM_NAME);
		this.setNeedLogin(true);
//		this.setLoginFrm(true);
	}
	
	@Override
	public HttpUriRequest getRequest() {
		HttpPost educationCheck = new HttpPost(EDUCATIONCHECK_NETWORK_EXAM_ACTION);
		educationCheck.addHeader("Content-Type", "application/x-www-form-urlencoded");
		educationCheck.addHeader("Host","www.ecogd.edu.cn");
		educationCheck.addHeader("Origin","http://www.ecogd.edu.cn");
		educationCheck.addHeader("Referer","http://www.ecogd.edu.cn/cr/cgbm/chsi_xlcx.jsp");
		educationCheck.setEntity(new UrlEncodedFormEntity(wrapperParm(), Charset.forName("gbk")));
		return educationCheck;
	}

	@Override
	public boolean isStart() {
		return true;
	}

}
