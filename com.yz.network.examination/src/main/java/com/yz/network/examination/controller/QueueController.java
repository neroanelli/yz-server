package com.yz.network.examination.controller;

import com.yz.network.examination.croe.annotation.Rule;
import com.yz.network.examination.croe.exception.ExamException;
import com.yz.network.examination.model.BdLearnQueueInfo;
import com.yz.network.examination.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ Author     ：林建彬.
 * @ Date       ：Created in 10:16 2018/8/8
 * @ Description：排号系统
 */
@Controller
@RequestMapping("/queue")
public class QueueController {

    @Autowired
    private QueueService queueService;

    /**
     * 获取排号表内容
     * @param pfsnLevel
     * @return
     */
    @RequestMapping("/getQueue")
    @ResponseBody
    @Rule
    public Object getQueueList(String pfsnLevel){
        List<BdLearnQueueInfo> bdLearnQueueInfos = queueService.getQueueList(pfsnLevel);
        return bdLearnQueueInfos;
    }

    /**
     * 点击下几位时更改状态
     * @param pfsnLevel
     * @param number
     * @return
     */
    @RequestMapping("/nextQueue")
    @ResponseBody
    @Rule
    public Object nextQueue(@RequestParam(name = "pfsnLevel", required = true)String pfsnLevel, @RequestParam(name = "number", required = true)Integer number){
        queueService.updateNextStatus(pfsnLevel,number);
        return "SUCCESS";
    }

    /**
     * 旁边的未预约插队
     * @param queueId
     * @return
     */
    @RequestMapping("/jumpQueue")
    @ResponseBody
    @Rule
    public Object jumpQueue(@RequestParam(name = "queueIds", required = true)String[] queueIds){
        if(null == queueIds || queueIds.length == 0){
            throw new ExamException("E100001","请选择要排队的人！");
        }
        queueService.jumpQueue(queueIds);
        return "SUCCESS";
    }

}
