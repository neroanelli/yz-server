package com.yz.job.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface BdsCourseMapper {

	/**
	 * 根据类型和时间区间查询课程
	 * @param courseType
	 * @param startTimePre
	 * @param endTimePre
	 * @return
	 */
	List<Map<String, String>> selectCourse(@Param("courseType") String courseType,@Param("startTimePre") String startTimePre, @Param("endTimePre") String endTimePre);

	/**
	 * 查询直播/面授+直播的课程
	 * @param courseType
	 * @param startTimePre
	 * @param endTimePre
	 * @return
	 */
	List<Map<String, String>> selectLiveCourse(@Param("courseType") String courseType,@Param("startTimePre") String startTimePre, @Param("endTimePre") String endTimePre);
	
	/**
	 * 查看要上辅导课的学员信息
	 * @param courseId
	 * @param stdStage(暂时去掉)
	 * @return
	 */
	List<Map<String, String>> selectUserIdByFDCourseId(@Param("courseId") String courseId);
	
	/**
	 * 查看要上学科的学员信息
	 * @param courseId
	 * @param stdStage
	 * @return
	 */
	List<Map<String, String>> selectUserIdByXKCourseId(@Param("courseId") String courseId, @Param("stdStage") String stdStage);
	
	/**
	 * 批量获取openId
	 * @param userIds
	 * @return
	 */
	List<String> getOpenIdsByUserIds(@Param("userIds") List<String> userIds);

}
