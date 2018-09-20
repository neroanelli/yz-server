package com.yz.service.recruit;

import com.yz.constants.GlobalConstants;
import com.yz.constants.TransferConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.oa.OaEmployeeMapper;
import com.yz.dao.pubquery.TutorshipBookSendMapper;
import com.yz.dao.recruit.StudentSceneConfirmMapper;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.SessionDpInfo;
import com.yz.model.oa.OaEmployeeJobInfo;
import com.yz.model.pubquery.TutorshipBookSendQuery;
import com.yz.model.recruit.StudentSceneConfirmInfo;
import com.yz.model.recruit.StudentSceneConfirmQuery;
import com.yz.model.transfer.BdStudentModify;
import com.yz.service.transfer.BdStudentModifyService;
import com.yz.service.transfer.BdStudentOutService;
import com.yz.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
@Service
public class StudentSceneConfirmService {

    private static Logger log = LoggerFactory.getLogger(StudentSceneConfirmService.class);

    @Autowired
    StudentSceneConfirmMapper studentSceneConfirmMapper;

    @Autowired
    private BdStudentModifyService modifyService;

    @Autowired
    private OaEmployeeMapper oaEmployeeMapper;



    /**
     * 我的学员现场确认查询
     * @param query
     * @return
     */
    public List<StudentSceneConfirmInfo> findAllSceneConfirm(StudentSceneConfirmQuery query,int start,int length) {
        BaseUser user = getUser();
        PageHelper.offsetPage(start, length).setCountMapper("com.yz.dao.recruit.StudentSceneConfirmMapper.getAllSceneConfirmCount");
        List<StudentSceneConfirmInfo> result = studentSceneConfirmMapper.findAllSceneConfirm(query,user);
        return result;
    }

    public StudentSceneConfirmInfo getInfoById(String confirmId) {
        BaseUser user = getUser();
        StudentSceneConfirmQuery query = new StudentSceneConfirmQuery();
        query.setConfirmId(confirmId);
        StudentSceneConfirmInfo studentSceneConfirmInfo;
        List<StudentSceneConfirmInfo> list = studentSceneConfirmMapper.findAllSceneConfirm(query, user);
        if (list.size() > 0) {
            studentSceneConfirmInfo = list.get(0);
            if(user.getUserLevel().equals("1")){
                return studentSceneConfirmInfo;
            }
            if (user.getJtList().contains("XJ") || user.getJtList().contains("XJZL")) {
                if (!user.getEmpId().equals(studentSceneConfirmInfo.getEmpId()) ) {
                    if((studentSceneConfirmInfo.getEmpStatus().equals("1") || studentSceneConfirmInfo.getEmpStatus().equals("3"))) {
                        studentSceneConfirmInfo.setMobile("******");
                        studentSceneConfirmInfo.setIdCard("******");
                    }
                }
            }
            else if(!user.getEmpId().equals(studentSceneConfirmInfo.getEmpId())){
                studentSceneConfirmInfo.setMobile("******");
                studentSceneConfirmInfo.setIdCard("******");
            }
            return studentSceneConfirmInfo;
        }
        return null;
    }

    private BaseUser getUser(){
        BaseUser user = SessionUtil.getUser();
        // 校监助理和校监一样权限
        if (user.getJtList().contains("XJZL")) {
            OaEmployeeJobInfo jobInfo = oaEmployeeMapper.getEmployeeJobInfo(user.getEmpId());
            List<SessionDpInfo> list =new ArrayList<SessionDpInfo>();
            SessionDpInfo sessionDpInfo = new SessionDpInfo();
            sessionDpInfo.setDpId(jobInfo.getDpId());
            list.add(sessionDpInfo);
            user.setMyDpList(list);
            user.setUserLevel("3");
        }
        return user;
    }

    public List<Map<String,String>> getSceneRegisterList(String learnId,String empId){
        BaseUser user = SessionUtil.getUser();
        List<Map<String,String>> list = studentSceneConfirmMapper.getSceneRegisterList(learnId);
        boolean isShow = isShowPassword(user,learnId,empId);
        //editBy zhuliping at 2018-09-04
        isShow=true;
        for (Map<String,String> map:list){
            if(!isShow){
                map.put("password","******");
            }
        }
        return list;
    }

    public List<Map<String,String>> getExamNoModifyRecord(String learnId){
        return studentSceneConfirmMapper.getExamNoModifyRecord(learnId);
    }

    @Transactional
    public void updateConfirmInfo(String learnId,String stdId,String examNo,String remark){
        String eNo = studentSceneConfirmMapper.existExamNo(learnId);
        if(eNo==null){
            if(StringUtil.hasValue(examNo)) {
                studentSceneConfirmMapper.insertExamNo(learnId, stdId, examNo);
                addModifyRecord(learnId, stdId, "添加考生号：" + examNo);
            }
        }else{
            if(!examNo.equals(eNo)){
                addModifyRecord(learnId,stdId,"将考生号'"+eNo+"'变更为'"+examNo+"'");
                studentSceneConfirmMapper.updateExamNo(learnId,examNo);
            }
        }
        studentSceneConfirmMapper.updateRemark(learnId,remark);
    }

    /**
     * 添加考生号变更记录
     * @param learnId
     * @param stdId
     * @param modifyText
     */
    public void addModifyRecord(String learnId,String stdId,String modifyText){
        BaseUser user = SessionUtil.getUser();
        //如果教材地址有变更，填写变更记录
        BdStudentModify studentModify = new BdStudentModify();
        studentModify.setLearnId(learnId);
        studentModify.setStdId(stdId);
        studentModify.setExt1(modifyText);
        studentModify.setIsComplete("1");
        studentModify.setModifyType(TransferConstants.MODIFY_TYPE_CHANGE_EXAMNO_10);
        if(StringUtil.hasValue(modifyText.toString())) {
            modifyService.addStudentModifyRecord(studentModify);
        }
    }

    public boolean isShowPassword(BaseUser user,String learnId,String empId){
        if(user.getUserLevel().equals("1")){
            return true;
        }
        int count = studentSceneConfirmMapper.countMyAreaStudent(learnId, user.getEmpId());
        if(count>0){
            return true;
        }
        if(user.getJtList().contains("XJ") || user.getJtList().contains("XJZL")){
            int empCount = studentSceneConfirmMapper.countLeaveMyAreaStudent(learnId,empId);
            if(empCount>0){
                return true;
            }
        }
        return false;
    }
}
