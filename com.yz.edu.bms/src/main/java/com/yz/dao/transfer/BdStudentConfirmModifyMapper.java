package com.yz.dao.transfer;

import com.yz.model.admin.BaseUser;
import com.yz.model.transfer.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BdStudentConfirmModifyMapper {

	List<Map<String, String>> findConfirmModify(@Param("modi") StudentModifyMap studentModifyMap,
                                                @Param("user") BaseUser user);
	
	void deleteStudentModify(@Param("ids") String[] ids);
	
	BdStudentModify findStudentModifyById(String modifyId);
	
	List<Map<String, String>> findStudentInfo(@Param("sName") String sName,
			@Param("user") BaseUser user);
	Map<String, String> findStudentEnrollInfo(String learnId);
	
	void updateExt1(@Param("modifyId") String modifyId,@Param("ext1") String ext1);

    Integer ifModifyingByLearnId(@Param("learnId")String learnId);

    String getCityCodeByTaId(@Param("taId")String taId);
	
}