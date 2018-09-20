package com.yz.edu.domain.hook;

import com.yz.edu.cmd.BaseCommand;
import com.yz.edu.domain.YzBaseDomain;

/**
 * 
 * @desc domain 执行的后置钩子函数 自定义收集一些相关参数
 * @author Administrator
 *
 */
public interface YzDomainExecuteHook {

	/**
	 * 
	 * @param domain
	 *            执行Command前domain实例
	 * @param cmd
	 */
	default public void preExecute(YzBaseDomain domain, BaseCommand cmd) {

	}

	/**
	 * 
	 * @param domain
	 *            执行Command的domain实例
	 * @param cmd
	 */
	default public void postExecute(YzBaseDomain domain, BaseCommand cmd) {

	}
}
