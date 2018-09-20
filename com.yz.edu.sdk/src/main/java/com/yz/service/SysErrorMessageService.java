package com.yz.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.yz.report.ReportJdbcDao;
import com.yz.util.StringUtil;

/**
 * 
 * Description: 异常信息业务
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年5月9日.
 *
 */
@Service(value = "yzSysErrorMessageService")
public class SysErrorMessageService {

	@Autowired
	private ReportJdbcDao reportJdbcDao;

	public static String ERROR_MSG_REP = "querySysErrorMessage";

	/**
	 * 获取app异常信息
	 * 
	 * @param errorCode
	 *            错误代码
	 * @return
	 */
	public String getErrorMsg(String errorCode) {
		return this.getError(errorCode, "app_msg");
	}

	/**
	 * 获取Sys异常信息
	 * 
	 * @param errorCode
	 *            错误代码
	 * @return
	 */
	public String getSysErrorMsg(String errorCode) {
		return this.getError(errorCode, "sys_msg");
	}

	private String getError(String errorCode, String msg) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("errorCode", errorCode);
		Map result = this.reportJdbcDao.queryObject(ERROR_MSG_REP, param, Map.class);
		if (result != null && !result.isEmpty()) {
			return StringUtil.obj2String(result.get(msg));
		}
		return StringUtil.EMPTY;
	}

}
