package com.yz.network.examination.starter;

import org.slf4j.LoggerFactory;

import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets; 
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.http.NetWorkExaminationHttpInvoker;

/**
 * 
 * @desc 网报启动器
 * @author lingdian
 *
 */
@Component
public class NetWorkExamStarter {

	private static Logger logger = LoggerFactory.getLogger(NetWorkExamStarter.class);

	@Autowired
	private NetWorkExaminationHttpInvoker httpInvoker;

	/**
	 * @desc 批量多个网报请求
	 * @step 1 网报
	 * @param forms
	 */
	public void start(Set<BaseNetWorkExamForm> forms) {
		httpInvoker.startNetWorkExamFrm(forms);
	}

	/**
	 * @desc 单个网报请求
	 * @step 1 网报
	 * @param form
	 */
	public void start(BaseNetWorkExamForm form) {
		this.start(Sets.newHashSet(form));
	}
}
