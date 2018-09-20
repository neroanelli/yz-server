package com.yz.network.examination.service;

import com.yz.constants.TransferConstants;
import com.yz.generator.IDGenerator;
import com.yz.network.examination.croe.util.SessionUtil;
import com.yz.network.examination.dao.StudentConfirmSignMapper;
import com.yz.network.examination.model.BdConfirmStudentInfo;
import com.yz.network.examination.model.BdLearnQueueInfo;
import com.yz.network.examination.model.BdStudentModify;
import com.yz.network.examination.vo.LoginUser;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Description:
 * @Author: luxing
 * @Date 2018\8\8 0008 17:17
 **/
@Service
@Transactional
public class StudentConfirmSignService {
    private static final Logger log = LoggerFactory.getLogger(StudentConfirmSignService.class);

    @Autowired
    private StudentConfirmSignMapper studentConfirmSignMapper;

    @Autowired
    private QueueService queueService;


    /**
     * 登录签到系统
     * @param
     * @return
     */
    public Object loginSign(){
        LoginUser user = SessionUtil.getUser();
        HashMap hashMap = new HashMap();
        hashMap.put("userName",user.getUserName());
        hashMap.put("cityName",user.getSignCity());
        hashMap.put("signCityCode",user.getSignCityCode());
        //通过排序表查询该账号的总签到人数和当天签到人数
        hashMap.put("totalCount",studentConfirmSignMapper.getTotalCount(user.getUserId()));
        hashMap.put("todayCount",studentConfirmSignMapper.getTodayCount(user.getUserId()));
        return hashMap;
    }
    
    
    /**
     * 刷身份证签到
     * @param searchInfo
     * @return
     */
    public Object brushCard(String searchInfo){
        LoginUser user = SessionUtil.getUser();
        //首先根据身份证号码查询报读信息,没有则提示未发现报读信息，请先进行报读
        List<BdConfirmStudentInfo> list = studentConfirmSignMapper.getConfirmInfo(searchInfo,null);
        if(list==null || list.size()<1){
            throw new IllegalArgumentException("未发现报读信息，请先进行报读！");
        }
        BdConfirmStudentInfo bdConfirmStudentInfo = list.get(0);
        //判断是否是本签到地点的学员，不是则提示不是本考试县区的学员
        if(!user.getSignCityCode().equals(bdConfirmStudentInfo.getCityCode())){
            throw new IllegalArgumentException("您的考试县区为"+bdConfirmStudentInfo.getTaName()+",与签到地点不一致！");
        }

        //判断是否已网报，没有则提示尚未网报，请先进行网报
        if(bdConfirmStudentInfo.getWebRegisterStatus()==null || "0".equals(bdConfirmStudentInfo.getWebRegisterStatus())){
            throw new IllegalArgumentException("尚未网报,请先进行网报！");
        }
        //判断是否已缴费，没有则提示尚未缴费，请先缴纳考试费
        if(bdConfirmStudentInfo.getExamPayStatus()==null || "0".equals(bdConfirmStudentInfo.getExamPayStatus())){
            throw new IllegalArgumentException("尚未缴费,请先缴纳考试费！");
        }
        //调用生成排序号，打印方法(返回排序号码)
        Map<String,String> map = printInfo(bdConfirmStudentInfo,user.getUserName(),user.getUserId());
        if(map!=null){
            bdConfirmStudentInfo.setQueueType(map.get("queueType"));
            bdConfirmStudentInfo.setNo(map.get("no"));
            bdConfirmStudentInfo.setWaitNum(map.get("waitNum"));
            bdConfirmStudentInfo.setSignTime(map.get("signTime"));
        }
        bdConfirmStudentInfo.setSignStatus(1);
        bdConfirmStudentInfo.setTotalCount(studentConfirmSignMapper.getTotalCount(user.getUserId()));
        bdConfirmStudentInfo.setTodayCount(studentConfirmSignMapper.getTodayCount(user.getUserId()));
        log.info("------------------------------刷身份证签到返回数据：" + JsonUtil.object2String(bdConfirmStudentInfo));
        return bdConfirmStudentInfo;
    }

    /**
     * 输入身份证查询报考信息
     * @param searchInfo
     * @return
     */
    public Object inputCard(String searchInfo){
        //首先根据身份证号码查询报读信息,没有则提示未发现报读信息，请先进行报读
        HashMap hashMap = new HashMap();
        LoginUser user = SessionUtil.getUser();
        hashMap.put("totalCount",studentConfirmSignMapper.getTotalCount(user.getUserId()));
        hashMap.put("todayCount",studentConfirmSignMapper.getTodayCount(user.getUserId()));
        List<BdConfirmStudentInfo> list = studentConfirmSignMapper.getConfirmInfo(searchInfo,null);
        if(list==null || list.size()<1){
            throw new IllegalArgumentException("未发现报读信息，请先进行报读！");
        }
        hashMap.put("list",list);
        return hashMap;
    }

