package com.yz.controller.operate;

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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yz.conf.YzSysConfig;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.operate.BdScholarshipStory;

import com.yz.oss.OSSUtil;
import com.yz.service.operate.BdScholarshipStoryService;
import com.yz.util.Assert;

import com.yz.util.StringUtil;
import com.yz.util.ValidationUtil;

//import com.yz.util.Assert;
//import com.yz.util.ValidationUtil;

/**
 * 奖学金故事
 * @author Dell
 * @date 2018/06/28
 */
@Controller
@RequestMapping("/scholarshipStory")
public class BdScholarshipStoryController {
	
	@Autowired
	private BdScholarshipStoryService scholarshipStoryService;
	

	@Autowired
	private YzSysConfig yzSysConfig ; 
	/**
	 * 跳转到 奖学金列表页
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("scholarshipStory:query")
	public String toList() {
		return "operate/scholarshipStory/scholarship_story_list";
	}
	
	
	/**
	 * 删除图片
	 * @param mRequest
	 * @return
	 */
	@RequestMapping("/deletefile")
	@ResponseBody
	public Object deletefile(MultipartHttpServletRequest mRequest) {
		String isscholarshipStory = mRequest.getParameter("scholarshipStory");
		if(!StringUtil.isEmpty(isscholarshipStory)){
			String bucket = yzSysConfig.getBucket();
			String filename = mRequest.getParameter("filename");
			OSSUtil.deleteFile(bucket, filename);
		}
		return "success";
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
	@Rule("scholarshipStory:query")
	public Object getScholarshipStoryList(@RequestParam(name = "start", defaultValue = "0") int start,@RequestParam(name = "length", defaultValue = "10") int length,BdScholarshipStory query){
		return scholarshipStoryService.getScholarshipStory(start,length,query);
	}
	
	
	/**
	 * 跳转新增或编辑页面
	 * @param tjType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/editOrAdd")
	@Rule("scholarshipStory:update")
	public String editOrAdd(@RequestParam(name = "tjType", required = true) String tjType, HttpServletRequest request,
			Model model) {
		// isAllow
		String[] words = { "scholarshipId", "articleTitle", "textPre", "entranceText", "entranceLink", "textLast", "articleLinkTitle", "articleLinkDes", "articlePicUrl"};
		Map<String, Object> scholarshipStory = new HashMap<String, Object>();
		for (String word : words) {
			scholarshipStory.put(word, null);
		}

		if ("UPDATE".equals(tjType.trim().toUpperCase())) {
			String scholarshipId = RequestUtil.getString("scholarshipId");
			Assert.hasText(scholarshipId, "参数名称不能为空");
			scholarshipStory = scholarshipStoryService.getOneScholarshipStory(scholarshipId);
		}
		model.addAttribute("tjType", tjType);
		model.addAttribute("scholarshipStory", scholarshipStory);
		return "operate/scholarshipStory/scholarship_story_edit";
	}
	
	/**
	 * 删除奖学金故事
	 * @param scholarshipId
	 * @return
	 */
	@RequestMapping("/deleteScholarshipStory")
	@ResponseBody
	@Rule("scholarshipStory:delete")
	public Object deleteScholarshipStory(@RequestParam(name = "scholarshipId", required = true) String scholarshipId) {
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(scholarshipId);
		
		scholarshipStoryService.deleteScholarshipStory(scholarshipId);
		return "success";
	}
	
	
	/**
	 * 修改启用禁用状态
	 * @param scholarshipId
	 * @param isAllow
	 * @return
	 */
	@RequestMapping("/updateIsAllow")
	@ResponseBody
	@Rule("scholarshipStory:updateStatus")
	public Object updateIsAllow(@RequestParam(name = "scholarshipId", required = true) String scholarshipId,@RequestParam(name = "isAllow", required = true) String isAllow){
		scholarshipStoryService.updateIsAllow(scholarshipId, isAllow);
		return "success";
	}
	
	/**
	 * 批量删除奖学金故事
	 * @param scholarshipIds
	 * @return
	 */
	@RequestMapping("/deleteScholarshipStoryByIdArr")
	@ResponseBody
	@Rule("scholarshipStory:deleteChecked")
	public Object deleteScholarshipStoryByIdArr(@RequestParam(name = "scholarshipIds[]", required = true) String[] scholarshipIds){
		scholarshipStoryService.deleteScholarshipStoryByIdArr(scholarshipIds);
		return "SUCCESS";
	}
	

	/**
	 * 修改奖学金故事
	 * @param query
	 * @return
	 */
	@RequestMapping("/updateScholarshipStory")
	@ResponseBody
	@Rule("scholarshipStory:edit")
	public Object updateScholarshipStory(BdScholarshipStory query) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(query.getScholarshipId());
		BaseUser user = SessionUtil.getUser();
		query.setUpdateUser(user.getUserName());
		query.setUpdateUserId(user.getUserId());
		scholarshipStoryService.updateScholarshipStory(query);
		return "success";
	}
	
	
	/**
	 * 插入奖学金故事
	 * @param query
	 * @return
	 */
	@RequestMapping("/insertScholarshipStory")
	@ResponseBody
	@Rule("scholarshipStory:insert")
	public Object insertScholarshipStory(BdScholarshipStory query) {
		BaseUser user = SessionUtil.getUser();
		query.setCreateUser(user.getUserName());
		query.setCreateUserId(user.getUserId());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String  createTime = df.format(new Date());//获取获取当前系统时间
		query.setCreateTime(createTime);
		scholarshipStoryService.insert(query);
		return "success";
	}
	
}
