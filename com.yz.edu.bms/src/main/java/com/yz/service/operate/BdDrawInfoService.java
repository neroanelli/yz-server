package com.yz.service.operate;

import com.yz.dao.operate.BdDrawInfoMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.common.IPageInfo;
import com.yz.model.operate.DrawRecord;
import com.yz.model.operate.GsLotteryTicket;
import com.yz.model.operate.LeaveMessage;
import com.yz.redis.RedisService;
import com.yz.redis.hook.RedisOpHookHolder;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BdDrawInfoService {

    @Autowired
    private BdDrawInfoMapper bdDrawInfoMapper;

    public IPageInfo<GsLotteryTicket> getGsLotteryTicketList(int start, int length, GsLotteryTicket lotteryTicket) {
        PageHelper.offsetPage(start, length);
        List<GsLotteryTicket> lotteryTickets = bdDrawInfoMapper.getGsLotteryTicketList(lotteryTicket);
        return new IPageInfo<GsLotteryTicket>((Page<GsLotteryTicket>) lotteryTickets);
    }

    public IPageInfo<DrawRecord> getRecordList(int start, int length, DrawRecord drawRecord) {
        PageHelper.offsetPage(start, length);
        List<DrawRecord> list = bdDrawInfoMapper.getRecordList(drawRecord);
        return new IPageInfo<DrawRecord>((Page<DrawRecord>) list);
    }
    
    public IPageInfo<LeaveMessage> getLeaveMessageList(int start,int length,LeaveMessage leaveMessage){
    	PageHelper.offsetPage(start, length);
    	List<LeaveMessage> list = bdDrawInfoMapper.getLeaveMessageList(leaveMessage);
    	return new IPageInfo<LeaveMessage>((Page<LeaveMessage>) list);
    }
    
    public void updateMessage(LeaveMessage leaveMessage){
		Map<String, String> newMsgInfo = bdDrawInfoMapper.getLeaveMsgInfoById(leaveMessage.getId());
		if(leaveMessage.getIsShow().equals("1")){
			RedisService.getRedisService().zadd(
					YzTaskConstants.YZ_NEW_ENROLL_MSG_INFO+newMsgInfo.get("scholarship"),
					DateUtil.stringToLong(newMsgInfo.get("createTime"), DateUtil.YYYYMMDDHHMMSS_SPLIT) ,
					JsonUtil.object2String(newMsgInfo),
					RedisOpHookHolder.NEW_USER_REGLIST_HOOK);
		}else{
			RedisService.getRedisService().zrem(YzTaskConstants.YZ_NEW_ENROLL_MSG_INFO+newMsgInfo.get("scholarship"), JsonUtil.object2String(newMsgInfo));
		}
    	bdDrawInfoMapper.updateMessage(leaveMessage);
    }
}
