package com.yz.controller.transfer;

import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.common.IPageInfo;
import com.yz.model.transfer.BdStudentConfirmModify;
import com.yz.model.transfer.BdStudentModify;
import com.yz.model.transfer.StudentModifyMap;
import com.yz.service.transfer.BdExamModifyReviseService;
import com.yz.service.transfer.BdStudentConfirmCheckService;
import com.yz.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：林建彬.
 * @ Date       ：Created in 15:18 2018/8/20
 * @ Description：网报异动修改
 */
@Controller
@RequestMapping("/examModifyRevise")
public class BdExamModifyReviseController {

    @Autowired
    private BdExamModifyReviseService bdExamModifyReviseService;

    @Autowired
    private BdStudentConfirmCheckService bdStudentConfirmCheckService;

    @RequestMapping("/toList")
    @Rule("examRevise:find")
    public String toList(HttpServletRequest request) {
        return "transfer/exam/student-exam-modify-revise-list";
    }

    @RequestMapping("/findExamModifyRevise")
    @Rule("examRevise:find")
    @ResponseBody
    public Object findExamModifyRevise(@RequestParam(value = "start", defaultValue = "1") int start,
                                         @RequestParam(value = "length", defaultValue = "10") int length, StudentModifyMap studentModifyMap) {
        PageHelper.offsetPage(start, length);
        List<Map<String, String>> resultList = bdExamModifyReviseService.findStudentAuditModify(studentModifyMap);
        return new IPageInfo((Page) resultList);
    }

    /**
     *
     * @param modifyId
     * @param checkStatus
     * @param reason
     * @param remark
     * @return
     */
    @RequestMapping("/passModify")
    @Rule("examRevise:update")
    @ResponseBody
    public Object passModify(String modifyId, String checkStatus, String reason, String remark) {
        Assert.hasText(modifyId, "参数名称不能为空");
        Assert.hasText(checkStatus, "参数名称不能为空");
        bdExamModifyReviseService.passModify(modifyId, checkStatus, reason, remark);
        return "SUCCESS";
    }

    /**
     *  跳转到查看页面
     * @param modifyId
     * @return
     */
    @RequestMapping("/toRevise")
    @Rule("examRevise:update")
    public Object toRevise(String modifyId, String learnId, Model model) {

        Assert.hasText(modifyId, "参数名称不能为空");
        Assert.hasText(learnId, "参数名称不能为空");

        //查询基本信息
        HashMap<String,String> studentInfo = bdExamModifyReviseService.findStudentInfo(learnId);
        model.addAttribute("studentInfo",studentInfo);
        //查询异动信息
        BdStudentConfirmModify modify = bdStudentConfirmCheckService.findStudentModifyById(modifyId);
        model.addAttribute("modify",modify);
        //查询审核记录
        List<Map<String, String>> records = bdStudentConfirmCheckService.findStudentModifyByModifyId(modifyId);
        model.addAttribute("records",records);
        return "transfer/exam/student-exam-modify-revise-info";
    }

}
