package com.yz.service.recruit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.constants.WechatMsgConstants;
import com.yz.dao.recruit.StudentTaskMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.WechatMsgVo;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.educational.OaTaskInfoQuery;
import com.yz.model.condition.educational.OaTaskTargetStudentQuery;
import com.yz.model.educational.OaTaskInfo;
import com.yz.model.educational.OaTaskStudentInfo;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 招生管理-学员任务
 * @author lx
 * @date 2018年7月23日 下午4:37:31
 */
@Transactional
@Service
public class StudentTaskService {

	@Autowired
	private StudentTaskMapper studentTaskMapper;
	
	@Value("${zm.visitUrl}")
	private String visitUrl; //智米中心访问地址
	/**
	 * 任务列表
	 * @param start
	 * @param length
	 * @param infoQuery
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo selectStudentTaskInfo(int start, int length,OaTaskInfoQuery infoQuery) {
		PageHelper.offsetPage(start, length);
		List<OaTaskInfo> taskInfos = studentTaskMapper.selectStudentTaskInfo(infoQuery);
		if(null != taskInfos && taskInfos.size()>0){
			
			Date currentDate = new Date();
			taskInfos.stream().forEach(t->{
				Date startDate = DateUtil.convertDateStrToDate(t.getStartTime(), DateUtil.YYYYMMDDHHMMSS_SPLIT);
				Date endDate = DateUtil.convertDateStrToDate(t.getEndTime(), DateUtil.YYYYMMDDHHMMSS_SPLIT);
				String taskStatus = "0";
				if (currentDate.getTime() > startDate.getTime() && currentDate.getTime()<endDate.getTime()) {
					taskStatus = "1"; // 进行中
				}else if(currentDate.getTime() > endDate.getTime()){
					taskStatus = "2"; // 已结束
				}
				t.setTaskStatus(taskStatus);
			});
		}
		return new IPageInfo((Page) taskInfos);
	}
	/**
	 * 新增学员任务
	 * @param taskInfo
	 */
	public void insertTask(OaTaskInfo taskInfo){
		taskInfo.setTaskId(IDGenerator.generatorId());
		taskInfo.setModule("2");
		studentTaskMapper.insertTask(taskInfo);
	}
	/**
	 * 修改学员任务
	 * @param taskInfo
	 */
	public void updateTask(OaTaskInfo taskInfo){
		studentTaskMapper.updateTask(taskInfo);
	}
	/**
	 * 获取某个任务的详细
	 * @param taskId
	 * @return
	 */
	public OaTaskInfo getTaskInfoById(String taskId){
		OaTaskInfo taskInfo = studentTaskMapper.getTaskInfoById(taskId);
		if(null != taskInfo){
			Date startDate = DateUtil.convertDateStrToDate(taskInfo.getStartTime(), DateUtil.YYYYMMDDHHMMSS_SPLIT);
			Date endDate = DateUtil.convertDateStrToDate(taskInfo.getEndTime(), DateUtil.YYYYMMDDHHMMSS_SPLIT);
			String taskStatus = "0";
			Date currentDate = new Date();
			if (currentDate.getTime() > startDate.getTime() && currentDate.getTime()<endDate.getTime()) {
				taskStatus = "1"; // 进行中
			}else if(currentDate.getTime() > endDate.getTime()){
				taskStatus = "2"; // 已结束
			}
			taskInfo.setTaskStatus(taskStatus);
		}
		return taskInfo;
	}
	/**
	 * 查询学员任务的目标学员
	 * @param start
	 * @param length
	 * @param studentQuery
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IPageInfo queryTaskTargetStudentInfo(int start, int length,OaTaskTargetStudentQuery studentQuery) {
		PageHelper.offsetPage(start, length);
		String[] stdStageArray = studentQuery.getStdStage().split(",");
		studentQuery.setStdStageArray(stdStageArray);
		if(StringUtil.isEmpty(studentQuery.getIsChecked())){
			studentQuery.setIsChecked("2");
		}
		List<OaTaskStudentInfo> studentInfos = studentTaskMapper.queryTaskTargetStudentInfo(studentQuery);
		return new IPageInfo((Page) studentInfos);
	}
	/**
	 * 根据筛选条件添加全部
	 * @param studentQuery
	 */
	public void addAllStu(OaTaskTargetStudentQuery studentQuery){
		OaTaskInfo taskInfo = studentTaskMapper.getTaskInfoById(studentQuery.getTaskId());
		if (null != taskInfo) {
			String[] stdStageArray = studentQuery.getStdStage().split(",");
			studentQuery.setStdStageArray(stdStageArray);
			List<OaTaskStudentInfo> list = studentTaskMapper.queryTaskTargetStudentInfo(studentQuery);
			if(list.size() >0){
				studentTaskMapper.addStu(list, studentQuery.getTaskId(),studentQuery.getTaskType());
			}
		}
		
	}
	/**
	 * 根据条件全部删除
	 * @param studentQuery
	 */
	public void delAllStu(OaTaskTargetStudentQuery studentQuery){
		OaTaskInfo taskInfo = studentTaskMapper.getTaskInfoById(studentQuery.getTaskId());
		if (null != taskInfo) {
			String[] stdStageArray = studentQuery.getStdStage().split(",");
			studentQuery.setStdStageArray(stdStageArray);
			List<OaTaskStudentInfo> list = studentTaskMapper.queryTaskTargetStudentInfo(studentQuery);
			if(list.size() >0){
				studentTaskMapper.delAllStu(list,studentQuery.getTaskId());
			}
		}
	}
	/**
	 * 勾选添加目标学员
	 * @param idArray
	 * @param taskId
	 * @param taskType
	 */
	public void addStu(String[] idArray,String taskId,String taskType){
		List<OaTaskStudentInfo> list = new ArrayList<>();
		OaTaskInfo taskInfo = studentTaskMapper.getTaskInfoById(taskId);
		if (null != taskInfo && idArray.length > 0) {  //当任务存在时
			for (String str : idArray) {
				String[] splitStr = str.split(";");
				OaTaskStudentInfo studentInfo = new OaTaskStudentInfo();
				// 由于一个学业只能收到同一个类型任务一次
				studentInfo.setLearnId(splitStr[0]);
				studentInfo.setTutor(splitStr[1]);
				if (splitStr.length > 2) {
					if (StringUtil.isNotBlank(splitStr[2])) {
						studentInfo.setOpenId(splitStr[2]);
					}
				}
				list.add(studentInfo);
			}
			//主任务
			if(list.size() >0){
				studentTaskMapper.addStu(list, taskId,taskType);
			}
		}
	}
	/**
	 * 勾选删除目标学员
	 * @param idArray
	 * @param taskId
	 */
	public void delStu(String[] idArray,String taskId){
		OaTaskInfo taskInfo = studentTaskMapper.getTaskInfoById(taskId);
		if(null != taskInfo){
			studentTaskMapper.delStu(idArray,taskId);
		}
	}
	
