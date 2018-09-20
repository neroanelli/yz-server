package com.yz.controller.sceneMng;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.common.IPageInfo;
import com.yz.model.recruit.StudentSceneConfirmInfo;
import com.yz.model.recruit.StudentSceneConfirmQuery;
import com.yz.service.sceneMng.BdStudentSceneConfirmService;
import com.yz.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

/**
 * @author zlp
 * @version 1.0
 */
@RequestMapping("/bdSceneConfirm")
@Controller
public class BdsStudentSceneConfirmController {

    @Autowired
    BdStudentSceneConfirmService studentSceneConfirmService;

    @RequestMapping("/list")
    @Rule("bdSceneConfirm:query")
    public String showList(HttpServletRequest request) {
        return "sceneMng/student_scene_confirm_list";
    }

    @RequestMapping("/toEdit")
    @Rule("bdSceneConfirm:edit")
    public String toEdit(Model model) {
        StudentSceneConfirmInfo confirmInfo;
        String id = RequestUtil.getString("id");
        Assert.hasText(id, "id参数名称不能为空");
        confirmInfo = studentSceneConfirmService.getInfoById(id);
        model.addAttribute("dataInfo", confirmInfo);
        model.addAttribute("regInfo",studentSceneConfirmService.getSceneRegisterList(confirmInfo.getLearnId()));
        model.addAttribute("modifyInfo",studentSceneConfirmService.getExamNoModifyRecord(confirmInfo.getLearnId()));
        return "sceneMng/student_scene_confirm_edit";
    }

    @RequestMapping("/findAllSceneConfirm")
    @Rule("bdSceneConfirm:query")
    @ResponseBody
    public Object findAllSceneConfirm(@RequestParam(value = "start", defaultValue = "1") int start,
                                      @RequestParam(value = "length", defaultValue = "10") int length, StudentSceneConfirmQuery query) {
        PageHelper.offsetPage(start, length).setCountMapper("com.yz.dao.sceneMng.BdStudentSceneConfirmMapper.getAllSceneConfirmCount");
        List<StudentSceneConfirmInfo> resultList = studentSceneConfirmService.findAllSceneConfirm(query);
        return new IPageInfo((Page) resultList);
    }

    @RequestMapping("/updateConfirmInfo")
    @Rule("bdSceneConfirm:edit")
    @ResponseBody
    public Object updateConfirmInfo(String learnId,String stdId,String examNo,String sceneRemark){
        studentSceneConfirmService.updateConfirmInfo(learnId,stdId,examNo,sceneRemark);
        return "SUCCESS";
    }
    
    
   /**
    * 批量勾选重置任务
    * @param confirmIds
    * @return
    */
	@RequestMapping("/checkResetTask")
	@ResponseBody
	@Log
	@Rule("bdSceneConfirm:checkResetTask")
	public Object checkResetTask(@RequestParam(name = "confirmIds[]", required = true) String[] confirmIds) {
		studentSceneConfirmService.checkResetTask(confirmIds);
		return "SUCCESS";
	}
	
	/**
	 * 筛选重置任务
	 * @param confirmIds
	 * @return
	 */
	@RequestMapping("/queryResetTask")
	@ResponseBody
	@Log
	@Rule("bdSceneConfirm:queryResetTask")
	public Object queryResetTask(StudentSceneConfirmQuery query) {
		studentSceneConfirmService.queryResetTask(query);
		return "SUCCESS";
	}
	
	
	@RequestMapping("/toAddRegisterNo")
    @Rule("bdSceneConfirm:insertRegisterNo")
    public String toAddRegisterNo(Model model) {
        String learnId = RequestUtil.getString("learnId");
        String stdId = RequestUtil.getString("stdId");
        Assert.hasText(learnId, "learnId参数名称不能为空");
        model.addAttribute("learnId", learnId);
        model.addAttribute("stdId", stdId);
        return "sceneMng/student_registerNo_add";
    }
	
	
	@RequestMapping("/insertRegisterNo")
	@ResponseBody
    @Rule("bdSceneConfirm:insertRegisterNo")
    public Object insertRegisterNo(String learnId,String stdId,String username,String password) {
		studentSceneConfirmService.insertRegisterNo(learnId,stdId,username,password);
		return "SUCCESS";
    }
	
	@RequestMapping("/setAvailabe")
	@ResponseBody
    @Rule("bdSceneConfirm:setAvailabe")
    public Object setAvailabeRegisterNo(String registerId,String learnId,String stdId,String username) {
		studentSceneConfirmService.setAvailabeRegisterNo(registerId,learnId,stdId,username);
		return "SUCCESS";
    }
	
	
	@RequestMapping("/viewExamInfo")
    @Rule("bdSceneConfirm:detail")
    public String ViewExamInfo(Model model) {
        String learnId = RequestUtil.getString("learnId");
        Assert.hasText(learnId, "learnId参数名称不能为空");
        Map<String,String>  info = studentSceneConfirmService.getEaxmInfoByLearnId(learnId);
        model.addAttribute("dataInfo", info);
       return "sceneMng/student_scene_examInfo";
    }
	
	
	@RequestMapping("/stuExamNoImport")
    @Rule("bdSceneConfirm:importExamNo")
    public String stuExamNoImport(HttpServletRequest request) {
        return "sceneMng/student_examno_import";
    }
	 
	@RequestMapping("/uploadStuExamNo")
    @Rule("bdSceneConfirm:importExamNo")
    @ResponseBody
    public Object uploadStuExamNo(
            @RequestParam(value = "stuExamNoImport", required = false) MultipartFile stuExamNoImport) {
		 studentSceneConfirmService.importStuExamNo(stuExamNoImport);
    	 return "SUCCESS";
    }
	
	/**
	 * 修改网报信息状态
	 * @param request
	 * @return
	 */
	@RequestMapping("/toEditStatus")
    //@Rule("sceneConfirm:toEditStatus")
    public String toEdit(HttpServletRequest request) {
        return "recruit/studentSceneConfirm/edit_status";
    }
    
    /**
     * 修改网报信息状态
     * @param idCard
     * @param examPayStatus
     * @param webRegisterStatus
     * @return
     */
	@RequestMapping("/editStatus")
    //@Rule("bdSceneConfirm:toEditStatus")
    @ResponseBody
    public Object editStatus(String idCard,String examPayStatus,String webRegisterStatus){
    	studentSceneConfirmService.editConfirmStatus(idCard,examPayStatus,webRegisterStatus);
    	return "SUCCESS";
    }
	
	@RequestMapping("/exportConfirmStudent")
	@Rule("bdSceneConfirm:exportConfirmStudent")
	public void exportConfirmStudent(StudentSceneConfirmQuery query, HttpServletResponse response) {
		studentSceneConfirmService.exportConfirmStudent(query,response);
	}
}
