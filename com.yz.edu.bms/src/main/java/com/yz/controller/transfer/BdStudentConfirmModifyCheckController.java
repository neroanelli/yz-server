package com.yz.controller.transfer;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.edu.paging.bean.Page;
import com.yz.model.common.IPageInfo;
import com.yz.model.transfer.BdStudentConfirmModify;
import com.yz.model.transfer.BdStudentModify;
import com.yz.model.transfer.StudentModifyMap;
import com.yz.service.transfer.BdCheckRecordService;
import com.yz.service.transfer.BdStudentConfirmCheckService;
import com.yz.service.transfer.BdStudentConfirmModifyService;
import com.yz.util.Assert;
import com.yz.util.StringUtil;

/**
 * 现场确认异动
 */
@Controller
@RequestMapping("/confirmModifyCheck")
public class BdStudentConfirmModifyCheckController {

	private static final Logger log = LoggerFactory.getLogger(BdStudentConfirmModifyCheckController.class);
	@Autowired
	private BdStudentConfirmModifyService sms;
	@Autowired
	private BdStudentConfirmCheckService studentCheckService;

	@RequestMapping("/list")
	@Rule("confirmModifyCheck:toList")
	public String showList(HttpServletRequest request) {
		return "transfer/confirmModify/student-confirm-check-list";
	}

    @RequestMapping("/findStudentModifyCheck")
	@ResponseBody
	@Rule("confirmModifyCheck:find")
	public Object findAllStudentCheck(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, StudentModifyMap studentModifyMap) {
		List<Map<String, String>> resultList = studentCheckService.findStudentCheckModify(studentModifyMap, start, length);
		return new IPageInfo((Page) resultList);
	}
    
    @RequestMapping("/editToCheck")
    @Rule("confirmModifyCheck:toCheck")
	//@Token(action = Flag.Save, groupId = "confirmModifyCheck:passAudit")
	public String editToAudit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
    	String modifyId = RequestUtil.getString("modifyId");
		Assert.hasText(modifyId, "参数名称不能为空");
		BdStudentConfirmModify studentModify = studentCheckService.findStudentModifyById(modifyId);
		/*if(StringUtil.hasValue(studentModify.getGraduateTime())){
			studentModify.setGraduateTime(studentModify.getGraduateTime().substring(0,10));
		}
		if(StringUtil.hasValue(studentModify.getNewGraduateTime())){
			studentModify.setNewGraduateTime(studentModify.getNewGraduateTime().substring(0,10));
		}*/
		//查询审核记录s
        List<Map<String, String>> records = studentCheckService.findStudentModifyByModifyId(modifyId);
        model.addAttribute("records",records);
		model.addAttribute("exType", exType);
		model.addAttribute("studentModifyInfo", studentModify);
		return "transfer/confirmModify/student-confirm-check-edit";
	}
    
    
    @RequestMapping("/passStudentModifyCheck")
	@ResponseBody
	@Rule("confirmModifyCheck:check")
	public Object passStudentModifyCheck() {
    	String modifyId = RequestUtil.getString("modifyId");
    	Assert.hasText(modifyId, "参数名称不能为空");
    	String checkStatus = RequestUtil.getString("checkStatus");
    	Assert.hasText(checkStatus, "参数名称不能为空");
    	String remark = RequestUtil.getString("remark");
    	String reason = RequestUtil.getString("reason");
		studentCheckService.passStudentModifyCheck(modifyId, checkStatus, remark,reason);
		return "success";
	}

    
    @RequestMapping("/checkModifyBatch")
	@ResponseBody
	@Rule("confirmModifyCheck:passAudit")
	public Object checkModifyBatch(@RequestParam(name = "modifyIds[]", required = true) String[] modifyIds) {

    	studentCheckService.passModifyBatch(modifyIds);

		return "SUCCESS";
	}
}
