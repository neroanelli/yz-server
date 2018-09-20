package com.yz.controller.graduate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.graduate.OaDiplomaTask;
import com.yz.service.graduate.OaDiplomaTaskService;
import com.yz.util.Assert;
import com.yz.util.ValidationUtil;

/**
 * 任务发布配置
 * @author Dell
 * @date 2018/06/20
 */
@Controller
@RequestMapping("/taskprovide")
public class OaDiplomaTaskController {
	
	@Autowired
	private OaDiplomaTaskService oaDiplomaTaskService;
	
	
	/**
	 * 跳转到 毕业任务发布列表页
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("taskprovide:query")
	public String toList() {
		return "graduate/diplomaTask/student_diplomaTask_list";
	}
	
	@RequestMapping(value = "/list")
	@ResponseBody
	@Rule("taskprovide:query")
	public Object getOaDiplomaTaskList(@RequestParam(name = "start", defaultValue = "0") int start,@RequestParam(name = "length", defaultValue = "10") int length,OaDiplomaTask query){
		return oaDiplomaTaskService.getOaDiplomaTask(start,length,query);
	}
	
	@RequestMapping("/getTaskName")
	@ResponseBody
	public Object getTaskName(){
		return oaDiplomaTaskService.getTaskName();
	}
	
	@RequestMapping("/editOrAdd")
	@Rule("taskprovide:update")
	public String editOrAdd(@RequestParam(name = "tjType", required = true) String tjType, HttpServletRequest request,
			Model model) {

		String[] words = { "diplomaId", "taskName", "warmTips", "warmReminder"};
		Map<String, Object> oaDiplomaTask = new HashMap<String, Object>();
		for (String word : words) {
			oaDiplomaTask.put(word, null);
		}

		if ("UPDATE".equals(tjType.trim().toUpperCase())) {
			String diplomaId = RequestUtil.getString("diplomaId");
			Assert.hasText(diplomaId, "参数名称不能为空");
			oaDiplomaTask = oaDiplomaTaskService.getOneOaDiplomaTask(diplomaId);
		}
		model.addAttribute("tjType", tjType);
		model.addAttribute("oaDiplomaTask", oaDiplomaTask);
		return "graduate/diplomaTask/student_diplomaTask_edit";
	}
	
	
	@RequestMapping("/deleteOaDiplomaTask")
	@ResponseBody
	@Rule("taskprovide:delete")
	public Object deleteOaDiplomaTask(@RequestParam(name = "diplomaId", required = true) String diplomaId) {
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(diplomaId);
		if(oaDiplomaTaskService.getOaDiplomaTaskConfigure(diplomaId) > 0){
			return "false";
		}
		oaDiplomaTaskService.deleteOaDiplomaTask(diplomaId);
		return "success";
	}
	
	
	@RequestMapping("/updateOaDiplomaTask")
	@ResponseBody
	@Rule("taskprovide:edit")
	public Object updateOaDiplomaTask(OaDiplomaTask query) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(query.getDiplomaId());
		BaseUser user = SessionUtil.getUser();
		query.setUpdateUser(user.getUserName());
		query.setUpdateUserId(user.getUserId());
		oaDiplomaTaskService.updateOaDiplomaTask(query);
		return "success";
	}
	
	@RequestMapping("/insertOaDiplomaTask")
	@ResponseBody
	@Rule("taskprovide:insert")
	public Object insertOaDiplomaTask(OaDiplomaTask query) {
		BaseUser user = SessionUtil.getUser();
		query.setCreateUser(user.getUserName());
		query.setCreateUserId(user.getUserId());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String  createTime = df.format(new Date());//获取获取当前系统时间
		query.setCreateTime(createTime);
		oaDiplomaTaskService.insert(query);
		return "success";
	}
	
}
