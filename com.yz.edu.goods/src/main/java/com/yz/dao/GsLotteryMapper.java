package com.yz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.GsLottery;
import com.yz.model.GsLotteryWinning;
import com.yz.model.GsPrize;
import com.yz.model.GsUserPrize;

public interface GsLotteryMapper {

	/**
	 * 查询抽奖活动
	 * 
	 * @param lotteryCode
	 *            活动编码
	 * @return
	 */
	GsLottery selectLotteryByLotteryCode(String lotteryCode);

	/**
	 * 插入用户抽奖券
	 * 
	 * @param userId
	 *            用户ID
	 * @param lotteryId
	 *            抽奖活动ID
	 */
	void insertLotteryTicket(@Param("userId") String userId, @Param("lotteryId") String lotteryId,
						@Param("ticketId") String ticketId,@Param("giveWayType") String giveWayType);

	/**
	 * 查询对应活动的用户抽奖次数
	 * 
	 * @param userId
	 * @param lotteryId
	 * @param isUsed  是否使用
	 * @return
	 */
	int selectTicketCount(@Param("userId") String userId, @Param("lotteryId") String lotteryId,@Param("isUsed") String isUsed);

	/**
	 * 中奖轮循信息
	 * 
	 * @param lotteryId
	 * @return
	 */
	List<Map<String, String>> selectWinnerInfoList(String lotteryId);

	/**
	 * 查询抽奖活动
	 * 
	 * @param lotteryId
	 * @return
	 */
	GsLottery selectLotteryByLotteryId(String lotteryId);
	/**
	 * 获取抽奖的奖品信息
	 * @param lotteryId
	 * @return
	 */
	List<GsPrize> selectPrizes(String lotteryId);

	GsLotteryWinning selectUserLotteryCount(@Param("userId") String userId, @Param("lotteryId") String lotteryId);

	void insertLotteryWinning(GsLotteryWinning winInfo);

	void cutPrizeStock(String prizeId);

	void insertUserPrize(GsUserPrize userPrize);

	void updateUserPrizeSend(@Param("upId") String upId, @Param("sendStatus") String sendStatus);

	/**
	 * 使用一张抽奖券
	 * 
	 * @param userId
	 * @param lotteryId
	 */
	void cutLotterTicket(@Param("userId") String userId, @Param("lotteryId") String lotteryId);

	/**
	 * 获取某个抽奖的中奖人数
	 * @param lotteryId
	 * @return
	 */
	int selectWinCount(String lotteryId);

	/**
	 * 查找iphone中奖次数
	 * 
	 * @param lotteryId
	 * @param prizeId
	 * @return
	 */
	int selectPrizeWinCount(@Param("lotteryId") String lotteryId, @Param("prizeId") String prizeId);

	/**
	 * 获取我的中奖信息
	 * 
	 * @param userId
	 * @param lotteryId
	 * @return
	 */
	List<Map<String, String>> selectWinningInfo(@Param("userId") String userId, @Param("lotteryId") String lotteryId);

	/**
	 * 地址
	 * @param prize
	 */
	void updateUserPrizeAddress(GsUserPrize prize);

	/**
	 * 获取中奖记录
	 * @param lotteryId
	 * @return
	 */
	List<Map<String, String>> selectAllWinningInfo(String lotteryId);

	
	/**
	 * 获取用户的基本信息
	 * @param userId
	 * @return
	 */
	Map<String, String> getUsBaseInfoByUserId(@Param("userId") String userId);
	
	/**
	 * 插入学员抽奖券
	 * @param userId
	 * @param couponIds
	 * @return
	 */
	int insertStdCoupon(@Param("userId") String userId, @Param("couponIds") List<String> couponIds);
	
	/**
	 * 根据优惠类型获取抽奖
	 * @param scholarship
	 * @return
	 */
	public Map<String,String> selectLotteryInfoByScholarship(@Param("scholarship") String scholarship);
	
	/**
	 * 根据中奖人数获取指定的奖品
	 * @param lotteryId
	 * @param winCount
	 * @return
	 */
	public List<GsPrize> getAssignPrizesByWinCount(@Param("lotteryId") String lotteryId,@Param("winCount") int winCount);
	
}
