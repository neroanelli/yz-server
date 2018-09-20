package com.yz.controller.sceneMng;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.constants.CustomizeAttrConstants;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.BmsUser;
import com.yz.model.admin.BmsUserResponse;
import com.yz.service.SysDictService;
import com.yz.service.common.CustomizeAttrService;
import com.yz.service.sceneMng.NetToAccountService;
import com.yz.util.Assert;
import com.yz.util.StringUtil;

/**
 * 网报账号管理
 * @author lx
 * @date 2018年7月30日 上午11:02:20
 */
@RequestMapping("/netToAccount")
@Controller
public class NetToAccountController {

	@Autowired
	private NetToAccountService netToAccountService;
	
	@Autowired
	private CustomizeAttrService customizeAttrService;
	
	@Autowired
	private SysDictService sysDictService;
	
	/**
	 * 跳转页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/toList")
    @Rule("netToAccount:query")
    public String showList(HttpServletRequest request) {
        return "sceneMng/netToAccount_list";
    }
	
	/**
	 * 查询列表
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	@Rule("netToAccount:query")
	public Object queryUserList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BmsUser user) {
		return netToAccountService.queryUserList(start, length, user);
	}
	
	/**
	 * 网报账号编辑
	 * @param exType
	 * @param userId
	 * @param model
	 * @return
	 */
	@RequestMapping("/userEdit")
	@Token(action = Flag.Save, groupId = "netUser:edit")
	@Rule("netToAccount:update")
	@Log
	public Object userEdit(@RequestParam(name = "exType", required = true) String exType, String userId, Model model) {
		BmsUserResponse user = new BmsUserResponse();
		if ("UPDATE".equalsIgnoreCase(exType)) {
			Assert.hasText(userId, "用户ID不能为空");
			user = netToAccountService.queryUserById(userId);
			user.setAttrList(customizeAttrService.getCustomizeAttrList(userId, CustomizeAttrConstants.DEF_CATALOG_NET_WORK_EXAM));
		}else{
			user.setAttrList(customizeAttrService.getCustomizeAttrList(userId, CustomizeAttrConstants.DEF_CATALOG_NET_WORK_EXAM));
		}
	
		model.addAttribute("userInfo", user);
		model.addAttribute("exType", exType);
		return "sceneMng/netToAccount_add";
	}
	/**
	 * 修改和新增账号
	 * @param exType
	 * @param user
	 * @return
	 */
	@RequestMapping("/userUpdate")
	@ResponseBody
	@Token(action = Flag.Remove, groupId = "netUser:edit")
	@Log
	@Rule("netToAccount:update")
	public Object userUpdate(@RequestParam(name = "exType", required = true) String exType, BmsUserResponse user) {

		String deal = exType.trim();
		BaseUser principal = SessionUtil.getUser();
		user.setUpdateUserId(principal.getUserId());
		user.setUpdateUser(principal.getRealName());

		if ("UPDATE".equalsIgnoreCase(deal)) {
			netToAccountService.updateUser(user);
			// 清除用户缓存
			SessionUtil.clearUser(user.getUserId());
		} else if ("ADD".equalsIgnoreCase(exType)) {
			netToAccountService.insertUser(user);
		}
		return "success";
	}
	
	/**
	 * 启动和停用
	 * @param exType
	 * @param userId
	 * @param model
	 * @return
	 */
	@RequestMapping("/userBlock")
	@Log
	@ResponseBody
	@Rule("netToAccount:update")
	public Object userBlock(String isBlock, String userId, Model model) {

		BmsUser user = new BmsUser();
		BaseUser principal = SessionUtil.getUser();
		user.setUpdateUserId(principal.getUserId());
		user.setUpdateUser(principal.getRealName());
		user.setUserId(userId);
		user.setIsBlock(isBlock);
		netToAccountService.editUser(user);
		// 清除用户缓存
		SessionUtil.clearUser(userId);

		return "SUCCESS";
	}
	/**
	 * 严重登录账号是否存在
	 * @param userName
	 * @param oldUserName
	 * @return
	 */
	@RequestMapping("/validateUser")
	@ResponseBody
	@Rule("netToAccount:update")
	public Object validateUser(String userName, String oldUserName) {
		// 修改操作
		if (StringUtil.hasValue(userName)) {
			return netToAccountService.isUserNameExist(userName, oldUserName);
		}
		return false;
	}
    /**
     * 获取城市列表
     * @param repCode
     * @param provinceCode
     * @return
     */
	@RequestMapping("/getCityInfoByProvinceCode")
	@ResponseBody
	public Object getCityInfoByProvinceCode(@RequestParam(name = "repCode", required = true) String repCode, String provinceCode){
		return sysDictService.getCityInfoByProvinceCode(repCode, provinceCode);
	}
}
