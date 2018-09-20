package com.yz.network.examination.cmd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.form.LoginNetWorkExamForm;
import com.yz.network.examination.provider.FrmNetWorkExamDataProvider;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.redis.RedisService;  

/**
 * 
 * @desc 未登录补偿
 * @author lingdian
 *
 */
@Component(value = NetWorkExamConstants.UNLOGIN_NETWORKEXAMCOMPENSATE)
public class UnLoginCompensate implements NetWorkExamCompensate {

	@Autowired
	private NetWorkExamStarter netWorkExamStarter;
	
	@Autowired
	private FrmNetWorkExamDataProvider frmNetWorkExamDataProvider;

	@Override
	public boolean doCompensate(BaseNetWorkExamForm frm) {
		String id = frm.getId(); // 获取当前表单id
		LoginNetWorkExamForm loginFrm = RedisService.getRedisService().hgetarr(id, LoginNetWorkExamForm.class.getSimpleName(),LoginNetWorkExamForm.class);
		if(loginFrm==null) 
		{
			loginFrm = new LoginNetWorkExamForm(id);  
			loginFrm.setFrmCode(NetWorkExamConstants.FRM_COMMON_REPCODE);
			loginFrm = (LoginNetWorkExamForm)frmNetWorkExamDataProvider.provider(loginFrm);
		}
		loginFrm.setFrmNo(frm.getFrmNo());
		loginFrm.addValidCode();
		netWorkExamStarter.start(loginFrm);
		return loginFrm.getValue().isOk();
	}
}
