package com.yz.network.examination.form;


import com.yz.network.examination.constants.NetWorkExamConstants;

@YzNetWorkForm(  
		persistenceFrm =false,
		handler = NetWorkExamConstants.SAVEWECHAT_NETWORKEXAMINATION_HANDLER )
public class SaveWeChatPaymentForm  extends GdOnlinePayForm { }
