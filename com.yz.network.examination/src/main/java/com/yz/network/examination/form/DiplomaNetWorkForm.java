package com.yz.network.examination.form;

 
import org.apache.http.client.methods.HttpGet; 
import org.apache.http.client.methods.HttpUriRequest;

import com.yz.network.examination.constants.NetWorkExamConstants;

@SuppressWarnings("serial")
@YzNetWorkForm(handler = NetWorkExamConstants.DIPLOMA_NETWORKEXAMINATION_HANDLER,persistenceFrm = false)
/**
 * 
 * @desc 学历验证表单
 * @author lingdian
 *
 */
public class DiplomaNetWorkForm extends BaseNetWorkExamForm {

	public DiplomaNetWorkForm() {
		setNeedLogin(true);
		setName(NETWORK_EXAM_DIPLOMA_NAME);
	}

	public DiplomaNetWorkForm(String id) {
		super(id);  
		setNeedLogin(true);
		setName(NETWORK_EXAM_DIPLOMA_NAME);
	}

	@Override
	public HttpUriRequest getRequest() {
		HttpGet checkDiploma = new HttpGet(NETWORK_EXAM_DIPLOMA_ACTION);
		checkDiploma.addHeader("Referer","http://www.ecogd.edu.cn/cr/cgbm/ybcg.jsp");
		checkDiploma.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0");
		return checkDiploma;
	}

	@Override
	public boolean isStart() {
		return true;
	}

}
