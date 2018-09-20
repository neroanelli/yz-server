package com.yz.dao.operate;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.operate.BdEnrollLotteryInfo;

public interface BdEnrollLotteryMapper {

	/**
	 * 获取抽奖活动信息
	 * @param lotteryInfo
	 * @return
	 */
	List<BdEnrollLotteryInfo> getEnrollLotteryList(BdEnrollLotteryInfo lotteryInfo);
	
	/**
	 * 获取系统字典中的 优惠报读类型
	 * @return
	 */
	List<Map<String, String>> getScholarshipList();
	
	/**
	 * 增加抽奖
	 * @param lotteryInfo
	 */
	void addLotteryInfo(BdEnrollLotteryInfo lotteryInfo);
	
	/**
	 * 修改抽奖
	 * @param lotteryInfo
	 */
	void updateLotteryInfo(BdEnrollLotteryInfo lotteryInfo);
	
	/**
	 * 获取抽奖信息
	 * @param lotteryId
	 * @return
	 */
	public BdEnrollLotteryInfo getEnrollLotteryInfo(String lotteryId);
	
	/**
	 * 根据状态获取抽奖信息
	 * @param status
	 * @return
	 */
	public List<Map<String, String>> getLotteryInfoByStatus(@Param("status")String status);
	
	/**
	 * 获取特定优惠券信息
	 * @return
	 */
	public List<Map<String, String>> getCouponInfoByCond();
}
