package com.yz.service;

import com.alibaba.fastjson.JSONObject;
import com.yz.dao.BdsFullScholarshipMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.SessionInfo;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.redis.RedisService;
import com.yz.redis.hook.RedisOpHookHolder;
import com.yz.session.AppSessionHolder;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;

import net.sf.json.JSONArray;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @描述: 全额奖学金活动
 * @作者: DuKai
 * @创建时间: 2018/2/27 14:37
 * @版本号: V1.0
 */
@Service
public class BdsFullScholarshipService {

    @Autowired
    BdsFullScholarshipMapper bdsFullScholarshipMapper;


    public Map<String, Object> getEnrolmentCount(String scholarship){
        Map<String, Object> resultMap = new HashMap<>();
        //先从缓存中取
        String cacheKey = "enrolmentCount"+scholarship;
        String enrollKey = "initEnrollCount"+scholarship;
        String initEnrollCount = RedisService.getRedisService().get(enrollKey);
        String learnCount =RedisService.getRedisService().get(cacheKey);
        //如果缓存中没有就从数据库中取
        if(learnCount == null){
            resultMap = bdsFullScholarshipMapper.selectEnrolmentCount(scholarship);
            resultMap.put("learnCount", (NumberUtils.toInt(resultMap.get("learnCount").toString())+NumberUtils.toInt(initEnrollCount))+"");
            //设置缓存时长
            RedisService.getRedisService().setex(cacheKey, resultMap.get("learnCount").toString(), 3600*24*7);
        }else{
            resultMap.put("learnCount",(NumberUtils.toInt(learnCount)+NumberUtils.toInt(initEnrollCount))+"");
        }
        return resultMap;
    }

    public Object getNewEnrolmentList(String scholarship){
        Object obj = JSONArray.fromObject(bdsFullScholarshipMapper.selectNewEnrolmentList(scholarship));
        return obj;
    }
    /**
     * 报读评论列表信息
     * @param header
     * @param body
     * @return
     */
    public Object getEnrollMsgList(Header header, Body body) {
    	//走redis缓存
    	String scholarship = body.getString("scholarship");
		long now = new Date().getTime();
		Set<String> newMsgSet = RedisService.getRedisService().zrangeByScoreWithScores(YzTaskConstants.YZ_NEW_ENROLL_MSG_INFO+scholarship,  0, now, 0, 10);
		if (newMsgSet != null && !newMsgSet.isEmpty() && newMsgSet.size()>0) {
			return newMsgSet.stream().map(JSONObject::parseObject).collect(Collectors.toList());
		}else{
			List<Map<String, String>> dbNewMsgList = bdsFullScholarshipMapper.selectNewMsgList(scholarship);
			if(null !=dbNewMsgList && dbNewMsgList.size()>0 ){
				for(Map<String,String> info : dbNewMsgList){
					RedisService.getRedisService().zadd(YzTaskConstants.YZ_NEW_ENROLL_MSG_INFO+scholarship, 
							DateUtil.stringToLong(info.get("createTime"), DateUtil.YYYYMMDDHHMMSS_SPLIT) ,
							JsonUtil.object2String(info),
							RedisOpHookHolder.NEW_USER_REGLIST_HOOK);
				}
			}
			return dbNewMsgList;
		}
    }
    /**
     * 获取评论数
     * @param header
     * @param body
     * @return
     */
    public Object getMsgCount(Header header, Body body){
    	 Map<String, Object> resultMap = new HashMap<>();
    	String userId = header.getUserId();
    	String scholarship = body.getString("scholarship");
    	
        String cacheKey = "msgCount_"+scholarship+"_"+userId;
        String msgCount =RedisService.getRedisService().get(cacheKey);
        //如果缓存中没有就从数据库中取
        if(msgCount == null){
            resultMap.put("msgCount", bdsFullScholarshipMapper.getMsgCount(scholarship, userId));
            //设置缓存时长
            RedisService.getRedisService().setex(cacheKey, resultMap.get("msgCount").toString(), 3600*24*7);
        }else{
            resultMap.put("msgCount",msgCount);
        }
        return resultMap;
    }
    /**
     * 留言
     * @param header
     * @param body
     * @return
     */
    public Object enrollMsg(Header header, Body body){
	   	 String userId = header.getUserId();
		 String scholarship = body.getString("scholarship");
	     if(bdsFullScholarshipMapper.getMsgCount(scholarship, userId)>0){
	    	 throw new BusinessException("E200033");
    	 }
    	 String msgContent = body.getString("msgContent");
    	 SessionInfo session = AppSessionHolder.getSessionInfo(userId, AppSessionHolder.RPC_SESSION_OPERATOR);
    	 Map<String, String> enrollMap = new HashMap<>();
    	 enrollMap.put("id", IDGenerator.generatorId());
    	 enrollMap.put("userId", userId);
    	 enrollMap.put("nickname", session.getNickName());
    	 enrollMap.put("realName", session.getRealName());
    	 enrollMap.put("headImg", session.getHeadImg());
    	 enrollMap.put("mobile", session.getMobile());
    	 enrollMap.put("scholarship", scholarship);
    	 enrollMap.put("msgContent", msgContent);
    	 
    	 bdsFullScholarshipMapper.enrollMsg(enrollMap);
    	 
    	 String cacheKey = "msgCount_"+scholarship+"_"+userId;
         RedisService.getRedisService().setex(cacheKey, "1", 3600*24*7);
         
     	long now = new Date().getTime();

		Map<String, String> newMsgInfo = new HashMap<>();
		newMsgInfo.put("realName", session.getRealName());
		newMsgInfo.put("mobile", session.getMobile());
		newMsgInfo.put("headImg", session.getHeadImg());
		newMsgInfo.put("nickname", session.getNickName());
		newMsgInfo.put("msgContent", msgContent);
		newMsgInfo.put("createTime", DateUtil.getCurrentDate(DateUtil.YYYYMMDDHHMMSS_SPLIT));
		
		RedisService.getRedisService().zadd(
				YzTaskConstants.YZ_NEW_ENROLL_MSG_INFO+scholarship, now,
				JsonUtil.object2String(newMsgInfo),
				RedisOpHookHolder.NEW_USER_REGLIST_HOOK);
		
    	 return null;
    }

}
