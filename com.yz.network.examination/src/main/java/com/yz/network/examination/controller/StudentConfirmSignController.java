package com.yz.network.examination.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.network.examination.croe.annotation.Rule;
import com.yz.network.examination.model.BdConfirmStudentInfo;
import com.yz.network.examination.service.StudentConfirmSignService;



/**
 * @Description:现场确认签到
 * @Author: luxing
 * @Date 2018\8\8 0008 10:47
 **/
@Controller
@RequestMapping("/confirmSign")
public class StudentConfirmSignController {

    @Autowired
    private StudentConfirmSignService studentConfirmSignService;

    /**
     * 登录签到系统
     * @param
     * @return
     */
    @RequestMapping("/loginSign")
    @ResponseBody
    @Rule
    public Object loginSign(){
        return studentConfirmSignService.loginSign();
    }

    /**
     * 刷身份证签到
     * @param searchInfo
     * @return
     */
    @RequestMapping("/brushCard")
    @ResponseBody
    @Rule
    public Object brushCard(@RequestParam(name = "searchInfo")String searchInfo){
        return studentConfirmSignService.brushCard(searchInfo);
    }

    /**
     * 输入身份证签到
     * @param searchInfo
     * @return
     */
    @RequestMapping("/inputCard")
    @ResponseBody
    @Rule
    public Object inputCard(@RequestParam(name = "searchInfo") String searchInfo){
        return studentConfirmSignService.inputCard(searchInfo);
    }

    @RequestMapping("/confirmSign")
    @ResponseBody
    @Rule
    public Object confirmSign(BdConfirmStudentInfo bdConfirmStudentInfo){
        return studentConfirmSignService.confirmSign(bdConfirmStudentInfo);
    }
}
