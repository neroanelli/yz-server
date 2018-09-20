package com.yz.network.examination.dao;

import com.yz.network.examination.model.BdLearnQueueInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QueueMapper {

    /**
     * 根据条件查询列表
     * @param pfsnLevel
     * @param cityCode
     * @return
     */
    List<BdLearnQueueInfo> getQueueList(@Param("pfsnLevel") String pfsnLevel, @Param("cityCode") String cityCode);

    /**
     * 更新确认中的状态及下几位的状态
     * @param pfsnLevel
     * @param cityCode
     * @param userId
     * @param queueIds
     */
    void updateNextStatus(@Param("pfsnLevel") String pfsnLevel, @Param("cityCode") String cityCode, @Param("userId") String userId, @Param("queueIds") List<String> queueIds);

    /**
     * 未预约插队
     * @param queueId
     * @param queueNo
     */
    void jumpQueue(@Param("queueId") String queueId, @Param("queueNo") String queueNo);

    /**
     * 查询等待下几位的人
     * @param pfsnLevel
     * @param cityCode
     * @param number
     * @return
     */
    List<String> selectNextNumber(@Param("pfsnLevel") String pfsnLevel, @Param("cityCode") String cityCode, @Param("number") Integer number);

    /**
     * 添加签到排号记录
     * @param bdLearnQueueInfo
     */
    void addQueue(@Param("bdLearnQueueInfo") BdLearnQueueInfo bdLearnQueueInfo);

    /**
     *  获取当前等待的人数
     * @param pfsnLevel
     * @param cityCode
     * @param isPlace 是否预约
     * @return
     */
    String selectWaitNum(@Param("pfsnLevel") String pfsnLevel, @Param("cityCode") String cityCode, @Param("isPlace") boolean isPlace);
}
