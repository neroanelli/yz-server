package com.yz.controller.operate;

import com.yz.core.security.annotation.Rule;
import com.yz.model.operate.DrawRecord;
import com.yz.model.operate.GsLotteryTicket;
import com.yz.model.operate.LeaveMessage;
import com.yz.service.operate.BdDrawInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 报读抽奖管理
 * @Description: 
 * @date 2018年7月16日 下午3:35:18
 */
@Controller
@RequestMapping("/drawInfo")
public class BdDrawInfoController {

    @Autowired
    private BdDrawInfoService bdDrawInfoService;

    /**
     * 跳转到抽奖机会管理页面
     * @return
     */
    @RequestMapping("/toTicketList")
    @Rule("drawInfo:queryTicket")
    public String toTicketList() {
        return "operate/drawInfo/gsLotteryTicket-list";
    }

    /**
     * 查询抽奖机会列表
     * @param start
     * @param length
     * @param lotteryTicket
     * @return
     */
    @RequestMapping("/ticketList")
    @ResponseBody
    @Rule("drawInfo:queryTicket")
    public Object ticketList(@RequestParam(name = "start", defaultValue = "0") int start,
                             @RequestParam(name = "length", defaultValue = "10") int length, GsLotteryTicket lotteryTicket) {
        return bdDrawInfoService.getGsLotteryTicketList(start, length, lotteryTicket);
    }
    
    /**
     * 跳转到报读留言管理页面
     * @return
     */
    @RequestMapping("/toLeaveMessage")
    @Rule("drawInfo:leaveMessage")
    public String toLeaveMessage(){
    	return "operate/drawInfo/leaveMessage-list";
    }
    
    /**
     * 查询报读留言信息
     * @param start
     * @param length
     * @param leaveMessage
     * @return
     */
    @RequestMapping("/leaveMessageList")
    @ResponseBody
    @Rule("drawInfo:leaveMessage")
    public Object leaveMessageList(@RequestParam(name = "start", defaultValue = "0") int start,
                             @RequestParam(name = "length", defaultValue = "10") int length, LeaveMessage leaveMessage) {
        return bdDrawInfoService.getLeaveMessageList(start, length, leaveMessage);
    }
    
    /**
     * 更新报读信息显示状态
     * @param leaveMessage
     * @return
     */
    @RequestMapping("/updateLeaveMessage")
	//@Rule("drawInfo:updateMessage")
	@ResponseBody
	public Object updateLeaveMessage(LeaveMessage leaveMessage) {
    	bdDrawInfoService.updateMessage(leaveMessage);
		return null;
	}

    @RequestMapping("/toRecord")
    @Rule("drawInfo:queryRecord")
    public String toRecord() {
        return "operate/drawInfo/record-list";
    }

    @RequestMapping("/recordList")
    @ResponseBody
    @Rule("drawInfo:queryRecord")
    public Object recordList(@RequestParam(name = "start", defaultValue = "0") int start,
                             @RequestParam(name = "length", defaultValue = "10") int length, DrawRecord drawRecord) {
        return bdDrawInfoService.getRecordList(start, length, drawRecord);
    }

}
