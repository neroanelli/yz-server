package com.yz.network.examination.cmd;

 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.network.examination.aware.NetWorkFormAware;
import com.yz.network.examination.form.BaseNetWorkExamForm; 
import com.yz.network.examination.interceptor.CoreNetWorkExamInterceptor; 

/**
 * 
 * 
 * @desc 网报网络请求命令构建器
 * @author lingdian
 *
 */
@Component(value = "yzNetWorkExamCmdBuilder")
public class NetWorkExamCmdBuilder implements NetWorkFormAware {

	private Logger logger = LoggerFactory.getLogger(NetWorkExamCmdBuilder.class);

	@Autowired
	private CoreNetWorkExamInterceptor coreNetWorkExamInterceptor;
	
	/***
	 * @desc 通过form构建command
	 * @param frm
	 * @return
	 */
	public NetWorkExamHystrixCommand build(BaseNetWorkExamForm frm) {
		NetWorkExamHystrixCommand cmd = new NetWorkExamHystrixCommand(frm,coreNetWorkExamInterceptor);
		logger.info("createNetWorkExamHystrixCommand.cmdId:{},cmdName:{}",frm.getId(),frm.getName());
		return cmd;
	}


}
