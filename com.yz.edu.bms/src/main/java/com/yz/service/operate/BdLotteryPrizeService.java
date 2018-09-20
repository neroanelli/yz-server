package com.yz.service.operate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.operate.BdLotteryPrizeMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.operate.BdLotteryPrizeInfo;

@Service
@Transactional
public class BdLotteryPrizeService {
	
	@Autowired
	private BdLotteryPrizeMapper lotteryPrizeMapper;

	/**
	 * 抽奖奖品列表
	 * @param page
	 * @param pageSize
	 * @param info
	 * @return
	 */
	public IPageInfo<BdLotteryPrizeInfo> getLotteryPrizeList(int page,int pageSize,BdLotteryPrizeInfo info) {
		PageHelper.offsetPage(page, pageSize);
		List<BdLotteryPrizeInfo> lotteryPrizeList = lotteryPrizeMapper.getLotteryPrizeList(info);
		return new IPageInfo<>((Page<BdLotteryPrizeInfo>)lotteryPrizeList);
	}
	/**
	 * 添加奖品
	 * @param prizeInfo
	 */
	public void add(BdLotteryPrizeInfo prizeInfo){
		BaseUser user = SessionUtil.getUser();
		
		prizeInfo.setPrizeId(IDGenerator.generatorId());
		prizeInfo.setUpdateUser(user.getUserName());
		prizeInfo.setUpdateUserId(user.getUserId());
		lotteryPrizeMapper.addLotteryPrizeInfo(prizeInfo);
	}
	/**
	 * 修改奖品
	 * @param prizeInfo
	 */
	public void update(BdLotteryPrizeInfo prizeInfo){
		BaseUser user = SessionUtil.getUser();
		prizeInfo.setUpdateUser(user.getUserName());
		prizeInfo.setUpdateUserId(user.getUserId());
		lotteryPrizeMapper.updateLotteryPrizeInfo(prizeInfo);
	}
	/**
	 * 获取奖品详细
	 * @param prizeId
	 * @return
	 */
	public BdLotteryPrizeInfo getLotteryPrizeInfo(String prizeId){
		return lotteryPrizeMapper.getLotteryPrizeInfo(prizeId);
	}
}
