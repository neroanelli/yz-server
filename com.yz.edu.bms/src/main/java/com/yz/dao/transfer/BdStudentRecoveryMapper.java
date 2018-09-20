package com.yz.dao.transfer;

import org.apache.ibatis.annotations.Param;

import com.yz.model.recruit.BdLearnInfo;

public interface BdStudentRecoveryMapper {

	/**
	 * 获取已支付的订单数量
	 * 
	 * @param learnId
	 * @return
	 */
	int selectSubOrderForLearnId(@Param("learnId") String learnId);

	/**
	 * 根据learn_id获取学业数据
	 */
	BdLearnInfo selectBdLearnInfoForLearnId(@Param("learnId") String learnId);
}
