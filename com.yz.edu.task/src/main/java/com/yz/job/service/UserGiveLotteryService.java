package com.yz.job.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.constants.StudentConstants;
import com.yz.constants.UsConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.exception.IRpcException;
import com.yz.generator.IDGenerator;
import com.yz.job.common.YzTaskContext;
import com.yz.job.dao.UserGiveLotteryMapper;
import com.yz.job.model.GwWechatMsgTemplate;
import com.yz.job.model.UserLotteryTicketInfo;
import com.yz.model.UserLotteryEvent;
import com.yz.model.WechatMsgVo;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;
/**
 * 赠送抽奖机会
 * @author lx
 * @date 2018年7月13日 下午4:44:39
 */
@Service
public class UserGiveLotteryService {
	
	private static Logger logger = LoggerFactory.getLogger(UserGiveLotteryService.class);
	
	@Autowired
	private UserGiveLotteryMapper userGiveLotteryMapper;
	
	@Autowired
	private WechatSendMessageService wechatSendMessageService;
	
	public void giveLotteryTicket(UserLotteryEvent event){
		logger.info("giveLotteryTicket.event{}",JsonUtil.object2String(event));
		if(StringUtil.isNotBlank(event.getOperType())){
			if(event.getOperType().equals(UsConstants.GIVE_WAY_PAY)){ //缴费
				boolean hasGainTicket = false;

				for (String itemCode : event.getPayItems()) {
					String recruitType = event.getRecruitType();
					if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
						if ("Y0".equals(itemCode)) {
							hasGainTicket = true;
						}
					} else if (StudentConstants.RECRUIT_TYPE_GK.equals(recruitType)) {
						if ("Y1".equals(itemCode)) {
							hasGainTicket = true;
						}
					}
				}
				if (hasGainTicket) {
					// 赠送抽奖机会
					gainLoterryTicketByUserId(event.getUserId(),event.getScholarship(),event.getOperType());
				}
			}else{  //注册/绑定
				gainLoterryTicketByMobile(event.getMobile(),event.getUserId(),event.getOperType());
			}
		}else{
			logger.info("未知来源,无法赠送..........");
		}
	}
	/**
	 * 执行赠送(针对缴费)
	 * @param userId
	 * @param 优惠类型
	 */
	public void gainLoterryTicketByUserId(String userId,String scholarship,String operType) {
		try {
			if (!StringUtil.hasValue(userId)) {
				return;
			}
			//根据优惠类型查询活动
			Map<String, String> lottery = userGiveLotteryMapper.selectLotteryInfoByScholarship(scholarship);
			// 无活动 退出
			if (null == lottery) {
				return;
			}
			String lotteryId = lottery.get("lotteryId");
			logger.info("抽奖信息...=" +lotteryId );
			logger.info("start.....give....{}",userId);
			
			Map<String, String> userInfo = userGiveLotteryMapper.getUsBaseInfoByUserId(userId);
			logger.info("赠送机会的用户信息{}",JsonUtil.object2String(userInfo));
			//插入用户抽奖券(本人)
			giveSelfLotteryTicket(userInfo,operType,lottery);
			
			logger.info("end.....give....");
			String startTime = lottery.get("startTime"); //活动的开始时间
			String endTime = lottery.get("endTime");     //活动的结束时间
			
			int limitRegTime = NumberUtils.toInt(lottery.get("limitRegTime"));
			
			if(StringUtil.hasValue(userInfo.get("pId"))){               //有邀约人的情况下
				if(limitRegTime >0){                                   //如果有限制,则验证邀约人是否在活动期间内注册且缴费
					String pMobile = userInfo.get("pMobile");          //邀约人手机号
					logger.info("邀约人的手机号{}",pMobile);
					if(checkIfAccordCond(pMobile,startTime,endTime)){  //如果符合
						//赠送邀约人抽奖机会
						logger.info("邀约人符合赠送条件..........");
						giveParentLotteryTicket(userInfo,lottery);
					}
				}else{ //如果不限制,直接送
					//赠送邀约人抽奖机会
					giveParentLotteryTicket(userInfo,lottery);
				}
			}
		} catch (IRpcException e) {
			logger.error("-------------------------------------- 赠送抽奖券报错：" + e.getMessage());
		}
	}
	
	/**
	 * 执行赠送(针对注册/绑定)
	 * @param mobile
	 */
	public void gainLoterryTicketByMobile(String mobile,String userId,String operType){
		logger.info("用户{},通过手机号注册{}",userId,mobile);
		//根据手机先查到有没有对应的学业
		List<Map<String, String>> learnInfos = userGiveLotteryMapper.selectLearnIdsByMobile(mobile,null, null);
		logger.info("手机号{},对应的报读学业{}",mobile,JsonUtil.object2String(learnInfos));
		if(null != learnInfos && learnInfos.size()>0){
			Map<String, String> learnInfo  = learnInfos.get(0);
			if(null != learnInfo){
				//根据优惠类型查询活动
				Map<String, String> lottery = userGiveLotteryMapper.selectLotteryInfoByScholarship(learnInfo.get("scholarship"));
				// 无活动 退出
				if (null == lottery) {
					return;
				}
				//如果是绑定操作，则需要判断注册或者缴费的时候是否赠送过
				if(operType.equals(UsConstants.GIVE_WAY_BIND)){
					//验证是否赠送过
					if(userGiveLotteryMapper.ifSendTicket(lottery.get("lotteryId"), userId)<1){
						gainLoterryTicketByUserId(userId,learnInfo.get("scholarship"),operType);
					}
				}else{
					gainLoterryTicketByUserId(userId,learnInfo.get("scholarship"),operType);
				}
				
			}
		}
	}
	
	/**
	 * 根据用户手机号查询是否有对应的学业缴费了
	 * @param mobile
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public boolean checkIfAccordCond(String mobile,String startTime,String endTime) {

		List<Map<String, String>> learnInfos = userGiveLotteryMapper.selectLearnIdsByMobile(mobile,startTime, endTime);
		logger.info("手机号{},报读的学业信息{}",mobile,StringUtil.obj2String(learnInfos));
		if (learnInfos.isEmpty()) {
			return false;
		}
		for (Map<String, String> map : learnInfos) {
			String recruitType = map.get("recruitType");
			String learnId = map.get("learnId");
			String itemCode = null;
			if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
				itemCode = "Y0";
			} else if (StudentConstants.RECRUIT_TYPE_GK.equals(recruitType)) {
				itemCode = "Y1";
			}
			logger.info("时间段{}到{}的缴费",startTime,endTime);
			int payCount = userGiveLotteryMapper.selectTutionPaidCount(learnId, itemCode,startTime,endTime);
			logger.info("手机号{},是否缴费{}",mobile,payCount);
			if (payCount > 0) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 给自己赠送抽奖机会
	 */
	public void giveSelfLotteryTicket(Map<String, String> userInfo,String operType,Map<String, String> lottery){
		UserLotteryTicketInfo selfTicketInfo = new UserLotteryTicketInfo();
		selfTicketInfo.setTicketId(IDGenerator.generatorId());
		selfTicketInfo.setLotteryId(lottery.get("lotteryId"));
		selfTicketInfo.setGiveWayType(operType);
		selfTicketInfo.setUserId(userInfo.get("userId"));
		selfTicketInfo.setMobile(userInfo.get("mobile"));
		selfTicketInfo.setYzCode(userInfo.get("yzCode"));
		selfTicketInfo.setUserName(userInfo.get("realName"));
		
		userGiveLotteryMapper.insertLotteryTicket(selfTicketInfo);
		
		//发微信推送
		sendTicketInform(userInfo.get("openId"),lottery.get("lotteryName"),lottery.get("lotteryUrl"));
		YzTaskContext.getTaskContext().addEventDetail(userInfo.get("userId")+"/"+userInfo.get("mobile"), "赠送抽奖机会成功");
	}
	/**
	 * 给邀约人赠送抽奖机会
	 */
	public void giveParentLotteryTicket(Map<String, String> userInfo,Map<String, String> lottery){
		logger.info("给邀约人赠送抽奖机会{}",JsonUtil.object2String(userInfo));
		UserLotteryTicketInfo parentTicketInfo = new UserLotteryTicketInfo();
		parentTicketInfo.setTicketId(IDGenerator.generatorId());
		parentTicketInfo.setLotteryId(lottery.get("lotteryId"));
		parentTicketInfo.setGiveWayType(UsConstants.GIVE_WAY_INVITE);
		parentTicketInfo.setTriggerUserId(userInfo.get("userId"));
		parentTicketInfo.setTriggerUserMobile(userInfo.get("mobile"));
		parentTicketInfo.setTriggerYzCode(userInfo.get("yzCode"));
		parentTicketInfo.setTriggerUserName(userInfo.get("realName"));
		
		parentTicketInfo.setUserId(userInfo.get("pId"));
		parentTicketInfo.setMobile(userInfo.get("pMobile"));
		parentTicketInfo.setYzCode(userInfo.get("pYzCode"));
		parentTicketInfo.setUserName(userInfo.get("pRealName"));
		logger.info("give_parent_start........");
		userGiveLotteryMapper.insertLotteryTicket(parentTicketInfo);
		logger.info("give_parent_end........");
		//发微信推送
		sendTicketInform(userInfo.get("pOpenId"),lottery.get("lotteryName"),lottery.get("lotteryUrl"));
		YzTaskContext.getTaskContext().addEventDetail(userInfo.get("pId")+"/"+userInfo.get("pMobile"), "邀约人赠送抽奖机会成功");
	}
	/**
	 * 微信推送
	 * @param openId
	 * @param lotteryName
	 */
	private void sendTicketInform(String openId, String lotteryName,String lotteryUrl) {
		// 发送微信通知
		GwWechatMsgTemplate template = wechatSendMessageService.getWechatMsgTemplate(WechatMsgConstants.TEMPLATE_LOTTERY_TICKET_WARN);
		if (StringUtil.isNotBlank(openId)) {
			
			WechatMsgVo msgVo = new WechatMsgVo();
			msgVo.setTouser(openId);
			msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_LOTTERY_TICKET_WARN);
			msgVo.addData("lotteryName", lotteryName);
			msgVo.addData("now", DateUtil.getNowDateAndTime());
			msgVo.setIfUseTemplateUlr(false);
			msgVo.setExt1(lotteryUrl);
			wechatSendMessageService.sendWechatMsg(msgVo,template);
		}
	}
}
