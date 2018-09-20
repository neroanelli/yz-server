package com.yz.dao.sceneMng;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.sceneMng.OaSceneManagement;
import com.yz.model.stdService.StudentXuexinInfo;

public interface OaSceneManagementMapper {
	
	/**
	 * 查询现场确认点信息
	 * @param sceneManagement
	 * @return
	 */
	List<OaSceneManagement> getSceneManagement(OaSceneManagement sceneManagement);
	
	
	/**
	 * 新增
	 * @param sceneManagement
	 */
	void insert(OaSceneManagement sceneManagement);
	
	/**
	 * @param confirmationId
	 * @return
	 */
	Map<String,Object> getSceneManagementById(@Param("confirmationId") String confirmationId);
	
	
	/**
	 * 删除
	 * @param confirmationId
	 */
	void delete(@Param("confirmationId") String confirmationId);
	
	
	 /**
     * 根据ID批量删除信息
     * @param confirmationIds
     */
    void deleteByIdArr(@Param("confirmationIds") String[] confirmationIds);
    
    /**
	 * 获取考试区县下拉对象
	 * @return
	 */
	List<Map<String, String>> getExamDicName();
	
	
	/**
	 * 修改
	 * @param confirmationId
	 */
	void updateSceneManagement(OaSceneManagement sceneManagement);
	
	void updateIsAllow(@Param("confirmationId") String confirmationId,@Param("status") String status);
	
	/**
     * 批量修改状态
     * @param configIds
     * @param status
     */
    void updateStatus(@Param("confirmationIds") String[] confirmationIds,@Param("status") String status);
    
    /**
     * 查找该确认点是否有学员配置
     * @param confirmationId
     * @return
     */
    int findStuConfirmById(@Param("confirmationId") String confirmationId);
	
    
    void insertConfirma(@Param("sceneManagementList")List<OaSceneManagement> sceneManagementList,@Param("user") BaseUser user);
    
    List<Map<String, Object>> getNonExistsExamAddress(@Param("sceneManagementList")List<OaSceneManagement> sceneManagementList);
}
