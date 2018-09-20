package com.yz.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Maps;
import com.yz.constants.SystemParamConstants;
import com.yz.report.ReportJdbcDao;

@Service(value = "yzSysParameterService")
public class SysParameterService {

	@Autowired
	private ReportJdbcDao reportJdbcDao;

	private static String SYS_PARAMETER_REP = "querySysParameter";
	
	private static String SYS_ALL_PARAMETER_REP = "queryAllSysParameter";

	/**
	 * @desc 获取bms参数
	 * @param paramName
	 * @return
	 */
	public String getString(String paramName) {
		return this.getString(SystemParamConstants.BMS_BELONG_PARAM, paramName);
	}

	/**
	 * 
	 * @param paramName
	 * @return
	 */
	public String getString(String sysBelong, String paramName) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("sysBelog", sysBelong);
		param.put("paramName", paramName);
		String sysParameter = reportJdbcDao.queryObject(SYS_PARAMETER_REP, param, String.class);
		return sysParameter;
	}

	/**
	 * 
	 * @param paramName
	 * @return
	 */
	public int getInt(String sysBelong, String paramName) {
		return NumberUtils.toInt(getString(sysBelong, paramName));
	}
	
	/**
	 * 获取所有的系统参数
	 * @return
	 */
	public List sellectAll(){
		return reportJdbcDao.queryList(SYS_ALL_PARAMETER_REP, null, Map.class);
	}

}
