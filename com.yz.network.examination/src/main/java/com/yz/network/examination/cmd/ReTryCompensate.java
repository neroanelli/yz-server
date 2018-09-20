package com.yz.network.examination.cmd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.starter.NetWorkExamStarter;

@Component(value = NetWorkExamConstants.RETRY_NETWORKEXAMCOMPENSATE)
/**
 * @desc 重试补偿
 * @author lingdian
 *
 */
public class ReTryCompensate implements NetWorkExamCompensate {
	

	@Autowired
	private NetWorkExamStarter start;

	@Override
	public boolean doCompensate(BaseNetWorkExamForm frm) {
		start.start(frm);
		return frm.getValue().isOk();
	}
}
