package com.yz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 网报注册
 * 
 * @author lx
 * @date 2018年8月23日 下午4:52:20
 */
public interface RegNetWorkExamFrmMapper {

	public List<Map<String, String>> getStudentEducationForUpdate();
	
	public List<Map<String, String>> getStudentEducationForLook();

	public List<Map<String, String>> getStudentEducationGet();

	public List<Map<String, String>> getStudentEducationCheck();

	public List<Map<String, String>> getStudentNetWorkInfoBatchForFindParam();

	public List<Map<String, String>> getStudentNetWorkInfoBatchForParam();

	/**
	 * 根据身份证号获取网报信息
	 * 
	 * @param idCard
	 * @return
	 */
	public Map<String, String> getStudentNetWorkInfo(String learnId);

	/**
	 * 根据身份证号和手机号获取网报信息
	 * 
	 * @param idCard
	 * @return
	 */
	public Map<String, String> getStudentNetWorkInfoByIdCardAndMobile(@Param("idCard") String idCard,
			@Param("mobile") String mobile);

	/**
	 * 新增预报名信息
	 * 
	 * @param stuMap
	 */
	public void insertStudentSceneRegister(Map<String, String> stuMap);

	/**
	 * 更改有效网报信息
	 * 
	 * @param learnId
	 */
	public void updateStudentRegisterStatus(String learnId);

	/**
	 * 获取学员注册成功次数
	 * 
	 * @param learnId
	 * @return
	 */
	public int studentSceneRegisterNum(String learnId);

	/**
	 * 更新网报状态
	 * 
	 * @param status
	 * @return
	 */
	public int updateNetWorkStatus(@Param("status") String status, @Param("learnId") String learnId);

	/**
	 * 更新手机绑定状态
	 * 
	 * @param status
	 * @param learnId
	 * @return
	 */
	public int updateMobileBindStatus(@Param("status") String status, @Param("learnId") String learnId);

	/**
	 * 更新学员现场确认表中的网报状态
	 * 
	 * @param learnId
	 * @return
	 */
	public int updateStudentWebRegisterStatus(String learnId);

	/**
	 * 分批获取待网报数据
	 * 
	 * @param limit
	 * @return
	 */
	public List<Map<String, String>> getStudentNetWorkInfoBatch();

	/**
	 * 更新学员确认状态
	 * 
	 * @param examPayStatus
	 *            缴费状态
	 * @param picCollectStatus
	 *            相片采集状态
	 * @param mobileBindStatus
	 *            手机绑定状态
	 * @param learnId
	 * @return
	 */
	int updateSceneConfirmStatus(@Param("examPayStatus") int examPayStatus,
			@Param("picCollectStatus") int picCollectStatus, @Param("mobileBindStatus") int mobileBindStatus,
			@Param("learnId") String learnId);

	/**
	 * 更新预报名表打印内容
	 * 
	 * @param printHtml
	 * @param learnId
	 * @return
	 */
	int updateScenePrintHtml(@Param("printHtml") String printHtml, @Param("printStatus") int printStatus,
			@Param("learnId") String learnId);

	/**
	 * 根据learnId得到报读院校信息
	 * 
	 * @param idCard
	 * @return
	 */
	public Map<String, String> getUnvsInfoByLearnId(@Param("learnId") String learnId);

	/**
	 * 根据learnId得到预报名号
	 * 
	 * @param learnId
	 * @return
	 */
	public Map<String, String> getStudentSceneRegisterByLearnId(@Param("learnId") String learnId);

	/**
	 * 获取待采集报考基本信息的学员
	 * 
	 * @return
	 */
	List<Map<String, String>> getSceneConfirmCollectStudent();

	public String getNewMobileByIdCard(@Param("idCard") String idCard);

	public String getNetReportDataStutus(@Param("learnId") String learnId);

	public int updateNetReprotDataOfMobile(@Param("idCard") String idCard, @Param("mobile") String mobile);

	/**
	 * 更改状态
	 * 
	 * @param status
	 * @param learnId
	 */
	public void updateReportStatus(@Param("status") String status, @Param("learnId") String learnId);

	/**
	 * 是否成功
	 * 
	 * @param learnId
	 */
	public void updateRegIfSuccess(@Param("learnId") String learnId);
	
	/**
	 * 获取已预约待现场确认的学员
	 * @return
	 */
	List<Map<String, String>> getSceneConfirmCollectByAlreadyReserved();

}
