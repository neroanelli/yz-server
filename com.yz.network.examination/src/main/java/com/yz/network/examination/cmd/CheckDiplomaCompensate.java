package com.yz.network.examination.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.form.DiplomaNetWorkForm;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.util.JsonUtil; 


/**
 * @desc 学历验证补偿命令
 * @author lingdian
 *
 */
@Component(value = NetWorkExamConstants.CHECK_DIPLOMA_COMPENSATE)
public class CheckDiplomaCompensate implements NetWorkExamCompensate  {

	private static Logger logger = LoggerFactory.getLogger(CheckDiplomaCompensate.class);
	
	@Autowired
	private NetWorkExamStarter netWorkExamStarter;
	
	@Override
	public boolean doCompensate(BaseNetWorkExamForm frm) {
		DiplomaNetWorkForm diplomaNetWorkForm = new DiplomaNetWorkForm(frm.getId());
		netWorkExamStarter.start(diplomaNetWorkForm);
		logger.info("doCompensate.frmInfo：{}，result:{}",JsonUtil.object2String(diplomaNetWorkForm),frm.getId());
		return diplomaNetWorkForm.getValue().isOk();
	}

}
