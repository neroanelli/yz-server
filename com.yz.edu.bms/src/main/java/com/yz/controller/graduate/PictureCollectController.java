package com.yz.controller.graduate;

import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.dao.system.SysParameterMapper;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.condition.educational.OaTaskTargetStudentQuery;
import com.yz.model.graduate.PictureCollectInfo;
import com.yz.model.graduate.PictureCollectQuery;
import com.yz.model.graduate.PictureCollectWhiteQuery;
import com.yz.model.stdService.StudentXuexinInfo;
import com.yz.model.stdService.StudentXuexinQuery;
import com.yz.service.graduate.PictureCollectService;
import com.yz.service.stdService.StudentXuexinService;
import com.yz.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import scala.util.matching.Regex;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 毕业服务--毕业证图像采集
 * @author jyt
 */
@Controller
@RequestMapping("/pictureCollect")
public class PictureCollectController
{
	@Autowired
	private PictureCollectService pictureCollectService;
	
	@RequestMapping("/toList")
	@Rule("pictureCollect:query")
	public String toList(Model model, HttpServletRequest request) {
		return "/graduate/pictureCollect/picture_collect_list";
	}

	@RequestMapping("/toEdit")
	public String toEdit(String picCollectIds, Model model, HttpServletRequest request) {
		model.addAttribute("picCollectIds", picCollectIds);
		return "/graduate/pictureCollect/picture_collect_edit";
	}

	@RequestMapping("/toWhiteList")
	@Rule("pictureCollect:white")
	public String toWhiteList(Model model, HttpServletRequest request) {
		return "/graduate/pictureCollect/picture_collect_white_list";
	}
	
	@RequestMapping("/findPictureCollectList")
	@Rule("pictureCollect:query")
	@ResponseBody
	public Object findPictureCollectList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,PictureCollectQuery query) {
		PageHelper.offsetPage(start, length);
		return pictureCollectService.findPictureCollectList(query);
	}

	
    @RequestMapping("/pictureCollectWhiteImport")
	@Rule("pictureCollect:whiteUpload")
    public String pictureCollectWhiteImport(HttpServletRequest request) {
        return "graduate/pictureCollect/picture_collect_white-import";
    }

	@RequestMapping("/pictureCollectImport")
	@Rule("pictureCollect:upload")
	public String pictureCollectImport(HttpServletRequest request) {
		return "graduate/pictureCollect/picture_collect-import";
	}
    
    @RequestMapping("/uploadictureCollectWhite")
	@Rule("pictureCollect:whiteUpload")
    @ResponseBody
    public Object uploadictureCollectWhite(
            @RequestParam(value = "importFile", required = false) MultipartFile importFile) {
		pictureCollectService.uploadictureCollectWhite(importFile);
    	 return "SUCCESS";
    }

	@RequestMapping("/uploadPictureCollect")
	@Rule("pictureCollect:upload")
	@ResponseBody
	public Object uploadPictureCollect(
			@RequestParam(value = "importFile", required = false) MultipartFile importFile)  throws Exception {
		try {
			pictureCollectService.uploadPictureCollect(importFile);
		}catch (Exception ex){
			if(ex.getMessage().contains("idx_picture_no")){
				throw new Exception("相片编号重复!");
			}
			throw ex;
		}
		return "SUCCESS";
	}
    
    @RequestMapping("/exportPictureCollect")
	@Rule("pictureCollect:export")
	public void exportPictureCollect(PictureCollectQuery query, HttpServletResponse response) {
		pictureCollectService.exportPictureCollect(query, response);
	}

	@RequestMapping("/updateRemark")
	@Rule("pictureCollect:remark")
	@ResponseBody
	public Object updateRemark(String picCollectId,String addRemark, HttpServletResponse response) {
		pictureCollectService.updateRemark(picCollectId, addRemark);
		return "SUCCESS";
	}

	@RequestMapping("/check")
	@Rule("pictureCollect:check")
	@ResponseBody
	public Object check(String picCollectIds,String checkStatus,String reason, HttpServletResponse response) {
		pictureCollectService.check(Arrays.asList(picCollectIds.split(",")),checkStatus,reason);
		return "SUCCESS";
	}
	
	@RequestMapping("/revoke")
	@Rule("pictureCollect:revoke")
	@ResponseBody
	public Object revoke(String picCollectIds,String checkStatus, HttpServletResponse response) {
		pictureCollectService.revoke(Arrays.asList(picCollectIds.split(",")),checkStatus);
		return "SUCCESS";
	}
	
	

	@RequestMapping("/whiteList")
	@Rule("pictureCollect:white")
	@ResponseBody
	public Object stuList(@RequestParam(name = "start", defaultValue = "0") int start,
						  @RequestParam(name = "length", defaultValue = "10") int length,PictureCollectWhiteQuery whiteQuery) {
		return pictureCollectService.queryWhiteList(start, length,whiteQuery);
	}

	@RequestMapping("/addWhiteStu")
	@Rule("pictureCollect:addWhiteStu")
	@ResponseBody
	public Object addWhiteStu(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		pictureCollectService.addWhiteStu(idArray);
		return "success";
	}

	@RequestMapping("/delWhiteStu")
	@Rule("pictureCollect:delWhiteStu")
	@ResponseBody
	public Object delWhiteStu(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		pictureCollectService.delWhiteStu(idArray);
		return "success";
	}

	@RequestMapping("/addAllWhiteStu")
	@Rule("pictureCollect:addAllWhiteStu")
	@ResponseBody
	public Object addAllWhiteStu(PictureCollectWhiteQuery whiteQuery) {
		pictureCollectService.addAllWhiteStu(whiteQuery);
		return "success";
	}

	@RequestMapping("/delAllWhiteStu")
	@Rule("pictureCollect:delAllWhiteStu")
	@ResponseBody
	public Object delAllWhiteStu(PictureCollectWhiteQuery whiteQuery) {
		pictureCollectService.delAllWhiteStu(whiteQuery);
		return "success";
	}

	@RequestMapping("/exportRar")
	@Rule("pictureCollect:rar")
	public void exportRar(PictureCollectQuery query, HttpServletResponse response) {
		pictureCollectService.exportRar(query, response);
	}
}
