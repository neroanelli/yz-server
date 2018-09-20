package com.yz.controller.recruit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.condition.educational.OaTaskInfoQuery;
import com.yz.model.condition.educational.OaTaskTargetStudentQuery;
import com.yz.model.educational.OaTaskInfo;
import com.yz.service.recruit.StudentTaskService;
import com.yz.util.Assert;

/**
 * 招生管理-学员任务
 * @author lx
 * @date 2018年7月23日 下午3:31:10
 */
@Controller
@RequestMapping("/studentTask")
public class StudentTaskController {

	@Autowired
	private StudentTaskService studentTaskService;
	/**
	 * 跳转到学员任务页面列表
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("studentTask:query")
	public String toList() {
		return "/recruit/task/task_list";
	}
	/**
	 * 获取学员任务数据
	 * @param start
	 * @param length
	 * @param infoQuery
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Rule("studentTask:query")
	@ResponseBody
	public Object taskList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,OaTaskInfoQuery infoQuery) {
		BaseUser user = SessionUtil.getUser();
		infoQuery.setIssuer(user.getUserId());
	    return studentTaskService.selectStudentTaskInfo(start,length,infoQuery);
	}
	
	/**
	 * 跳转编辑或者新增学员任务
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toEdit")
	@Token(action = Flag.Save, groupId = "stutask:edit")
	@Rule("studentTask:edit")
	public String toEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,Model model) {
		OaTaskInfo taskInfo =new OaTaskInfo();
		if ("UPDATE".equals(exType.trim().toUpperCase()) || "LOOK".equals(exType.trim().toUpperCase())) {
			String taskId = RequestUtil.getString("taskId");
			Assert.hasText(taskId, "参数名称不能为空");
			taskInfo = studentTaskService.getTaskInfoById(taskId);
		}
		model.addAttribute("taskInfo", taskInfo);
		model.addAttribute("exType", exType);
		return "/recruit/task/task_edit";
	}
	/**
	 * 编辑或者新增学员任务
	 * @param exType
	 * @param taskInfo
	 * @return
	 */
	@RequestMapping("/taskUpdate")
	@Rule({ "studentTask:edit" })
	@Token(action = Flag.Remove, groupId = "stutask:edit")
	@ResponseBody
	@Log
	public Object taskUpdate(@RequestParam(name = "exType", required = true) String exType, OaTaskInfo taskInfo) {
		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();
		if ("UPDATE".equalsIgnoreCase(deal)) {
			taskInfo.setUpdateUserId(user.getUserId());
			taskInfo.setUpdateUser(user.getRealName());
			studentTaskService.updateTask(taskInfo);
			//清除缓存
		} else if ("ADD".equalsIgnoreCase(exType)) {
			taskInfo.setCreateUserId(user.getUserId());
			taskInfo.setCreateUser(user.getRealName());
			studentTaskService.insertTask(taskInfo);
		}

		return "success";
	}
	/**
	 * 停用或者启用学员任务
	 * @param taskId
	 * @param taskStatus
	 * @return
	 */
	@RequestMapping("/startOrStop")
	@Rule({ "studentTask:edit" })
	@ResponseBody
	@Log
	public Object startOrStop(String taskId,String taskStatus) {
		OaTaskInfo taskInfo = new OaTaskInfo();
		BaseUser user = SessionUtil.getUser();
		
		taskInfo.setUpdateUserId(user.getUserId());
		taskInfo.setUpdateUser(user.getRealName());
		taskInfo.setTaskId(taskId);
		taskInfo.setIsAllow(taskStatus);
		
		studentTaskService.updateTask(taskInfo);
		return "success";
	}
	
