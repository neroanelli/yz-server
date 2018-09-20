package com.yz.report.handler;

import java.util.List;
import java.util.Map;

/**
 * @desc 定义报表工具类的处理工具类 
 * @author lingdian
 * @date 2017年3月7日
 */
public interface ReportResultHandler {

	/**
	 * 
	 * @param param 原始的传入参数
	 * @param result 上次查询/处理结果 
	 * @return
	 */
	public Object execute(Map<String,Object>param,List<Map<String,Object>> result);
}
