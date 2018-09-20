package com.yz.network.examination.form;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.yz.network.examination.constants.NetWorkExamConstants;

@SuppressWarnings("serial")
@YzNetWorkForm(handler = NetWorkExamConstants.GETEDUCATION_NETWORKEXAMINATION_HANDLER, persistenceFrm = false)
/**
 * 
 * @desc 学历验证表单
 * @author lingdian
 *
 */
public class GetEducationNetWorkForm extends BaseNetWorkExamForm {

	public GetEducationNetWorkForm() {
		super();
		this.setNeedLogin(true);
		this.setName(BASEINFO_NETWORK_EXAM_NAME);
	}

	public GetEducationNetWorkForm(String id) {
		super(id);
		this.setNeedLogin(true);
		this.setName(BASEINFO_NETWORK_EXAM_NAME);
	}

	@Override
	public HttpUriRequest getRequest() {
		HttpGet baseinfo = new HttpGet(CONFIRMINFO_NETWORK_EXAM_ACTION);
		return baseinfo;
	}

	@Override
	// 判断该学员是否可以进行网报 ？
	public boolean isStart() {
		return true;
	}
}
