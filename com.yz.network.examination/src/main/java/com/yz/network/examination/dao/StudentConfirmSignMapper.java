package com.yz.network.examination.dao;

import org.apache.ibatis.annotations.Param;

import com.yz.network.examination.model.BdConfirmStudentInfo;
import com.yz.network.examination.model.BdStudentModify;

import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author: luxing
 * @Date 2018\8\8 0008 17:20
 **/
public interface StudentConfirmSignMapper {

    /**
     * 根据身份证查询学员报读信息
     * @param searchInfo
     * @return
     */
    List<BdConfirmStudentInfo> getConfirmInfo(@Param("searchInfo")String searchInfo,@Param("cityCode")String cityCode);

    /**
     * 查询签到账号的总签到人数
     * @param userId
     * @return
     */
    String getTotalCount(@Param("userId") String userId);

    /**
     * 查询签到账号当天的总签到人数
     * @param userId
     * @return
     */
    String getTodayCount(@Param("userId") String userId);

    /**
     * 现场确认签到
     * @param bds
     */
    void confirmSign(@Param("query") BdConfirmStudentInfo bds);
    
    /**
     * 根据确认id查询考生号和签到状态信息
     * @param confirmId
     * @return
     */
    BdConfirmStudentInfo existExamNo(@Param("confirmId") String confirmId);
    
    /**
     * 插入考生号数据
     * @param learnId
     * @param stdId
     * @param examNo
     * @return
     */
    int insertExamNo(@Param("learnId") String learnId, @Param("stdId") String stdId, @Param("examNo") String examNo);
    
    /**
     * 更新考生号数据
     * @param learnId
     * @param examNo
     * @return
     */
    int updateExamNo(@Param("learnId") String learnId, @Param("examNo") String examNo);

    
   /**
    * 添加考生号变动记录
    * @param studentModify
    */
    void insertSelectiveBdStudentModify(BdStudentModify studentModify);

    /**
     * 废弃排序号
     * @param learnId
     */
    void discardNum(@Param("learnId") String learnId);

}
