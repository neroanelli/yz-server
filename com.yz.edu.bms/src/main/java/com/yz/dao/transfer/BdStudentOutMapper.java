package com.yz.dao.transfer;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.transfer.BdCheckRecord;
import com.yz.model.transfer.BdStudentOut;
import com.yz.model.transfer.BdStudentOutExport;
import com.yz.model.transfer.BdStudentOutMap;
import com.yz.model.transfer.BdStudentOutRemark;

public interface BdStudentOutMapper {
	int deleteByPrimaryKey(String outId);

	int insert(BdStudentOut record);

	int insertSelective(BdStudentOut record);

	BdStudentOut selectByPrimaryKey(String outId);

	int updateByPrimaryKeySelective(BdStudentOut record);

	int updateByPrimaryKey(BdStudentOut record);

	List<BdStudentOutMap> findAllBdStudentOut(@Param("out") BdStudentOutMap bdStudentOut, @Param("user") BaseUser user);
	
	List<BdStudentOutExport> findAllBdStudentOutExport(@Param("out") BdStudentOutMap bdStudentOut, @Param("user") BaseUser user);

	List<Map<String, String>> findStudentInfo(@Param("sName") String sName, @Param("user") BaseUser user);

	List<Map<String, String>> getCheckWeight(String stdStage);

	Map<String, Object> findStudentInfoById(String learnId);

	List<Map<String, String>> findFinancialApproval(BdStudentOutMap studentOutMap);

	List<BdStudentOutMap> findDirectorApproval(@Param("outMap") BdStudentOutMap bdStudentOut,
			@Param("user") BaseUser user);

	List<BdStudentOutMap> findSchoolManagedApproval(BdStudentOutMap studentOutMap);

	List<BdStudentOutMap> findSenateApproval(BdStudentOutMap studentOutMap);

	Map<String, String> findStudentInfoByGraStdId(String learnId);

	Map<String, String> findStudentInfoByLearnId(String learnId);
	
	List<Map<String, String>> selectOpareRecord(String outId, String order);

	String getCheckOrder(@Param("checkType") String checkType, @Param("jtId") String jtId);

	List<BdStudentOut> selectUnCheckIds(@Param("outIds") String[] outIds);

	int deleteStudentOut(String outId);

	Map<String, String> selectStdInfoByLearnId(String learnId);

	int selectOutCount(String learnId);

	List<BdStudentOutExport> selectOutExportInfo();

	void updateFileUrl(@Param("outId") String outId, @Param("url") String url, @Param("fileName") String fileName);

	Map<String, String> selectUserInfoForLearnId(@Param("stdId") String stdId);
	
	void InsertStudentOutRemark(BdStudentOutRemark bdStudentOutRemark);
	
	List<BdCheckRecord> selectCheckRecordByOutId(@Param("outId") String outId);
	
	List<BdStudentOutRemark> findBdStudentOutRemarkByOutId(@Param("outId") String outId);
	
	String findXjBandId(@Param("empId") String empId);
}