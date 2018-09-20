package com.yz.network.examination.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.form.VailCodeNetWorkForm;
import com.yz.network.examination.handler.NetWorkExaminationHandler.NetWorkExamHandlerResult;
import com.yz.network.examination.starter.NetWorkExamStarter;

/**
 * 
 * @desc 获取验证码参数
 * @author lingdian
 *
 */
@Component(value= "validCodeNetWorkExamProvider")
public class ValidCodeNetWorkExamProvider implements NetWorkExamDataProvider<String>{
	
	@Autowired
	private NetWorkExamStarter start ; 

	@Override
	public String provider(BaseNetWorkExamForm frm) { 
		VailCodeNetWorkForm vailCodeNetWorkForm = new VailCodeNetWorkForm(frm.getId());
		vailCodeNetWorkForm.setId(frm.getId());
		vailCodeNetWorkForm.setFrmNo(frm.getFrmNo());
		start.start(vailCodeNetWorkForm);
		NetWorkExamHandlerResult result = vailCodeNetWorkForm.getValue();
		return String.valueOf(result.getResult());
	}
}
