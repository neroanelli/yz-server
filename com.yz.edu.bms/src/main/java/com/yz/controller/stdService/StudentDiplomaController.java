package com.yz.controller.stdService;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.model.common.IPageInfo;
import com.yz.model.stdService.StudentDiplomaInfo;
import com.yz.model.stdService.StudentDiplomaQuery;
import com.yz.service.stdService.StudentDiplomaService;
import com.yz.util.Assert;
import com.yz.util.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zlp
 * 学服任务--毕业证发放任务跟进
 */
@Controller
@RequestMapping("/diploma")
public class StudentDiplomaController
{
	@Autowired
	private StudentDiplomaService studentDiplomaService;
	
	@RequestMapping("/toList")
	@Rule("diploma:query")
	public String toList(Model model, HttpServletRequest request) {
		return "/stdservice/diploma/student_diploma_list";
	}
	
	@RequestMapping("/findAllDiplomaList")
	@Rule("diploma:query")
	@ResponseBody
	public Object findAllEngdiplomaList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,StudentDiplomaQuery query) {
		PageHelper.offsetPage(start, length);
		return studentDiplomaService.findAllDiplomaList(query);
	}
	
	@RequestMapping("/toDetails")
	@Rule("diploma:detail")
	public String toDetails(HttpServletRequest request,
			Model model) {
		StudentDiplomaInfo diplomaInfo  = new StudentDiplomaInfo();
		String followId = RequestUtil.getString("followId");
		Assert.hasText(followId, "参数名称不能为空");
		diplomaInfo = studentDiplomaService.getDiplomaInfoById(followId);
		model.addAttribute("dataInfo", diplomaInfo);
		return "/stdservice/diploma/student_diploma_detail";
	}
	
	@RequestMapping("/toStatistics")
	@Rule("diploma:statistics")
	public String toStatistics(Model model, HttpServletRequest request) {
		return "/stdservice/diploma/student_diploma_statistics";
	}
	
	@RequestMapping("/statistics")
    @Rule("diploma:statistics")
	@ResponseBody
	public Object statistics(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,String taskId,String tutor) {
		PageHelper.offsetPage(start, length);
		return studentDiplomaService.getDiplomaStatisticsInfo(taskId,tutor);
	}
	
	@RequestMapping("/toSetRemark")
	@Rule("diploma:edit")
	public String toSetRemark(HttpServletRequest request,
			Model model) {
		String followId = RequestUtil.getString("followId");
		Assert.hasText(followId, "参数名称不能为空");
		StudentDiplomaInfo diplomaInfo = studentDiplomaService.getDiplomaInfoById(followId);
		model.addAttribute("dataInfo", diplomaInfo);
		return "/stdservice/diploma/student_diploma_remark";
	}
	@RequestMapping("/setRemark")
	@Rule("diploma:edit")
	@ResponseBody
	public Object updateRemark(String followId,String remark, HttpServletResponse response) {
		studentDiplomaService.updateRemark(followId, remark);
		return "SUCCESS";
	}
	

	
	
  
    @RequestMapping("/exportDiplomaInfo")
	@Rule("diploma:export")
	public void exportDiplomaInfo(StudentDiplomaQuery query, HttpServletResponse response) {
	   studentDiplomaService.exportDiplomaInfo(query, response);
	}


	
    @RequestMapping("/findAffirmTimeList")
	@ResponseBody
	public Object findAffirmTimeList(String taskId, 
			@RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		return new IPageInfo((Page) studentDiplomaService.findAffirmTimeList(taskId));
	}
    
    @RequestMapping("/stuDiplomaImport")
    @Rule("diploma:import")
    public String studiplomaEnglishDataImport(HttpServletRequest request) {
        return "stdservice/diploma/student_diploma_import";
    }
    
    @RequestMapping("/uploadStuDiploma")
    @Rule("diploma:import")
    @ResponseBody
    public Object uploadStudiplomaEnglish(
            @RequestParam(value = "stuDiplomaImport", required = false) MultipartFile stuDiplomaImport) {
    	 studentDiplomaService.importStudiplomaDataInfo(stuDiplomaImport);
    	 return "SUCCESS";
    }
    
    
    /**
	 * 编辑页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toEdit")
	@Rule("diploma:query")
	public String toEdit(Model model, String followId) {
		Assert.hasText(followId, "参数名称不能为空");
		StudentDiplomaInfo diplomaInfo = studentDiplomaService.getDiplomaInfoById(followId);
		//model.addAttribute("followId", followId);
		model.addAttribute("dataInfo", diplomaInfo);
		return "/stdservice/diploma/student_diploma_edit";
	}
	
	
	/**
	 * 毕业证/发票编号编辑页
	 * @param model
	 * @return
	 */
	@RequestMapping("/toDiplomaCodeSet")
	@Rule({ "diploma:query", "diploma:codeset" })
	public String toDiplomaCodeSet(Model model, String followId) {
		return "/stdservice/diploma/student_diploma_code";
	}
	
	@RequestMapping("/diplomaCodeSet")
	@Rule("diploma:codeset")
	@ResponseBody
	public Object diplomaCodeSet(StudentDiplomaInfo info) {
		studentDiplomaService.editDiplomaInfo(info);
		return "SUCCESS";
	}
	
	/**
	 * 领取确认编辑页
	 * @param model
	 * @return
	 */
	@RequestMapping("/toReceiveAffirmSet")
	@Rule({ "diploma:query", "diploma:receiveAffirmSet" })
	public String toReceiveAffirmSet(Model model, String followId) {
		return "/stdservice/diploma/student_diploma_receiveAffirm";
	}
	
	@RequestMapping("/receiveAffirmSet")
	@Rule("diploma:receiveAffirmSet")
	@ResponseBody
	public Object receiveAffirmSet(StudentDiplomaInfo info) {
		//如果是待领取
		if(info.getReceiveStatus().equals("0")) {
			info.setReceiveTime("");
		}else {
			info.setReceiveTime(DateUtil.getNowDateAndTime());
		}
		studentDiplomaService.editDiplomaInfo(info);
		return "SUCCESS";
	}
	
	/**
	 * 未确认原因编辑页
	 * @param model
	 * @return
	 */
	@RequestMapping("/toUnconfirmReasonSet")
	@Rule({ "diploma:query", "diploma:unconfirmReasonSet" })
	public String toUnconfirmReasonSet(Model model, String followId) {
		return "/stdservice/diploma/student_diploma_unconfirmReason";
	}
	
	@RequestMapping("/unconfirmReasonSet")
	@Rule("diploma:unconfirmReasonSet")
	@ResponseBody
	public Object unconfirmReasonSet(StudentDiplomaInfo info) {
		studentDiplomaService.editDiplomaInfo(info);
		return "SUCCESS";
	}
	
	
	@RequestMapping("/resetUnconfirmReason")
	@Rule("diploma:unconfirmReasonSet")
	@ResponseBody
	public Object resetUnconfirmReason(String followId,String learnId) {
		studentDiplomaService.resetUnconfirmReason(followId,learnId);
		return "SUCCESS";
	}
	
	
	/**
	 * 邮寄发货编辑页
	 * @param model
	 * @return
	 */
	@RequestMapping("/toMailinfoSet")
	@Rule({ "diploma:query", "diploma:mailinfoSet" })
	public String toMailinfoSet(Model model, String followId) {
		return "/stdservice/diploma/student_diploma_mailinfoSet";
	}
	
	@RequestMapping("/mailinfoSet")
	@Rule("diploma:mailinfoSet")
	@ResponseBody
	public Object mailinfoSet(StudentDiplomaInfo info) {
		studentDiplomaService.editDiplomaInfo(info);
		return "SUCCESS";
	}
	
	/**
	 * 设置/重置领取信息编辑页
	 * @param model
	 * @return
	 */
	@RequestMapping("/toReset")
	@Rule({ "diploma:query", "diploma:reset" })
	public String toReset(Model model, String followId) {
		return "/stdservice/diploma/student_diploma_reset";
	}
	
	@RequestMapping("/resetTask")
	@Rule("diploma:reset")
	@ResponseBody
	public Object resetTask(StudentDiplomaInfo info) {
		studentDiplomaService.resetTask(info.getFollowId(),info.getTaskId(),info.getLearnId());
		return "SUCCESS";
	}
	
	@RequestMapping("/receiveInfoSet")
	@Rule("diploma:reset")
	@ResponseBody
	public Object receiveInfoSet(StudentDiplomaInfo info) {
		studentDiplomaService.receiveInfoSet(info);
		return "SUCCESS";
	}
	
	
	@RequestMapping("/getReceivePlaceList")
	@ResponseBody
	public Object getReceivePlaceList(StudentDiplomaInfo info,@RequestParam(value = "rows", defaultValue = "7") int rows,
			   @RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		return new IPageInfo((Page) studentDiplomaService.getReceivePlaceList(info));
	}
	
	@RequestMapping("/getReceiveAddress")
	@ResponseBody
	public Object getReceiveAddress(String placeId) {
		return studentDiplomaService.getReceiveAddress(placeId);
	}
 
	
	/**
	 * 得到学员毕业证领取日期列表
	 * @param info
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/findAffirmDateListByLearnId")
	@ResponseBody
	public Object findAffirmDateListByLearnId(StudentDiplomaInfo info,
			@RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		return new IPageInfo((Page) studentDiplomaService.findAffirmDateListByLearnId(info));
	}
	
	/**
	 * 得到学员毕业证领取时间段列表
	 * @param info
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/findAffirmTimeListByLearnId")
	@ResponseBody
	public Object findAffirmTimeListByLearnId(StudentDiplomaInfo info,
			@RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		return new IPageInfo((Page) studentDiplomaService.findAffirmTimeListByLearnId(info));
	}
	
	
}
