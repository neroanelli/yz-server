package com.yz.dao.stdService;

import com.yz.model.admin.BaseUser;
import com.yz.model.stdService.StudentDiplomaInfo;
import com.yz.model.stdService.StudentDiplomaQuery;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StudentDiplomaMapper {
	
	/**毕业证发放列表*/
    List<StudentDiplomaInfo> findAllDiplomaList(@Param("queryInfo") StudentDiplomaQuery query, @Param("user") BaseUser user);
    
    
    /**修改备注**/
    int updateRemark(@Param("followId") String followId, @Param("remark")String remark);
    
    /**
     * 修改毕业证发放
     * @param diplomaInfo
     * @return
     */
    int editDiplomaInfo(StudentDiplomaInfo diplomaInfo);
    
    int receiveInfoSet(@Param("configId") String configId,@Param("configIdOld") String configIdOld,@Param("learnId") String learnId,@Param("taskId") String taskId);
    /**
     * 重置任务
     * @param degreeId
     * @param taskId
     * @param learnId
     * @return
     */
    int resetTask(@Param("followId") String followId, @Param("taskId") String taskId, @Param("learnId") String learnId);

    /**
     * 重置未确认原因
     * @param followId
     * @return
     */
    int resetUnconfirmReason(@Param("followId") String followId);
    /**

     * 根据任务Id得到确认时间段列表
     * @param taskId
     * @return
     */
    List<Map<String, String>> findAffirmTimeList(@Param("taskId") String taskId);
    
    /**
     * 得到统计列表
     * @param taskId
     * @return
     */
    List<Map<String, Object>> selectDiplomaStatisticsInfo(@Param("taskId") String taskId,@Param("tutor") String tutor);
    
    
    /**
     * 根据省市区得到学员的可领取地址列表
     * @param diplomaInfo
     * @return
     */
    List<Map<String, Object>> getReceivePlaceList(StudentDiplomaInfo diplomaInfo);
    
    /**
     * 得到具体地址
     * @param placeId
     * @return
     */
    Map<String, String> getReceiveAddress(@Param("placeId") String placeId);
    
    List<Map<String, String>> findAffirmDateListByLearnId(StudentDiplomaInfo diplomaInfo);
    
    List<Map<String, String>> findAffirmTimeListByLearnId(StudentDiplomaInfo diplomaInfo);
    
    /**
     * 导入批量找到不存在的学员
     * @param studentDiplomaInfoList
     * @return
     */
    List<Map<String, Object>> getNonExistsStudent(@Param("studentDiplomaInfoList")List<StudentDiplomaInfo> studentDiplomaInfoList);
    
    /**
     * 批量excel导入
     * @param studentDiplomaInfoList
     * @param user
     */
    void insertByExcel(@Param("studentDiplomaInfoList")List<StudentDiplomaInfo> studentDiplomaInfoList,@Param("user") BaseUser user);
    
    /**
     * 当前学业是否存在毕业证发放跟进
     * @param learnId
     * @return
     */
    int checkIfExistByLearnId(@Param("learnId") String learnId);
    
    /**
     * 初始化跟进
     * @param list
     */
    void addStuDiplomaInfo(@Param("list") List<StudentDiplomaInfo> list);
    
    /**
     * 批量删除
     * @param ids
     * @param taskId
     */
    void delStuDiplomaInfo(@Param("ids") String[] ids,@Param("taskId") String taskId);
    
    /**
     * 单独增加
     * @param info
     */
    void singleAddStuDiplomaInfo(StudentDiplomaInfo info);
    
    /**
     * 单独删除
     * @param taskId
     * @param learnId
     */
    void aloneDelStuDiplomaInfo(@Param("learnId") String learnId,@Param("taskId") String taskId);
}
