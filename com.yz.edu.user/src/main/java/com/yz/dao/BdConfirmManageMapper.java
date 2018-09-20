package com.yz.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yz.model.BdConfirmStudentInfo;
import com.yz.model.student.BdStudentModify;
import org.apache.ibatis.annotations.Param;

import com.yz.model.BdConfirmManage;

public interface BdConfirmManageMapper {
	/**
	 * 钉钉查询现场确认点
	 * @return
	 */
    List<BdConfirmManage> getConfirmManageList(BdConfirmManage bdConfirmManage);

    /**
     * 根据条件查询现场确认点名称（不重复的）
     * @param bdConfirmManage
     * @return
     */
    List<Map<String,String>> getConfirmName(BdConfirmManage bdConfirmManage);
	
	/**
	 * 查询现场确认城市
	 * @return
	 */
	List<Map<String,String>> getConfirmCity();
	
	/**
	 * 查询现场确认点的县
	 * @return
	 */
	List<Map<String,String>> getTaName(BdConfirmManage bdConfirmManage);
	
	/**
	 * 查询确认点专业层次
	 * @return
	 */
	List<Map<String,String>> getConfirmLevel(BdConfirmManage bdConfirmManage);

	/**
	 * 查询确认学员信息
	 * @param searchInfo
	 * @return
	 */
	List<BdConfirmStudentInfo> getConfirmInfo(@Param("searchInfo")String searchInfo,@Param("city_code")String city_code);

    /**
     * 根据用户id查询城市code
     * @param userId
     * @return
     */
	HashMap<String,String> getCityCodeByUserId(@Param("userId")String userId);

    /**
     * 现场确认签到
     * @param bds
     */
	void confirmSign(BdConfirmStudentInfo bds);

    /**
     * 根据确认id查询考生号
     * @param confirmId
     * @return
     */
    BdConfirmStudentInfo existExamNo(@Param("confirmId")String confirmId);

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
     * 生成考生号变动记录
     * @param record
     * @return
     */
    int insertSelective(BdStudentModify record);

}
