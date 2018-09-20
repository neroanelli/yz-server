package com.yz.edu.constants;

public interface YzDomainConstants {

	public static final int COMMON_DOMAIN_VERSION = 1; // 定义domain的数据版本
	
	public static final int DOMAIN_OPERATION_UPDATE = 0 ;// cmd 标记为update操作
	
	public static final int DOMAIN_OPERATION_ADD =1 ;// cmd 标记为add操作
	
	public static final int COMMAND_STATUS_SUCCESS = 0 ; // 命令执行成功
	
 	public static final int COMMAND_STATUS_FAILED = 1 ; // 命令执行失败
 	
	public static final String DOMAIN_EVNET_SOURCE_TOPIC = "com.yz.domain.event.source" ; // eventSource topic 
	
}