    /**
     * 签到确认，或者更新考生号
     * @param bds
     */
    public Object confirmSign(BdConfirmStudentInfo bds){
        LoginUser user = SessionUtil.getUser();
        BdConfirmStudentInfo  bdInfo = studentConfirmSignMapper.existExamNo(bds.getConfirmId());
        bds.setIdCard(bdInfo.getIdCard());
        bds.setLearnId(bdInfo.getLearnId());
        bds.setCityCode(user.getSignCityCode());
        bds.setPfsnLevel(bdInfo.getPfsnLevel());
        bds.setStdName(bdInfo.getStdName());
        bds.setStartTime(bdInfo.getStartTime());
        //首先拿到签到状态和考生号
        String signStatus = "0";
        if(bdInfo.getSignStatus()!=null){
            signStatus = bdInfo.getSignStatus().toString();
        }
        String examNo = bdInfo.getExamNo();
        String learnId = bdInfo.getLearnId();
        String stdId = bdInfo.getStdId();
        //更新考生号
        if(examNo==null){
        	//学员没有考生号，现在输入考生号 添加
            if(StringUtil.hasValue(bds.getExamNo())) {
            	studentConfirmSignMapper.insertExamNo(learnId, stdId, bds.getExamNo());
                //添加学员信息变更记录
                addModifyRecord(learnId, stdId, "添加考生号：" + bds.getExamNo(),user.getUserId(),user.getUserName());
            }
        }else{
        	//学员有考生号与现在输入的考生号不一致 修改
            if(bds.getExamNo()==null || !bds.getExamNo().equals(examNo)){
                //添加学员信息变更记录
                addModifyRecord(learnId, stdId,"将考生号'"+examNo+"'变更为'"+bds.getExamNo()+"'",user.getUserId(),user.getUserName());
                studentConfirmSignMapper.updateExamNo(learnId,bds.getExamNo());
            }
        }
        Map<String,String> maps = new HashMap<>();
      //需要打印
        if(signStatus.equals("0") && bds.getSignStatus()==1){
        	maps = printInfo(bds,user.getUserName(),user.getUserId());
        }else{
            //如果传过来的是0：未签到 则废弃排序号
            if(bds.getSignStatus()==0 && learnId!=null){
                studentConfirmSignMapper.discardNum(learnId);
            }
            String signTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
            //bds.setSignTime(signTime);
        	bds.setUpdateUser(user.getUserName());
            bds.setUpdateUserId(user.getUserId());
            bds.setSignUserId(user.getUserId());
            studentConfirmSignMapper.confirmSign(bds);
        }
        log.info("------------------------------手动签到返回数据：" + JsonUtil.object2String(maps));
        return maps;
    }

    /**
     * 生成排序号，打印方法
     * @param userName
     * @param userId
     */
    public Map<String, String> printInfo(BdConfirmStudentInfo bdConfirmStudentInfo, String userName, String userId){
        //生成排序号则表示签到成功，更新签到状态
        BdConfirmStudentInfo bds = new BdConfirmStudentInfo();
        bds.setSignStatus(1);
        String signTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
        bds.setSignTime(signTime);
        bds.setUpdateUserId(userId);
        bds.setSignUserId(userId);
        bds.setUpdateUser(userName);
        bds.setConfirmId(bdConfirmStudentInfo.getConfirmId());
        bds.setWorkProve(bdConfirmStudentInfo.getWorkProve());
        bds.setSceneRemark(bdConfirmStudentInfo.getSceneRemark());
        bds.setExamNo(bdConfirmStudentInfo.getExamNo());
        studentConfirmSignMapper.confirmSign(bds);
        //调用生成排序号方法，打印,如果没有生产排序号则提示，排序号生成失败！
        BdLearnQueueInfo bdLearnQueueInfo = new BdLearnQueueInfo();
        bdLearnQueueInfo.setPlaceConfirmTime(bdConfirmStudentInfo.getStartTime());
        bdLearnQueueInfo.setPfsnLevel(bdConfirmStudentInfo.getPfsnLevel());
        bdLearnQueueInfo.setCityCode(bdConfirmStudentInfo.getCityCode());
        bdLearnQueueInfo.setIdCard(bdConfirmStudentInfo.getIdCard());
        bdLearnQueueInfo.setSignUserId(userId);
        bdLearnQueueInfo.setSignTime(DateUtil.convertDateStrToDate(signTime, "yyyy-MM-dd HH:mm:ss"));
        bdLearnQueueInfo.setLearnId(bdConfirmStudentInfo.getLearnId());
        bdLearnQueueInfo.setStdName(bdConfirmStudentInfo.getStdName());
        Map<String,String> map = new HashMap<>();
        map = queueService.addQueue(bdLearnQueueInfo);
        map.put("signTime",signTime);
        return map;
    }
    
    /**
     * 添加考生号变更记录
     * @param learnId
     * @param stdId
     * @param modifyText
     */
    public void addModifyRecord(String learnId,String stdId,String modifyText,String userId,String userName){
        //如果教材地址有变更，填写变更记录
        BdStudentModify studentModify = new BdStudentModify();
        studentModify.setLearnId(learnId);
        studentModify.setStdId(stdId);
        studentModify.setExt1(modifyText);
        studentModify.setIsComplete("1");
        studentModify.setModifyType(TransferConstants.MODIFY_TYPE_CHANGE_EXAMNO_10);
        studentModify.setModifyId(IDGenerator.generatorId());
        studentModify.setCreateUser(userName);
		studentModify.setCreateUserId(userId);
		studentConfirmSignMapper.insertSelectiveBdStudentModify(studentModify);
    }

}