	/**
	 * 查看目标学员
	 * @param start
	 * @param length
	 * @param studentQuery
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IPageInfo getTargetStu(int start, int length,OaTaskTargetStudentQuery studentQuery) {
		PageHelper.offsetPage(start, length);
		List<OaTaskStudentInfo> studentInfos = studentTaskMapper.getTargetStu(studentQuery);
		return new IPageInfo((Page) studentInfos);
	}
	
	/**
	 * 查询提醒未完成的学员
	 * @param studentQuery
	 */
	public void queryRemind(OaTaskTargetStudentQuery studentQuery){
		OaTaskInfo taskInfo = studentTaskMapper.getTaskInfoById(studentQuery.getTaskId());
		//强制默认提醒未完成的
		studentQuery.setIfFinish("0");
		List<OaTaskStudentInfo> studentInfos = studentTaskMapper.getTargetStu(studentQuery);
		if(null != studentInfos && studentInfos.size()>0){
			studentInfos.stream().forEach(s ->{
				if(StringUtil.isNotBlank(s.getOpenId())){
					//提醒
					WechatMsgVo msgVo = new WechatMsgVo();
					msgVo.setTouser(s.getOpenId());
					msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_TASK);
					msgVo.addData("now", DateUtil.getNowDateAndTime());
					msgVo.addData("first", "你有未完成的任务，请及时处理！");
					msgVo.addData("keyword1", taskInfo.getTaskTitle());
					msgVo.addData("keyword2", "学员任务");
					msgVo.addData("remark", "点击查看任务详情");
					msgVo.setIfUseTemplateUlr(false); //不使用系统模板,自己封装
					msgVo.setExt1(visitUrl+"student/mytask?learnId="+s.getLearnId());
					
					RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
				}
			});
		}
	}
	/**
	 * 勾选未完成的学员
	 * @param idArray
	 * @param taskId
	 */
	public void checkRemind(String[] idArray,String taskId){
		OaTaskInfo taskInfo = studentTaskMapper.getTaskInfoById(taskId);
		if (null != taskInfo && idArray.length > 0) {  //当任务存在时
			for (String str : idArray) {
				String[] splitStr = str.split(";");
				if (splitStr.length > 1) {
					if (StringUtil.isNotBlank(splitStr[1])) {
						//提醒
						WechatMsgVo msgVo = new WechatMsgVo();
						msgVo.setTouser(splitStr[1]);
						msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_TASK);
						msgVo.addData("now", DateUtil.getNowDateAndTime());
						msgVo.addData("first", "你有未完成的任务，请及时处理！");
						msgVo.addData("keyword1", taskInfo.getTaskTitle());
						msgVo.addData("keyword2", "学员任务");
						msgVo.addData("remark", "点击查看任务详情");
						msgVo.setIfUseTemplateUlr(false); //不使用系统模板,自己封装
						msgVo.setExt1(visitUrl+"student/mytask?learnId="+splitStr[0]);
						
						RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
					}
				}
			}
		}
	}
}
