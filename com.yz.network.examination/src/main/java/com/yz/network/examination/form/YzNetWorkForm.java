package com.yz.network.examination.form;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.yz.network.examination.process.NetWorkExamFlow.NetWorkExamFlowEnum;
import com.yz.util.StringUtil;
 
 

/**
 * 
 * @desc YzNetWorkConfig 配置 
 * @author Administrator
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface YzNetWorkForm { 
	  
	public String handler() default StringUtil.EMPTY; // 处理器（将数据回写业务表等操作）
	
	public String interceptor() default StringUtil.EMPTY; // 拦截器（处理http请求范围内）
	
	public String compensateCmd()default StringUtil.EMPTY; // 失败执行的补偿命令
	
	public boolean syncCompensate() default true; // 同步补偿
	
	public int compensateTimes() default 3; // 补偿次数
	
	public String provider() default StringUtil.EMPTY; //表单不存在，进行回溯的提供者 
	
	public boolean persistenceFrm() default true; // 是否持久化表单，存储
	
	public boolean scheduleFrm() default false; // 是否调度表单
	
	public NetWorkExamFlowEnum trigger() default NetWorkExamFlowEnum.BASE; //触发的状态
	
	public NetWorkExamFlowEnum next() default NetWorkExamFlowEnum.BASE ;
}
