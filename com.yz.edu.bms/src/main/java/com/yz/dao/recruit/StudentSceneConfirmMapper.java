package com.yz.dao.recruit;

import com.yz.model.admin.BaseUser;
import com.yz.model.pubquery.TutorshipBookSendQuery;
import com.yz.model.recruit.StudentSceneConfirmInfo;
import com.yz.model.recruit.StudentSceneConfirmQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
public interface StudentSceneConfirmMapper {

        List<StudentSceneConfirmInfo> findAllSceneConfirm(@Param("query") StudentSceneConfirmQuery query, @Param("user") BaseUser user);

        int getAllSceneConfirmCount (@Param("query") StudentSceneConfirmQuery query, @Param("user") BaseUser user);

        List<Map<String,String>> getSceneRegisterList(String learnId);

        String existExamNo(String learnId);

        int insertExamNo(@Param("learnId") String learnId, @Param("stdId") String stdId, @Param("examNo") String examNo);

        int updateExamNo(@Param("learnId") String learnId, @Param("examNo") String examNo);

        List<Map<String,String>> getExamNoModifyRecord(String learnId);

        int updateRemark(@Param("learnId") String learnId, @Param("remark") String remark);

        int countMyAreaStudent(@Param("learnId") String learnId, @Param("empId") String empId);

        int countLeaveMyAreaStudent(@Param("learnId") String learnId, @Param("empId") String empId);
}
