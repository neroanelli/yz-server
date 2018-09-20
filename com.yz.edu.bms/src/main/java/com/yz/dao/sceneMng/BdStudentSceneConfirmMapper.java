package com.yz.dao.sceneMng;

import com.yz.model.admin.BaseUser;
import com.yz.model.exam.BdStudentSceneRegister;
import com.yz.model.exam.YzNetworkExamFrm;
import com.yz.model.recruit.StudentSceneConfirmInfo;
import com.yz.model.recruit.StudentSceneConfirmQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zlp
 * @version 1.0
 */
public interface BdStudentSceneConfirmMapper {

        List<StudentSceneConfirmInfo> findAllSceneConfirm(@Param("query") StudentSceneConfirmQuery query, @Param("user") BaseUser user);
        
        int getAllSceneConfirmCount (@Param("query") StudentSceneConfirmQuery query, @Param("user") BaseUser user);

        List<Map<String,String>> getSceneRegisterList(String learnId);

        String existExamNo(String learnId);

        int insertExamNo(@Param("learnId") String learnId, @Param("stdId") String stdId, @Param("examNo") String examNo);

        int updateExamNo(@Param("learnId") String learnId, @Param("examNo") String examNo);

        List<Map<String,String>> getExamNoModifyRecord(String learnId);

        int updateRemark(@Param("learnId") String learnId, @Param("remark") String remark);

        int resetSceneConfirmTask(@Param("confirmId") String confirmId,@Param("learnId") String learnId);
        
        int insertRegisterNo(Map<String, String> map);
        
        int setAvailabeRegisterNo(@Param("registerId")String registerId,@Param("learnId") String learnId);
        
        Map<String,String> getEaxmInfoByLearnId(@Param("learnId") String learnId);
        
        /**
         * 导入批量找到不存在的学员
         * @param studentDiplomaInfoList
         * @return
         */
        List<Map<String, Object>> getNonExistsStudent(@Param("studentExamNoInfoList")List<StudentSceneConfirmInfo> studentExamNoInfoList);
        
        /**
         * 批量检查学员是否现场确认
         * @param studentExamNoInfoList
         * @return
         */
        List<Map<String, Object>>  checkStudentSceneConfirmStatus(@Param("studentExamNoInfoList")List<StudentSceneConfirmInfo> studentExamNoInfoList);
        /**
         * 批量excel导入
         * @param studentDiplomaInfoList
         * @param user
         */
        void insertByExcel(@Param("studentExamNoInfoList")List<StudentSceneConfirmInfo> studentExamNoInfoList,@Param("user") BaseUser user);
        
        
        void editConfirmStatus(@Param("idCard") String idCard,@Param("examPayStatus") String examPayStatus,@Param("webRegisterStatus") String webRegisterStatus);


        void updatePPMStatus(@Param("learnId") String learnId);

        YzNetworkExamFrm getYzNetworkExamFrmByLeranId(@Param("learnId") String learnId);

        void insertYzNetworkExamFrm(YzNetworkExamFrm yzNetworkExamFrm);

        BdStudentSceneRegister getBdStudentSceneRegister(@Param("registerId") String registerId);

        void insertYzNetworkExamFrmAttr(@Param("id") Long id, @Param("username") String username, @Param("password") String password);
}
