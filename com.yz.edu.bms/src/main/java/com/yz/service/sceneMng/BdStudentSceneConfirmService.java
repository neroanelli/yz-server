package com.yz.service.sceneMng;

import com.yz.constants.TransferConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.refund.UsInfoMapper;
import com.yz.dao.sceneMng.BdStudentSceneConfirmMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.WechatMsgVo;
import com.yz.model.admin.BaseUser;
import com.yz.model.exam.BdStudentSceneRegister;
import com.yz.model.exam.YzNetworkExamFrm;
import com.yz.model.recruit.StudentSceneConfirmInfo;
import com.yz.model.recruit.StudentSceneConfirmQuery;
import com.yz.model.stdService.StudentDegreeEnglishInfo;
import com.yz.model.transfer.BdStudentModify;
import com.yz.redis.RedisService;
import com.yz.service.transfer.BdStudentModifyService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;
import com.yz.util.ExcelUtil.IExcelConfig;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zlp
 * @version 1.0
 */
@Service
public class BdStudentSceneConfirmService {

    private static Logger logger = LoggerFactory.getLogger(BdStudentSceneConfirmService.class);

    @Autowired
    BdStudentSceneConfirmMapper studentSceneConfirmMapper;

    @Autowired
    private BdStudentModifyService modifyService;

    @Autowired
	private UsInfoMapper usInfoMapper;

    @Autowired
	private DictExchangeUtil dictExchangeUtil;
    /**
     * 我的学员现场确认查询
     * @param query
     * @return
     */
    public List<StudentSceneConfirmInfo> findAllSceneConfirm(StudentSceneConfirmQuery query) {
        List<StudentSceneConfirmInfo> result = studentSceneConfirmMapper.findAllSceneConfirm(query,getUser());
        return result;
    }

    public StudentSceneConfirmInfo getInfoById(String confirmId) {
        StudentSceneConfirmQuery query = new StudentSceneConfirmQuery();
        query.setConfirmId(confirmId);
        StudentSceneConfirmInfo studentSceneConfirmInfo;
        List<StudentSceneConfirmInfo> list = studentSceneConfirmMapper.findAllSceneConfirm(query, getUser());
        if (list.size() > 0) {
            studentSceneConfirmInfo = list.get(0);
            return studentSceneConfirmInfo; 
        }
        return null;
    }

    private BaseUser getUser(){
        BaseUser user = SessionUtil.getUser();
        // 校监，校监助理
        if (user.getJtList().contains("XCQR_ALL")) {
            user.setUserLevel("1");
        }
        return user;
    }

