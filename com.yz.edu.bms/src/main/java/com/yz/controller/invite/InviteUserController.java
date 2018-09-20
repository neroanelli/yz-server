package com.yz.controller.invite;


import com.yz.model.invite.UsStudentRemark;
import com.yz.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.model.condition.invite.InviteUserQuery;
import com.yz.model.finance.AtsAccount;
import com.yz.service.invite.InviteUserService;

import java.util.List;

@RequestMapping("/invite_user")
@Controller
public class InviteUserController {

	@Autowired
	private InviteUserService inviteUserService;

	@RequestMapping("/toList")
	@Rule("invite_user")
	public String toList() {
		return "/invite/user/user_list";
	}

	@RequestMapping("/getList")
	@Rule("invite_user")
	@ResponseBody
	public Object getList(InviteUserQuery queryInfo) {
		return inviteUserService.getList(queryInfo);
	}

	@RequestMapping("/showLog")
	//@Rule({ "invite_user:log", "invite_fans:log" })
	public String showLog(Model model, @RequestParam(value = "userId", required = true) String userId) {
		model.addAttribute("userId", userId);
		return "/invite/user/distribution_log";
	}

	@RequestMapping("/getLogs")
	//@Rule({ "invite_user:log", "invite_fans:log" })
	@ResponseBody
	public Object getLogs(@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "start", required = true) int start,
			@RequestParam(value = "length", required = true) int length) {
		return inviteUserService.getLogs(start, length, userId);
	}

	@RequestMapping("/toAccount")
	//@Rule({ "invite_user","invite_fans","invite_admin","invite_schoolSuper","invite_subFans"})
	public String toAccount(@RequestParam(value = "userId", required = true) String userId, Model model) {
		model.addAttribute("userId", userId);
		AtsAccount account = inviteUserService.getAccountInfo(userId);
		if (account != null) {
			model.addAttribute("zmAmount", account.getAccAmount());
		} else {
			model.addAttribute("zmAmount", "0.00");
		}
		return "/invite/user/account";
	}

	@RequestMapping("/getAccount")
	//@Rule({ "invite_user","invite_fans","invite_admin","invite_schoolSuper","invite_subFans" })
	@ResponseBody
	public Object getAccount(@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "start", required = true) int start,
			@RequestParam(value = "length", required = true) int length) {
		return inviteUserService.getAccount(start, length, userId);
	}
	/**
	 * 跳转到备注页面
	 * @param userId
	 * @param stdId
	 * @param model
	 * @return
	 */
	@RequestMapping("/toEditRemark")
	public String toEditRemark(String userId,String stdId, Model model) {
		
		String remark = inviteUserService.getUserOrStdRemark(userId, stdId);
		if(StringUtil.isNotBlank(remark)){
			model.addAttribute("remark",remark);
		}
		model.addAttribute("userId",userId);
		model.addAttribute("stdId",stdId);
		return "/invite/user/user_remark_edit";
	}

	/**
	 * 新增或者修改备注信息
	 * @param usStudentRemark
	 * @return
	 */
	@RequestMapping("/editRemark")
	@ResponseBody
	public Object editRemark(UsStudentRemark usStudentRemark) {
		inviteUserService.addOrUpdateRemark(usStudentRemark);
		return "success";
	}

	@RequestMapping("/toIntentionType")
	public String toIntentionType(String userId,String intentionType, Model model) {
		model.addAttribute("userId",userId);
		model.addAttribute("intentionType",intentionType);
		return "/invite/user/user_intention_type_edit";
	}

	@RequestMapping("/intentionType")
	@ResponseBody
	public Object intentionType(String userId,String intentionType) {
		inviteUserService.updateIntentionType(userId, intentionType);
		return "success";
	}
}
