package com.yz.dao.operate;

import java.util.List;

import com.yz.model.operate.BdLotteryPrizeInfo;

public interface BdLotteryPrizeMapper {

	/**
	 * 获取奖品信息
	 * @param prizeInfo
	 * @return
	 */
	public List<BdLotteryPrizeInfo> getLotteryPrizeList(BdLotteryPrizeInfo prizeInfo);
	
	/**
	 * 添加奖品
	 */
	public void addLotteryPrizeInfo(BdLotteryPrizeInfo prizeInfo);
	
	/**
	 * 修改奖品
	 */
	public void updateLotteryPrizeInfo(BdLotteryPrizeInfo prizeInfo);
	
	/**
	 * 获取抽奖详细
	 * @param prizeId
	 * @return
	 */
	public BdLotteryPrizeInfo getLotteryPrizeInfo(String prizeId);
}
