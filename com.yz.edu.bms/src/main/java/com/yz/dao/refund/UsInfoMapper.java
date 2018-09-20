package com.yz.dao.refund;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface UsInfoMapper {

	/**
	 * 获取用户的openId
	 * @param userId
	 * @return
	 */
	String selectUserOpenId(String userId);
	
	/**
	 * 根据学业来查询对应学员的的openId
	 * @param sendId
	 * @return
	 */
	public String getOpenIdByLearnId(@Param("learnId") String learnId);
	
	/**
	 * 收书地址
	 * @param learnId
	 * @return
	 */
	Map<String, String> selectUsBookAddress(String learnId);

	/**
	 * 根据用户id获取用户信息
	 * @param userId
	 * @return
	 */
	Map<String, String> selectUserInfo(String userId);
	
	/**
	 * 根据手机号获取用户列表
	 * @param mobile
	 * @return
	 */
	List<Map<String, String>> getUserListInfoByMobile(String mobile);
}
