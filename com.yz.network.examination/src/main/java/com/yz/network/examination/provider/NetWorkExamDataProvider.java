package com.yz.network.examination.provider;

import com.yz.network.examination.form.BaseNetWorkExamForm;

/**
 * 
 * @desc 网报参数提供 
 * @author lingdian
 *
 */
public interface NetWorkExamDataProvider<T> {

	/***
	 * @desc 获取参数值 
	 * @param key 参数key值
	 * @return
	 */
	public T provider(BaseNetWorkExamForm frm);
}