	/**
	 * 调整到选择学员信息界面
	 * @param taskId
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toStu")
	@Rule("studentTask:choice")
	public String toStu(@RequestParam(name = "taskId", required = true) String taskId,
			@RequestParam(name = "taskType", required = true) String taskType,
			@RequestParam(name = "exType", required = true) String exType, 
			HttpServletRequest request, Model model) {
		model.addAttribute("taskId", taskId);
		model.addAttribute("exType", exType);
		model.addAttribute("taskType", taskType);
		return "/recruit/task/stu_list";
	}
	/**
	 * 获取学员列表
	 * @param start
	 * @param length
	 * @param studentQuery
	 * @return
	 */
	@RequestMapping(value = "/stuList", method = RequestMethod.GET)
	@Rule("studentTask:choice")
	@ResponseBody
	public Object stuList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,OaTaskTargetStudentQuery studentQuery) {
		return studentTaskService.queryTaskTargetStudentInfo(start, length,studentQuery);
	}
	/**
	 * 根据条件添加全部
	 * @param studentQuery
	 * @return
	 */
	@RequestMapping("/addAllStu")
	@Rule("studentTask:choice")
	@ResponseBody
	public Object addAllStu(OaTaskTargetStudentQuery studentQuery) {
		Assert.hasText(studentQuery.getTaskId(), "参数名称不能为空");
		studentTaskService.addAllStu(studentQuery);
		return "success";
	}
	/**
	 * 根据条件删除全部
	 * @param studentQuery
	 * @return
	 */
	@RequestMapping("/delAllStu")
	@Rule("studentTask:choice")
	@ResponseBody
	public Object delAllStu(OaTaskTargetStudentQuery studentQuery) {
		Assert.hasText(studentQuery.getTaskId(), "参数名称不能为空");
		studentTaskService.delAllStu(studentQuery);
		return "success";
	}
	
	/**
	 * 分配学员
	 * @param idArray
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/addStu")
	@Rule("studentTask:choice")
	@ResponseBody
	public Object addStu(@RequestParam(name = "idArray[]", required = true) String[] idArray,
			             @RequestParam(name = "taskId", required = true) String taskId,
			             @RequestParam(name = "taskType", required = true) String taskType) {
		studentTaskService.addStu(idArray,taskId,taskType);
		return "success";
	}
	/**
	 * 删除已分配的学员
	 * @param idArray
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/delStu")
	@Rule("studentTask:choice")
	@ResponseBody
	public Object delStu(@RequestParam(name = "idArray[]", required = true) String[] idArray,
			             @RequestParam(name = "taskId", required = true) String taskId) {
		studentTaskService.delStu(idArray,taskId);
		return "success";
	}
	
	/**
	 * 跳转到目标学员
	 * @return
	 */
	@RequestMapping("/toTargetStu")
	@Rule("studentTask:look")
	public String toTargetStu(@RequestParam(name = "taskId", required = true) String taskId,
			@RequestParam(name = "taskStatus", required = true) String taskStatus,
			HttpServletRequest request, Model model) {
		Assert.hasText(taskId, "参数名称不能为空");
		model.addAttribute("taskId", taskId);
		model.addAttribute("taskStatus", taskStatus);
		return "/recruit/task/target_list";
	}
	/**
	 * 获取目标学员信息
	 * @param start
	 * @param length
	 * @param studentQuery
	 * @return
	 */
	@RequestMapping(value = "/getTargetStu", method = RequestMethod.GET)
	@Rule("studentTask:look")
	@ResponseBody
	public Object getTargetStu(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,OaTaskTargetStudentQuery studentQuery) {
		return studentTaskService.getTargetStu(start, length,studentQuery);
	}
	
	/**
	 * 筛选查询提醒
	 * @param studentQuery
	 * @return
	 */
	@RequestMapping("/queryRemind")
	@Rule("studentTask:look")
	@ResponseBody
	public Object queryRemind(OaTaskTargetStudentQuery studentQuery) {
		Assert.hasText(studentQuery.getTaskId(), "参数名称不能为空");
		studentTaskService.queryRemind(studentQuery);
		return "success";
	}
	
	/**
	 * 勾选提醒
	 * @param idArray
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/checkRemind")
	@Rule("studentTask:choice")
	@ResponseBody
	public Object checkRemind(@RequestParam(name = "idArray[]", required = true) String[] idArray,
			             @RequestParam(name = "taskId", required = true) String taskId) {
		studentTaskService.checkRemind(idArray,taskId);
		return "success";
	}
}
