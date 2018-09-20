package com.yz.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.yz.report.ReportJdbcDao;
import com.yz.util.StringUtil;

@Service(value = "yzSysDictService")
public class SysDictService {

	@Autowired
	private ReportJdbcDao reportJdbcDao;

	public static String SYS_DICT_REP = "querySysDict";

	public String getDictName(String dictValue) {
		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("dictValue", dictValue);
		Map dict = this.reportJdbcDao.queryObject(SYS_DICT_REP, paramMap, Map.class);
		if (dict != null && !dict.isEmpty()) {
			return String.valueOf(dict.get(dictValue));
		}
		return StringUtil.EMPTY;
	}
	
	@SuppressWarnings("rawtypes")
	public List getCityInfoByProvinceCode(String repCode,String provinceCode){
		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("provinceCode", provinceCode);
		return (List)reportJdbcDao.getRepResultList(repCode, paramMap, Map.class);
	}
	
}
