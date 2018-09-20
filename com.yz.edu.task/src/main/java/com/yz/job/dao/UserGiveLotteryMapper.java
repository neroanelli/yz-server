package com.yz.job.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.job.model.UserLotteryTicketInfo;

/**
 * 赠送抽奖机会
 * @author lx
 * @date 2018年7月13日 下午5:28:28
 */
public interface UserGiveLotteryMapper {

	/**
	 * 根据优惠类型获取对应的抽奖活动
	 * @param scholarship
	 * @return
	 */
	public Map<String,String> selectLotteryInfoByScholarship(@Param("scholarship") String scholarship);
	
	/**
	 * 获取用户的基本信息
	 * @param userId
	 * @return
	 */
	public Map<String, String> getUsBaseInfoByUserId(@Param("userId") String userId);
	
	/**
	 * 赠送抽奖机会
	 * @param ticketInfo
	 */
	public void insertLotteryTicket(UserLotteryTicketInfo ticketInfo);
	
	/**
	 * 根据手机号获取某个时间段内报读的学业
	 * @param mobile
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, String>> selectLearnIdsByMobile(@Param("mobile") String mobile,@Param("startTime") String startTime,@Param("endTime") String endTime);
	
	
	/**
	 * 查询对应科目是否已缴
	 * @param learnId
	 * @param itemCode
	 * @return
	 */
	int selectTutionPaidCount(@Param("learnId") String learnId, @Param("itemCode") String itemCode,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime);
	
	/**
	 * 根据手机号获取某个时间段内报读的学业
	 * @param mobile
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int selectLearnCountByMobile(@Param("mobile") String mobile,@Param("startTime") String startTime,@Param("endTime") String endTime);
	
	/**
	 * 是否赠送过
	 * @param lotteryId
	 * @return
	 */
	int ifSendTicket(@Param("lotteryId") String lotteryId,@Param("userId") String userId);
}
