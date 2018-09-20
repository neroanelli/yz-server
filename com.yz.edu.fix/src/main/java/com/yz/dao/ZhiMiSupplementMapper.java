package com.yz.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ZhiMiSupplementMapper {
  
	/**
	 * 查询要补送的智米的数据
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<HashMap<String, String>> selectZhiCountForDateBetween(@Param("startTime") String startTime,@Param("endTime") String endTime);
	/**
	 * 2018-07-11~2018-08-17京东兑换智米扣除
	 * @return
	 */
	List<HashMap<String, String>> selectexchangeDeductionZM();
}
