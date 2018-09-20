package com.yz.controller.sceneMng;

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
import org.springframework.web.multipart.MultipartFile;

import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.sceneMng.OaSceneManagement;
import com.yz.service.sceneMng.OaSceneManagementService;
import com.yz.util.Assert;
import com.yz.util.ValidationUtil;

/**
 * 现场 确认管理
 * @author Dell
 *
 */
@RequestMapping("/sceneManagement")
@Controller
public class OaSceneManagementController {
	
	
		@Autowired
		private OaSceneManagementService sceneManagementService;
		
		/**
		 * 跳转页面
		 * @param request
		 * @return
		 */
		@RequestMapping("/toList")
	    @Rule("sceneManagement:query")
	    public String showList(HttpServletRequest request) {
	        return "sceneMng/sceneManagement-list";
	    }
	
		
		/**
		 * 查询列表
		 * @param start
		 * @param length
		 * @param query
		 * @return
		 */
		@RequestMapping(value = "/list")
		@ResponseBody
		@Rule("sceneManagement:query")
		public Object findAllSceneManagement(@RequestParam(name = "start", defaultValue = "0") int start,@RequestParam(name = "length", defaultValue = "10") int length,OaSceneManagement query){
			return sceneManagementService.getScholarshipStory(start, length, query);
		}
		
		
		/**
		 * 跳转新增或编辑页面
		 * @param tjType
		 * @param request
		 * @param model
		 * @return
		 */
		@RequestMapping("/editOrAdd")
		@Rule("sceneManagement:updateOrAdd")
		public String editOrAdd(@RequestParam(name = "tjType", required = true) String tjType, HttpServletRequest request,
				Model model) {
			// isAllow
			String[] words = { "confirmationId", "taId","taName","confirmCity", "appointmentNum","address", "confirmName", "confirmAddressLevel", "requiredMaterials", "chargePerson", "chargePersonTel","remark","startTime","endTime","tjType"};
			Map<String, Object> sceneManagement = new HashMap<String, Object>();
			for (String word : words) {
				if("tjType".equals(word)){
					sceneManagement.put(word, tjType);
				}else{
					sceneManagement.put(word, null);
				}
			}

			if ("UPDATE".equals(tjType.trim().toUpperCase())) {
				String confirmationId = RequestUtil.getString("confirmationId");
				Assert.hasText(confirmationId, "参数名称不能为空");
				sceneManagement = sceneManagementService.getSceneManagementById(confirmationId);
				sceneManagement.put("tjType", tjType);
			}
			model.addAttribute("sceneManagement", sceneManagement);
			return "sceneMng/sceneManagement_edit";
		}
		
		
		@RequestMapping("/getExamDicName")
		@ResponseBody
		public Object getExamDicName(){
			return sceneManagementService.getExamDicName();
		}
		
		@RequestMapping("/insert")
		@ResponseBody
		@Rule("sceneManagement:insert")
		public Object insert(OaSceneManagement query) {
			BaseUser user = SessionUtil.getUser();
			query.setCreateUser(user.getRealName());
			query.setCreateUserId(user.getUserId());
			sceneManagementService.insert(query);
			return "SUCCESS";
		}
		
		
		/**
		 * 删除
		 * @param confirmationId
		 * @return
		 */
		@RequestMapping("/delete")
		@ResponseBody
		@Rule("sceneManagement:delete")
		public Object delete(@RequestParam(name = "confirmationId", required = true) String confirmationId) {
			ValidationUtil.getInstance().isNotEmptyIgnoreBlank(confirmationId);
			if(sceneManagementService.findStuConfirmById(confirmationId)>0){
				return "false";
			}
			sceneManagementService.delete(confirmationId);
			return "SUCCESS";
		}
		
		/**
		 * 批量删除
		 * @param scholarshipIds
		 * @return
		 */
		@RequestMapping("/deleteByIdArr")
		@ResponseBody
		@Rule("sceneManagement:deleteChecked")
		public Object deleteScholarshipStoryByIdArr(@RequestParam(name = "confirmationIds[]", required = true) String[] confirmationIds){
			sceneManagementService.deleteByIdArr(confirmationIds);
			return "SUCCESS";
		}
		
		
		/**
		 * 修改
		 * @param query
		 * @return
		 */
		@RequestMapping("/updateSceneManagement")
		@ResponseBody
		@Rule("sceneManagement:edit")
		public Object update(OaSceneManagement query) {
			// 校验唯一标识不能为空
			ValidationUtil.getInstance().isNotEmptyIgnoreBlank(query.getConfirmationId());
			BaseUser user = SessionUtil.getUser();
			query.setUpdateUser(user.getRealName());
			query.setUpdateUserId(user.getUserId());
			String number = RequestUtil.getString("appointmentNum"); //预约人数
			int availableNumbers = Integer.parseInt(query.getConfigs().get(0).getNumber()) - Integer.parseInt(number);
			query.setAvailableNumbers(String.valueOf(availableNumbers));
			sceneManagementService.updateSceneManagement(query);
			return "success";
		}
		
		
		/**
		 * 修改状态
		 * @param query
		 * @return
		 */
		@RequestMapping("/updateAllow")
		@ResponseBody
		@Rule("sceneManagement:editStatus")
		public Object updateAllow(@RequestParam(name = "confirmationId", required = true) String confirmationId,String status) {
			ValidationUtil.getInstance().isNotEmptyIgnoreBlank(confirmationId);
			if("2".equals(status)&&sceneManagementService.findStuConfirmById(confirmationId)>0){
				return "false";
			}
			sceneManagementService.updateIsAllow(confirmationId, status);
			return "success";
		}
	
		/**
		 * 批量修改状态
		 * @param query
		 * @return
		 */
		@RequestMapping("/updateStatusByIdArr")
		@ResponseBody
		@Rule("sceneManagement:editStatusChecked")
		public Object updateStatusByIdArr(@RequestParam(name = "confirmationIds[]", required = true) String[] confirmationIds,String status) {
			sceneManagementService.updateStatus(confirmationIds,status);
			return "success";
		}
		
		
		 @RequestMapping("/toUploadSceneManagement")
		 @Rule("sceneManagement:toImport")
		 public String toUploadSceneManagement(HttpServletRequest request) {
		        return "sceneMng/sceneManagement-import";
		 }
		
		@RequestMapping("/uploadSceneManagement")
	    @Rule("sceneManagement:import")
	    @ResponseBody
	    public Object uploadSceneManagementData(@RequestParam(value = "sceneManagementImport", required = false) MultipartFile sceneManagementImport) {
			sceneManagementService.uploadSceneManagementData(sceneManagementImport);
	    	 return "SUCCESS";
	    }
}
