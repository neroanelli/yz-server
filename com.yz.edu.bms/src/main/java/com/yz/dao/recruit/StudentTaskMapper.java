package com.yz.dao.recruit;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.condition.educational.OaTaskInfoQuery;
import com.yz.model.condition.educational.OaTaskTargetStudentQuery;
import com.yz.model.educational.OaTaskInfo;
import com.yz.model.educational.OaTaskStudentInfo;

/**
 * 招生管理-学员任务
 * @author lx
 * @date 2018年7月23日 下午4:39:13
 */
public interface StudentTaskMapper {

	//所有的学员任务
	public List<OaTaskInfo> selectStudentTaskInfo(OaTaskInfoQuery infoQuery);
	
	//新增学员任务
	public void insertTask(OaTaskInfo taskInfo);
	
	//修改学员任务
	public void updateTask(OaTaskInfo taskInfo);
	
	//某个具体的任务信息
	public OaTaskInfo getTaskInfoById(@Param("taskId") String taskId);
	
	//查询要选择的学员
	public List<OaTaskStudentInfo> queryTaskTargetStudentInfo(OaTaskTargetStudentQuery studentQuery);
	
	//添加目标学员
	public void addStu(@Param("list") List<OaTaskStudentInfo> list,@Param("taskId") String taskId,@Param("taskType") String taskType);
	
	//删除目标学员
	public void delStu(@Param("ids") String[] ids,@Param("taskId") String taskId);
	
	//批量删除目标学员
	public void delAllStu(@Param("list") List<OaTaskStudentInfo> list,@Param("taskId") String taskId);
	
	//查找已经选择的目标学员
	public List<OaTaskStudentInfo> getTargetStu(OaTaskTargetStudentQuery studentQuery);
	
}
