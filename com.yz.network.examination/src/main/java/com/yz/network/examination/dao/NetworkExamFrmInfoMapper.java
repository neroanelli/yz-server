package com.yz.network.examination.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.network.examination.model.YzNetworkExamFrmAttr;
import com.yz.network.examination.model.YzNetworkExamInfoFrm;

/***
 * 
 * @desc
 * @author lingdian
 *
 */
public interface NetworkExamFrmInfoMapper {

	void saveNetworkExamFrm(YzNetworkExamInfoFrm record);

	/**
	 * 批量保存网报表单属性
	 * 
	 * @param attrs
	 */
	void saveNetworkExamFrmAttrs(@Param(value = "attrs") List<YzNetworkExamFrmAttr> attrs);

	List<Map<String, String>> getNetWorkList(@Param("idCard") String idCard);

	String getcount(@Param("learnId") String learnId);

	String getNetRegistCount(@Param("learnId") String learnId);

	Map<String, String> getStudentInfo(@Param("learnId") String learnId);

	Map<String, String> getNetWorkDucation(@Param("learnId") String learnId);
	
	// 获取自己组装的数据
	Map<String, String> getStdReportData(@Param("learnId") String learnId);

	Map<String, String> getRegReportData(@Param("learnId") String learnId);

	// 传入院校代码，获取对应的院校代码
	Map<String, String> getUnvsDataForExam(@Param("unvsCode") String unvsCode, @Param("pfsnLevel") String pfsnLevel,
			@Param("pfsnName") String pfsnName, @Param("objCode") String objCode);

	// 保存数据到reportData
	void addNetReportData(Map<String, String> map);
	//更新数据
	void upNetReportData(Map<String, Object> map);

}