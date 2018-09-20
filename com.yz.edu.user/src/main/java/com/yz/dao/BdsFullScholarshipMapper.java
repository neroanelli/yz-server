package com.yz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @描述: 全额奖学金活动
 * @作者: DuKai
 * @创建时间: 2018/2/27 14:38
 * @版本号: V1.0
 */
public interface BdsFullScholarshipMapper {

    Map<String, Object>  selectEnrolmentCount(String scholarship);

    List<Map<String, Object>>  selectNewEnrolmentList(String scholarship);
    
    /**
     * 新评论信息列表
     * @return
     */
    List<Map<String, String>> selectNewMsgList(String scholarship);
    
    /**
     * 某个报读类型某个用户的评论数
     * @param scholarship
     * @param userId
     * @return
     */
    int getMsgCount(@Param("scholarship") String scholarship,@Param("userId") String userId);
    
    /**
     * 留言
     * @param enrollMap
     * @return
     */
    public void enrollMsg(Map<String, String> enrollMap);
}
