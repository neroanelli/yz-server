package com.yz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.BdsLearnInfoApi;
import com.yz.constants.GlobalConstants;
import com.yz.core.constants.AppConstants;
import com.yz.core.util.LotteryUtil;
import com.yz.dao.GsLotteryMapper;
import com.yz.edu.paging.bean.PageInfo;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.GsLottery;
import com.yz.model.GsLotteryWinning;
import com.yz.model.GsPrize;
import com.yz.model.GsUserPrize;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class GsLotteryService {

	private static final Logger log = LoggerFactory.getLogger(GsLotteryService.class);

	@Reference(version = "1.0")
	private BdsLearnInfoApi learnApi;

	@Autowired
	private GsLotteryMapper lotteryMapper;

	public Object getAllWinningInfo(Header head, Body body) {
		int page = body.getInt(GlobalConstants.PAGE_NUM, 0);
		int pageSize = body.getInt(GlobalConstants.PAGE_SIZE, 10);
		String scholarship = body.getString("scholarship");
		// 查询活动
		Map<String, String> lotteryMap = lotteryMapper.selectLotteryInfoByScholarship(scholarship);
		if (null == lotteryMap) {
			return null;
		}
		PageHelper.startPage(page, pageSize);
		List<Map<String, String>> list = lotteryMapper.selectAllWinningInfo(lotteryMap.get("lotteryId"));
		for (Map<String, String> map : list) {
			String mobile = map.get("mobile");
			if (StringUtil.hasValue(mobile)) {
				mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
			}
			map.put("mobile", mobile);

		}
		PageInfo<Map<String, String>> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 某个人的某个抽奖活动
	 * @param lotteryCode
	 * @param userId
	 * @return
	 */
	public Object getLotteryInfo(String scholarship, String userId) {
		Map<String, Object> result = new HashMap<String, Object>();

		// 查询活动
		Map<String, String> lotteryMap = lotteryMapper.selectLotteryInfoByScholarship(scholarship);

		if (null == lotteryMap) {
			return null;
		}

		Integer ticketCount = lotteryMapper.selectTicketCount(userId, lotteryMap.get("lotteryId"),"0");
		result.put("lotteryId", lotteryMap.get("lotteryId"));
		result.put("ticketCount", ticketCount);

		String ticketStatus = null;
		String learnId = null;

		if (ticketCount <= 0) {
			Map<String, String> statusInfo = learnApi.getLearnStatus(userId);
			ticketStatus = statusInfo.get("ticketStatus");
			learnId = statusInfo.get("learnId");
		}

		// 抽奖状态 1-去报读 2-去缴费 3-去邀约
		result.put("ticketStatus", ticketStatus);
		result.put("learnId", learnId);

		// 中奖信息轮循
		List<Map<String, String>> winners = lotteryMapper.selectWinningInfo(userId, lotteryMap.get("lotteryId"));

		result.put("winners", winners);

		return result;
	}

	/**
	 * 抽奖
	 * 
	 * @param userId
	 * @param lotteryId
	 * @return
	 */
	public Object lottery(String userId, String lotteryId) {

		// 判断活动是否结束
		GsLottery lottery = lotteryMapper.selectLotteryByLotteryId(lotteryId);

		if (null == lottery) {
			throw new BusinessException("E200026"); // 活动已结束
		}
		//判断当前活动是否有抽奖限制
		if(NumberUtils.toInt(lottery.getLotteryNum())>0){
			//抽奖次数
			int alreadyTakeCount = lotteryMapper.selectTicketCount(userId, lotteryId,"1");
			if(alreadyTakeCount >=NumberUtils.toInt(lottery.getLotteryNum())){
				throw new BusinessException("E200032"); // 抽奖次数达到上限
			}
		}
		
		int ticketCount = lotteryMapper.selectTicketCount(userId, lotteryId,"0");
		if (ticketCount <= 0) {
			throw new BusinessException("E200027"); // 抽奖券不足
		}
		// 使用抽奖券
		lotteryMapper.cutLotterTicket(userId, lotteryId);

		List<GsPrize> prizes = lotteryMapper.selectPrizes(lotteryId);
		log.info("抽奖奖品信息{}",JsonUtil.object2String(prizes));
		if (prizes.isEmpty()) {
			throw new BusinessException("E200026"); // 活动已结束
		}
		
		//从奖品中获取指定的奖品
		int winCount = lotteryMapper.selectWinCount(lotteryId);
		log.info("已中奖人数{}",winCount);
		List<GsPrize> assignPrizes = lotteryMapper.getAssignPrizesByWinCount(lotteryId,winCount);
		log.info("指定抽奖奖品信息{}",JsonUtil.object2String(assignPrizes));
	
		if(null != assignPrizes && assignPrizes.size() >0){  //有指定的时候,走特定的逻辑
			for(GsPrize prize : assignPrizes){
				/*int prizeWinCount = lotteryMapper.selectPrizeWinCount(lotteryId, prize.getPrizeId());
				if(prizeWinCount < NumberUtils.toInt(prize.getPrizeCount())){
					prizes.add(prize);
				}*/
				if(NumberUtils.toInt(prize.getPrizeCount())>0){
					prizes.add(prize);
				}
			}
			
		}
		// 生产随机数
		int index = LotteryUtil.drawGift(prizes);
		log.info("抽奖随机数:{}",index);
		if (index < 0) {
			throw new BusinessException("E200026"); // 活动已结束
		}

		// 中奖奖品
		GsPrize prize = prizes.get(index);
		
		if(null != assignPrizes && assignPrizes.size() >0){  //有指定的时候,走特定的逻辑(如果还没有中奖,则强制中奖)
			for(GsPrize assignprize : assignPrizes){
				if(winCount == NumberUtils.toInt(assignprize.getEnd())){
					/*int prizeWinCount = lotteryMapper.selectPrizeWinCount(lotteryId, assignprize.getPrizeId());
					if(prizeWinCount < NumberUtils.toInt(assignprize.getPrizeCount())){
						prize = assignprize;
						break; //多个的话取出一个中奖
					}*/
					if(NumberUtils.toInt(assignprize.getPrizeCount())>0){
						prize = assignprize;
						break; //多个的话取出一个中奖
					}
				}
			}
		}
		log.info("中奖奖品信息{}",JsonUtil.object2String(prize));
		
		// 减少库存
		lotteryMapper.cutPrizeStock(prize.getPrizeId());

		// 插入中奖信息
		Map<String, String> userInfo = lotteryMapper.getUsBaseInfoByUserId(userId);

		GsLotteryWinning win = lotteryMapper.selectUserLotteryCount(userId, lotteryId);

		if (null == win) { // 未有中奖纪录，插入新的记录
			win = new GsLotteryWinning();

			win.setLotteryId(lotteryId);
			win.setUserId(userId);
			win.setMobile(userInfo.get("mobile"));
			win.setRealName(userInfo.get("realName"));
			win.setWinningId(IDGenerator.generatorId());
			lotteryMapper.insertLotteryWinning(win);
		}

		GsUserPrize userPrize = new GsUserPrize();
		userPrize.setWinningId(win.getWinningId());
		userPrize.setPrizeId(prize.getPrizeId());
		userPrize.setUpId(IDGenerator.generatorId());
		// 插入中奖商品
		lotteryMapper.insertUserPrize(userPrize);

		if (AppConstants.LOTTERY_PRIZE_TYPE_VIRTUAL.equals(prize.getPrizeType())) { // 优惠券

			// 赠送优惠券
			List<String> couponsId = new ArrayList<>();
			couponsId.add(prize.getCouponId());
			lotteryMapper.insertStdCoupon(userId, couponsId);

			// 修改商品赠送状态
			lotteryMapper.updateUserPrizeSend(userPrize.getUpId(), AppConstants.LOTTERY_USER_PRIZE_STATUS_SENDED);

		}
		prize.setUpId(userPrize.getUpId());

		return prize;
	}
	/**
	 * 中奖记录
	 * @param header
	 * @param body
	 * @return
	 */
	public Object getWinningInfo(Header header, Body body) {
		Map<String, String> lotteryMap = lotteryMapper.selectLotteryInfoByScholarship(body.getString("scholarship"));

		if (null == lotteryMap) {
			return null;
		}
		return lotteryMapper.selectWinnerInfoList(lotteryMap.get("lotteryId"));
	}

	public void setPrizeAddress(Header header, Body body) {
		GsUserPrize prize = new GsUserPrize();
		prize.setCity(body.getString("city"));
		prize.setCityCode(body.getString("cityCode"));
		prize.setDistrict(body.getString("district"));
		prize.setDistrictCode(body.getString("districtCode"));
		prize.setAddress(body.getString("address"));
		prize.setMobile(body.getString("mobile"));
		prize.setProvince(body.getString("province"));
		prize.setProvinceCode(body.getString("provinceCode"));
		prize.setUserName(body.getString("userName"));
		String upId = body.getString("upId");
		prize.setUpId(upId);
		lotteryMapper.updateUserPrizeAddress(prize);

	}

}
