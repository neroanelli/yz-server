package com.yz.controller.stdService;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.edu.paging.bean.Page;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.stdService.StudentTaskFollowUp;
import com.yz.model.stdService.StudentTaskFollowUpQuery;
import com.yz.service.stdService.StudentTaskFollowUpService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 学服任务跟进
 * @author lx
 * @date 2017年8月16日 下午4:45:59
 */
@RequestMapping("/taskfollow")
@Controller
public class StudentTaskFollowUpController {
	
	@Autowired
	private StudentTaskFollowUpService taskFollowUpService;

	@RequestMapping("/toList")
	@Rule("taskfollow:query")
	public String toList(Model model) {
		return "/stdservice/taskfollow/student_taskfollow_list";
	}
	
	@RequestMapping("/getTaskList")
	@Rule("taskfollow:query")
	@ResponseBody
	public Object getStudyingList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,StudentTaskFollowUpQuery query) {
		BaseUser user = SessionUtil.getUser();

			if(user.getJtList() != null && user.getJtList().size()>0){
				if(user.getJtList().indexOf("GKXJ")!=-1&&user.getJtList().indexOf("CJXJ")!=-1){
					if(query.getRecruitType() == null || query.getRecruitType() == ""){
						query.setRecruitType("");
					}
				}else if(user.getJtList().indexOf("CJXJ")!=-1){
					if(query.getRecruitType() != null && query.getRecruitType() != ""){
						if(query.getRecruitType().equals("2")){
							query.setRecruitType("100");
						}
					}else{
						query.setRecruitType("1");
					}
					
				}else if(user.getJtList().indexOf("GKXJ")!=-1){                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
					
					if(query.getRecruitType() != null && query.getRecruitType() != ""){
						if(query.getRecruitType().equals("1")){
							query.setRecruitType("100");
						}
					}else{
						query.setRecruitType("2");
					}
				}
			}
		
		
		/*query.setTutorId(user.getEmpId());*/                                
		return taskFollowUpService.getStudentTaskFollowUp(start,length,query);
	}
	
	@RequestMapping("/finishTask")
	@Log(needLog = true)
	@ResponseBody
	@Rule("taskfollow:finish")
	public Object finishTask() {

		String taskId = RequestUtil.getString("taskId");
		String learnId = RequestUtil.getString("learnId");
		
		taskFollowUpService.finishTask(taskId,learnId);
	
		return "SUCCESS";
	}
	
	
	/**
	 * 备注添加
	 * @param userId
	 * @param stdId
	 * @param model
	 * @return
	 */
	@RequestMapping("/toEditRemark")
	public String toEditRemark(String taskId,String learnId, Model model) {
		String oldRemarks = "";
		if(taskFollowUpService.getStudentTaskFollowUpByTidAndlearnId(taskId,learnId) != null){
			oldRemarks = taskFollowUpService.getStudentTaskFollowUpByTidAndlearnId(taskId,learnId).getRemarks();
			model.addAttribute("remarksList",oldRemarks);
		}
		model.addAttribute("taskId",taskId);
		model.addAttribute("learnId",learnId);
		return "/stdservice/taskfollow/student_taskremark_edit";
	}
	
	
	@RequestMapping("/editTaskRemark")
	@ResponseBody
	public Object editTaskRemark() {
		String taskId = RequestUtil.getString("taskId");
		String learnId = RequestUtil.getString("learnId");
		String remarks = RequestUtil.getString("remarks");
		BaseUser user = SessionUtil.getUser();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String  updatetime = df.format(new Date());//获取获取当前系统时间
		int i = 1;
		String oldRemarks = "";
		if(taskFollowUpService.getStudentTaskFollowUpByTidAndlearnId(taskId,learnId) != null){
			oldRemarks = taskFollowUpService.getStudentTaskFollowUpByTidAndlearnId(taskId,learnId).getRemarks();
			JSONArray jsonArray = JSONArray.fromObject(oldRemarks);  
			
			JSONObject obj = (JSONObject) jsonArray.get(jsonArray.size()-1);    
			i  += Integer.parseInt(obj.getString("no"));
			oldRemarks = oldRemarks + ",";
			
			oldRemarks = oldRemarks.substring(0,oldRemarks.length() - 2) + ",";
		}else{
			oldRemarks = "[";
		}
		
		String newRemarks =  oldRemarks + "{no:\"" + i + "\",remarks:\""+ remarks +"\",updater:\""+ user.getUserName() +"\",updatetime:\""+ updatetime +"\"}]";
		taskFollowUpService.updateStudentTaskRemark(taskId,learnId,newRemarks);
		return "success";
	}
	
	@RequestMapping("/batchFinish")
	@Rule("taskfollow:batchFinish")
	@ResponseBody
	public Object batchFinish(@RequestParam(name = "taskIds", required = true) String taskIds) {
		
		//转换
		JSONArray jso = JSONArray.fromObject(taskIds);
		if(null != jso && jso.size() >0){
			for(int i =0;i<jso.size();i++){
				JSONObject obj = JSONObject.fromObject(jso.get(i));
				String taskId = obj.getString("taskId");
				String learnId = obj.getString("learnId");
				taskFollowUpService.finishTask(taskId,learnId);
			}
		}
		return "success";
	}
}