    public List<Map<String,String>> getSceneRegisterList(String learnId){
        List<Map<String,String>> list = studentSceneConfirmMapper.getSceneRegisterList(learnId);
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
                addModifyRecord(learnId, stdId, "添加考生号：" + examNo,TransferConstants.MODIFY_TYPE_CHANGE_EXAMNO_10);
            }
        }else{
            if(!examNo.equals(eNo)){
                addModifyRecord(learnId,stdId,"将考生号'"+eNo+"'变更为'"+examNo+"'",TransferConstants.MODIFY_TYPE_CHANGE_EXAMNO_10);
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
    public void addModifyRecord(String learnId,String stdId,String modifyText,String modifyType){
        //如果教材地址有变更，填写变更记录
        BdStudentModify studentModify = new BdStudentModify();
        studentModify.setLearnId(learnId);
        studentModify.setStdId(stdId);
        studentModify.setExt1(modifyText);
        studentModify.setIsComplete("1");
        studentModify.setModifyType(modifyType);
        if(StringUtil.hasValue(modifyText.toString())) {
            modifyService.addStudentModifyRecord(studentModify);
        }
    }
    
    /**
	 * 勾选后批量录取
	 * @param enroll
	 */
    @Transactional
	public void checkResetTask(String[] idArray) {
	    Arrays.asList(idArray).stream().forEach(v -> {
	    	String[] confirmInfo=v.split("##");
	    	if(!confirmInfo[2].equals("0")) {
	    		studentSceneConfirmMapper.resetSceneConfirmTask(confirmInfo[0],confirmInfo[1]);
	    		sendWechatMsg(confirmInfo[1]);
       	 	}
	    });			
	}
    /**
     * 筛选重置任务
     * @param query
     */
    @Transactional
   	public void queryResetTask(StudentSceneConfirmQuery query) {
         List<StudentSceneConfirmInfo> result = studentSceneConfirmMapper.findAllSceneConfirm(query,getUser());
         for (StudentSceneConfirmInfo confirmInfo:result){
        	 if(!query.getPlaceConfirmStatus().equals("0")) {
        		 studentSceneConfirmMapper.resetSceneConfirmTask(confirmInfo.getConfirmId(),confirmInfo.getLearnId());
        		 sendWechatMsg(confirmInfo.getLearnId());
        	 }
        	 
         }		
   	}
    
    @Transactional
   	public void insertRegisterNo(String learnId,String stdId,String username,String password) {
    	 String registerId=IDGenerator.generatorId();
    	 Map<String,String> map = new HashMap<>(4);
         map.put("registerId",registerId);
         map.put("learnId",learnId);
         map.put("username",username); 
         map.put("password",password); 
         studentSceneConfirmMapper.insertRegisterNo(map);	 	
         addModifyRecord(learnId,stdId,"添加预报名号'"+username+"'",TransferConstants.MODIFY_TYPE_CHANGE_sceneRegister_11);
   	}
    
    @Transactional
   	public void setAvailabeRegisterNo(String registerId,String learnId,String stdId,String username) {
         studentSceneConfirmMapper.setAvailabeRegisterNo(registerId,learnId);
         //重新设置账号为有效后修改跟进状态，重新抓取信息
        studentSceneConfirmMapper.updatePPMStatus(learnId);
        //查询预报名和密码
        BdStudentSceneRegister bdStudentSceneRegister = studentSceneConfirmMapper.getBdStudentSceneRegister(registerId);
        //更新yz_network_exam_frm，yz_network_exam_frm_attr
        YzNetworkExamFrm yzNetworkExamFrm = studentSceneConfirmMapper.getYzNetworkExamFrmByLeranId(learnId);
        if (yzNetworkExamFrm == null || yzNetworkExamFrm.getId() == null){
            yzNetworkExamFrm = new YzNetworkExamFrm();
            yzNetworkExamFrm.setFrmId(learnId);
            yzNetworkExamFrm.setFrmName("LoginNetWorkExamForm");//暂写死，不定义常量
            yzNetworkExamFrm.setFrmType("LoginNetWorkExamForm");//暂写死，不定义常量
            studentSceneConfirmMapper.insertYzNetworkExamFrm(yzNetworkExamFrm);
        }
        //更新预报名信息
        studentSceneConfirmMapper.insertYzNetworkExamFrmAttr(yzNetworkExamFrm.getId(),
                bdStudentSceneRegister.getUsername(),
                bdStudentSceneRegister.getPassword());
         addModifyRecord(learnId,stdId,"将预报名号'"+username+"'设置为有效网报信息",TransferConstants.MODIFY_TYPE_CHANGE_sceneRegister_11);
   	}
    
    public Map<String,String> getEaxmInfoByLearnId(String learnId){
    	return studentSceneConfirmMapper.getEaxmInfoByLearnId(learnId);
    }

    public void sendWechatMsg(String learnId) {
    	//发送微信通知
		String userOpenId = usInfoMapper.getOpenIdByLearnId(learnId);
		WechatMsgVo vo = new WechatMsgVo();
		vo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_TASK);
		vo.setTouser(userOpenId);
		vo.addData("now", DateUtil.getNowDateAndTime());
		vo.addData("first", "现场确认点预约通知");
		vo.addData("keyword1", "现场确认点重新预约");
		vo.addData("keyword2", "您好，由于您未能在预约的时间内前往现场确认点进行确认，您可重新进行预约，谢谢！");
		vo.addData("remark", "可前往“远智学堂”--“我的任务”进行再次预约。");
		vo.setIfUseTemplateUlr(false);
		RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(vo));
		
    }
    
    
    @SuppressWarnings("unchecked")
    public void importStuExamNo(MultipartFile stuExamNoImport) {
    	//对导入工具进行字段填充
        IExcelConfig<StudentSceneConfirmInfo> testExcelCofing = new IExcelConfig<StudentSceneConfirmInfo>();
        testExcelCofing.setSheetName("index").setType(StudentSceneConfirmInfo.class)           
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName")) 
                .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("考试县区", "taName"))
                .addTitle(new ExcelUtil.IExcelTitle("考生号", "examNo"));
        

        // 行数记录
        int index = 2;
        try {
            // 对文件进行分析转对象
            List<StudentSceneConfirmInfo> list = ExcelUtil.importWorkbook(stuExamNoImport.getInputStream(), testExcelCofing,
            		stuExamNoImport.getOriginalFilename());
            // 遍历插入
            for (StudentSceneConfirmInfo info : list) {
                
                if (!StringUtil.hasValue(info.getStdName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！姓名不能为空");
                }
                if (!StringUtil.hasValue(info.getIdCard())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！身份证号不能为空");
                }
                if (!StringUtil.hasValue(info.getGrade())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级不能为空");
                }
                if (!StringUtil.hasValue(info.getExamNo())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！考生号不能为空");
                }
                index++;

            }
//            List<Map<String, Object>> resultList = studentSceneConfirmMapper.getNonExistsStudent(list);
//            if (resultList.size() > 0) {
//                StringBuilder sb = new StringBuilder();
//                sb.append("核对以下学员不存在：<br/>");
//                for (Map<String, Object> map : resultList) {
//                    sb.append("身份证："+map.get("id_card") + "-姓名：" + map.get("std_name") +"-年级：" + map.get("grade")+"<br/>");
//                }
//                throw new IllegalArgumentException(sb.toString());
//            }
            
//            //批量检查现场确认状态
//            resultList= studentSceneConfirmMapper.checkStudentSceneConfirmStatus(list);
//            if (resultList.size() > 0) {
//                StringBuilder sb = new StringBuilder();
//                sb.append("核对以下学员现场确认状态为[未确认]，未确认学员不能导入考生号：<br/>");
//                for (Map<String, Object> map : resultList) {
//                    sb.append("身份证："+map.get("id_card") + "-姓名：" + map.get("std_name") +"-年级：" + map.get("grade")+"<br/>");
//                }
//                throw new IllegalArgumentException(sb.toString());
//            }
            BaseUser user = SessionUtil.getUser();
            studentSceneConfirmMapper.insertByExcel(list, user);
        } catch (IOException e) {
            throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
        }
    	
    	
    }
    
    /**
     * 修改网报信息状态
     * @param idCard
     * @param examPayStatus
     * @param webRegisterStatus
     */
    public void editConfirmStatus(String idCard,String examPayStatus,String webRegisterStatus){
    	if(!StringUtil.hasValue(idCard)){
    		 throw new IllegalArgumentException("请输入身份证号码");
    	}
    	studentSceneConfirmMapper.editConfirmStatus(idCard,examPayStatus,webRegisterStatus);
    }
    
    @SuppressWarnings("unchecked")
    public void exportConfirmStudent(StudentSceneConfirmQuery query, HttpServletResponse response){
    	// 对导出工具进行字段填充
        IExcelConfig<StudentSceneConfirmInfo> testExcelCofing = new IExcelConfig<StudentSceneConfirmInfo>();
        testExcelCofing.setSheetName("index").setType(StudentSceneConfirmInfo.class)
                .addTitle(new ExcelUtil.IExcelTitle("学员姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("确认城市", "confirmCity"))
                .addTitle(new ExcelUtil.IExcelTitle("确认点", "confirmName"))
                .addTitle(new ExcelUtil.IExcelTitle("确认日期", "startTime"))
                .addTitle(new ExcelUtil.IExcelTitle("确认时间段", "timeQuantum"))
                .addTitle(new ExcelUtil.IExcelTitle("考试县区", "taName"));
        

        List<StudentSceneConfirmInfo> list = studentSceneConfirmMapper.findAllSceneConfirm(query, getUser());
        for (StudentSceneConfirmInfo info : list) {

            if(info.getStartTime() != null && info.getEndTime() != null){
                info.setTimeQuantum(info.getStartTime().substring(11,16)+ "至" + info.getEndTime().substring(11,16));
            }
            if(info.getStartTime() != null){
                info.setStartTime(info.getStartTime().substring(0,10));
            }

            // 是否报名转换
			String valueTemple = dictExchangeUtil.getParamKey("grade", info.getGrade().trim());
			info.setGrade(valueTemple);
        }

        SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

        ServletOutputStream out = null;
        String filename="stuSceneConfirm"+"-"+query.getStartTime();
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="+new String(filename.getBytes("gb2312"), "iso8859-1")+ ".xlsx");       
            out = response.getOutputStream();
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
            	logger.error(e.getMessage());
            }
        }
    }
}
