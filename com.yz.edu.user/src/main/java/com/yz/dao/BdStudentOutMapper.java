package com.yz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.BdStudentOut;
import com.yz.model.StudentOutListMap;
import com.yz.model.student.BdCheckRecord;
import com.yz.model.student.BdStudentModify;

public interface BdStudentOutMapper {
	
	void insertSelectiveBdStudentModify(BdStudentModify studentModify);
	
	void insertSelectiveRecord(BdCheckRecord checkRecord);
	
	int insertSelective(BdStudentOut record);

	List<Map<String, String>> getCheckWeight(String stdStage);

	Map<String, String> findStudentInfoById(String learnId);
	
	int selectOutCount(String learnId);

	Map<String, String> getDict(@Param("dictId") String dictId);
	
	List<StudentOutListMap> studentOutList(@Param("learnId") String learnId);
	
	void updateFileUrl(@Param("outId") String outId, @Param("url") String url, @Param("fileName") String fileName);
	
	Map<String, String> selectUserInfoForLearnId(@Param("stdId") String stdId);
	
	Map<String, String> findStudentInfoByLearnId(String learnId);
	
	String findXjBandId(@Param("empId") String empId);
}