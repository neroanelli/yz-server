package com.yz.job.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.constants.WechatMsgConstants;
import com.yz.job.dao.StudentSceneConfirmMapper;
import com.yz.job.dao.UserMapper;
import com.yz.model.WechatMsgVo;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;

@Service
public class SceneConfirmResetService {

	private static final Logger logger = LoggerFactory.getLogger(SceneConfirmResetService.class);

	@Autowired
	private StudentSceneConfirmMapper sceneConfirmMapper;
 
	@Autowired
	private UserMapper usInfoMapper;
	/**
	 * 重置学员在预约时间未前往现场确认
	 * @param jobInfo
	 * @throws ParseException 
	 */
	public void reSetJobUpdate() {
		int type=0;
		Calendar ca = Calendar.getInstance();
		int hour=ca.get(Calendar.HOUR_OF_DAY);//小时
		if(hour>15) {
			type=1;
		}
		logger.info("重置学员预约确认开始type:{}............",type);
		List<Map<String, String>> result=sceneConfirmMapper.getCurrentDateStudentSceneConfirm(type);
		if(result!=null&&result.size()>0) {
			result.stream().forEach(v ->{
				sceneConfirmMapper.reSetSceneConfirm(v.get("confirmId"), v.get("learnId"));
				
				//发送微信通知
				String userOpenId = usInfoMapper.getOpenIdByLearnId(v.get("learnId"));
				WechatMsgVo vo = new WechatMsgVo();
				vo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_TASK);
				vo.setTouser(userOpenId);
				vo.addData("now", DateUtil.getNowDateAndTime());
				vo.addData("first", "现场确认点预约通知");
				vo.addData("keyword1", "现场确认点重新预约");
				vo.addData("keyword2", "您好，由于您未能在预约的时间内前往现场确认点进行确认，您可重新进行预约，谢谢！");
				vo.addData("remark", "可前往“远智学堂”--“我的任务”进行再次预约。");
				vo.setIfUseTemplateUlr(false);
				RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(vo));
	    	} );
		}
		logger.info("重置学员预约确认结束type:{}............",type);
	}
}
