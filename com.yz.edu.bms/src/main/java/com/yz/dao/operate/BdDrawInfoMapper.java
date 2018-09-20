package com.yz.dao.operate;

import com.yz.model.operate.DrawRecord;
import com.yz.model.operate.GsLotteryTicket;
import com.yz.model.operate.LeaveMessage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BdDrawInfoMapper {

    /**
     * 查询抽奖机会信息
     * @param lotteryTicket
     * @return
     */
    List<GsLotteryTicket> getGsLotteryTicketList(GsLotteryTicket lotteryTicket);

    List<DrawRecord> getRecordList(DrawRecord drawRecord);
    
    /**
     * 查询报读留言信息
     * @param leaveMessage
     * @return
     */
    List<LeaveMessage> getLeaveMessageList(LeaveMessage leaveMessage);
    
    /**
     * 更新报读信息状态
     * @param leaveMessage
     */
    public void updateMessage(LeaveMessage leaveMessage);
    
    /**
     * 某个具体留言的信息
     * @param id
     * @return
     */
    Map<String, String> getLeaveMsgInfoById(@Param("id") String id);
}
