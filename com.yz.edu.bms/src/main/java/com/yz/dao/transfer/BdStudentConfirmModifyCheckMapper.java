package com.yz.dao.transfer;

import com.yz.model.admin.BaseUser;
import com.yz.model.transfer.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BdStudentConfirmModifyCheckMapper {

	List<Map<String, String>> findConfirmModify(@Param("studentModifyMap") StudentModifyMap studentModifyMap, @Param("user") BaseUser user);
	
	BdStudentConfirmModify findStudentModifyById(String modifyId);
	
	void passStudentModifyCheck(@Param("modifyId")String modifyId,@Param("remark")String remark, @Param("user") BaseUser user);

	void passStudentModifyRecordOneCheck(@Param("checkStatus")String checkStatus,@Param("modifyId")String modifyId,@Param("reason")String reason,@Param("remark")String remark, @Param("user") BaseUser user); 
	
	void passStudentModifyRecordTwoCheck(@Param("checkStatus")String checkStatus,@Param("modifyId")String modifyId);
	
	List<Map<String, String>> findStudentModifyByModifyId(String modifyId);
	
	void passModifyBatch(@Param("modifyIds") String[] modifyIds,@Param("user") BaseUser user); 
}