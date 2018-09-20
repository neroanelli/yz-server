package com.yz.controller.recruit;

import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.common.IPageInfo;
import com.yz.model.pubquery.TutorshipBookSendQuery;
import com.yz.model.recruit.StudentSceneConfirmInfo;
import com.yz.model.recruit.StudentSceneConfirmQuery;
import com.yz.model.stdService.StudentXuexinInfo;
import com.yz.service.pubquery.TutorshipBookSendService;
import com.yz.service.recruit.StudentSceneConfirmService;
import com.yz.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
@RequestMapping("/sceneConfirm")
@Controller
public class StudentSceneConfirmController {

    @Autowired
    StudentSceneConfirmService studentSceneConfirmService;

    @RequestMapping("/list")
    @Rule("sceneConfirm:query")
    public String showList(HttpServletRequest request) {
        return "recruit/studentSceneConfirm/scene_confirm_list";
    }

    @RequestMapping("/toEdit")
    @Rule("sceneConfirm:edit")
    public String toEdit(Model model) {
        StudentSceneConfirmInfo confirmInfo;
        String id = RequestUtil.getString("id");
        String empId =  RequestUtil.getString("empId");
        Assert.hasText(id, "id参数名称不能为空");
        Assert.hasText(empId, "empId参数名称不能为空");
        confirmInfo = studentSceneConfirmService.getInfoById(id);
        model.addAttribute("dataInfo", confirmInfo);
        model.addAttribute("regInfo",studentSceneConfirmService.getSceneRegisterList(confirmInfo.getLearnId(),empId));
        model.addAttribute("modifyInfo",studentSceneConfirmService.getExamNoModifyRecord(confirmInfo.getLearnId()));
        return "recruit/studentSceneConfirm/scene_confirm_edit";
    }

    @RequestMapping("/findAllSceneConfirm")
    @Rule("sceneConfirm:query")
    @ResponseBody
    public Object findAllSceneConfirm(@RequestParam(value = "start", defaultValue = "1") int start,
                                      @RequestParam(value = "length", defaultValue = "10") int length, StudentSceneConfirmQuery query) {
        List<StudentSceneConfirmInfo> resultList = studentSceneConfirmService.findAllSceneConfirm(query,start,length);
        return new IPageInfo((Page) resultList);
    }

    @RequestMapping("/updateConfirmInfo")
    @Rule("sceneConfirm:update")
    @ResponseBody
    public Object updateConfirmInfo(String learnId,String stdId,String examNo,String remark){
        studentSceneConfirmService.updateConfirmInfo(learnId,stdId,examNo,remark);
        return "SUCCESS";
    }
}
