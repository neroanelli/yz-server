package com.yz.job.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface StudentSceneConfirmMapper {

	void reSetSceneConfirm(@Param("confirmId") String confirmId,@Param("learnId") String learnId);
	
	/**
	 * 得到当天预约的学员
	 * @param type 0 上午预约 1下午预约
	 * @return
	 */
	List<Map<String,String>>  getCurrentDateStudentSceneConfirm(@Param("type") Integer type);

}
