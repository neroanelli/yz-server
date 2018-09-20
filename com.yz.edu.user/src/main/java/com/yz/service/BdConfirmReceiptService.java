package com.yz.service;

import com.yz.constants.TransferConstants;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.BdConfirmReceiptMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.BdConfirmReceipt;
import com.yz.model.student.BdStudentModify;
import com.yz.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @Description:
 * @Author: luxing
 * @Date 2018\8\17 0017 16:10
 **/
@Service
public class BdConfirmReceiptService {
    private static final Logger log = LoggerFactory.getLogger(BdConfirmReceiptService.class);

    @Autowired
    private BdConfirmReceiptMapper bdConfirmReceiptMapper;
    @Autowired
    private BdStudentOutService studentOuntService;

    public Object getReceiptInfo(String learnId){
        return bdConfirmReceiptMapper.getReceiptInfo(learnId);
    }

    public Object uploadReceipt(String learnId,String stdId,String tempUrl,String examNo,String userId,String userName,String isUpdate){
        //首先查询回执信息，然后判断是否已经有值，有值则不能修改
        BdConfirmReceipt bcf =  bdConfirmReceiptMapper.getReceiptInfo(learnId);
        //上传附件
        if("1".equals(isUpdate)){//更改附件路径
            BdConfirmReceipt bdConfirmReceipt = new BdConfirmReceipt();
            try {
                StringBuffer s = new StringBuffer(FileSrcUtil.Type.STUDENT.get()).append("/");
                s.append(stdId).append("/");
                s.append(tempUrl);
                String url = s.toString();
                FileUploadUtil.copyToDisplay(tempUrl, url);
                //添加附件更新信息
                //上传回执，默认设置20
                bdConfirmReceipt.setAnnexType("20");
                bdConfirmReceipt.setLearnId(learnId);
                bdConfirmReceipt.setAnnexName("报名回执");
                bdConfirmReceipt.setAnnexUrl(url);
                bdConfirmReceipt.setAnnexStatus("3");//状态为已审核
                bdConfirmReceipt.setIsRequire("1");//是否必传 0-否 1-是
                bdConfirmReceipt.setUpdateTime(new Date());
                bdConfirmReceipt.setUpdateUser(userName);
                bdConfirmReceipt.setUpdateUserId(userId);
                bdConfirmReceipt.setUploadUser(userName);
                bdConfirmReceipt.setUploadUserId(userId);
                bdConfirmReceipt.setUploadTime(new Date());
                //已经上传了回执附件,则更新
                if(StringUtil.hasValue(bcf.getAnnexId())){
                    bdConfirmReceipt.setAnnexId(bcf.getAnnexId());
                    bdConfirmReceiptMapper.updateReceiptFile(bdConfirmReceipt);
                }else{
                    bdConfirmReceipt.setAnnexId(IDGenerator.generatorId());
                    bdConfirmReceiptMapper.insertReceiptFile(bdConfirmReceipt);
                }
                addModifyRecord(learnId, stdId,"上传了报名回执",userId,userName,TransferConstants.MODIFY_TYPE_CHANGE_normalopt_5);
            } catch (Exception e) {
                log.error("--------------- 上传文件失败");
                throw new BusinessException("E000118"); // 文件上传失败；
            }
        }

        if(!StringUtil.hasValue(bcf.getExamNo())){
            //添加考生号
            if(StringUtil.hasValue(examNo)){
                bdConfirmReceiptMapper.insertExamNo(learnId,stdId, examNo,"1");
                addModifyRecord(learnId, stdId, "添加考生号：" + examNo,userId,userName,TransferConstants.MODIFY_TYPE_CHANGE_EXAMNO_10);
            }
        }else{
            //更改考生号
            if(!bcf.getExamNo().equals(examNo)){
                bdConfirmReceiptMapper.updateExamNo(learnId,examNo,"1");
                //添加学员信息变更记录
                addModifyRecord(learnId, stdId,"将考生号'"+bcf.getExamNo()+"'变更为'"+examNo+"'",userId,userName,TransferConstants.MODIFY_TYPE_CHANGE_EXAMNO_10);
            }
        }
        //更新学员修改数量
        bdConfirmReceiptMapper.updateNum(learnId,"1");
        return "success";
    }

    /**
     * 添加考生号变更记录
     * @param learnId
     * @param stdId
     * @param modifyText
     */
    public void addModifyRecord(String learnId,String stdId,String modifyText,String userId,String userName,String modifyType) {
        //如果教材地址有变更，填写变更记录
        BdStudentModify studentModify = new BdStudentModify();
        studentModify.setLearnId(learnId);
        studentModify.setStdId(stdId);
        studentModify.setExt1(modifyText);
        studentModify.setIsComplete("1");
        studentModify.setModifyType(modifyType);
        studentModify.setModifyId(IDGenerator.generatorId());
        studentOuntService.addStudentModifyRecord(studentModify, userName, userId);
    }
}
