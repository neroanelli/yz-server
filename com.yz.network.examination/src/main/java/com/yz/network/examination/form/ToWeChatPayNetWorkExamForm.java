package com.yz.network.examination.form;

import com.yz.network.examination.constants.NetWorkExamConstants; 

@YzNetWorkForm( 
		persistenceFrm= false,syncCompensate=true,
		handler = NetWorkExamConstants.WECHAT_PAY_NETWORKEXAMINATION_HANDLER)
public class ToWeChatPayNetWorkExamForm extends ToPayNetWorkExamForm{
	
	public ToWeChatPayNetWorkExamForm() {
		setNeedLogin(true);
		setName(TOPAY_NETWORK_EXAM_NAME);
	}
	
	public ToWeChatPayNetWorkExamForm(String id ) {
		super(id);
		setNeedLogin(true);
		setName(TOPAY_NETWORK_EXAM_NAME);
	}
}
