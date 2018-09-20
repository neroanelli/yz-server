package com.yz.dao.transfer;

import com.yz.model.admin.BaseUser;
import com.yz.model.transfer.BdStudentModify;
import com.yz.model.transfer.StudentModifyMap;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BdExamModifyReviseMapper {
    List<Map<String,String>> findStudentAuditModify(@Param("studentModifyMap") StudentModifyMap studentModifyMap, @Param("user") BaseUser user);

    HashMap<String,String> findStudentInfo(@Param("learnId") String learnId);

    void updateupdateBdStudentHistory(BdStudentModify bdStudentModify);

    String getRecruitOpenIdByLearnId(@Param("learnId") String learnId);

    void updateStudentBirt(@Param("stdId") String stdId, @Param("birt") String birt);
}
