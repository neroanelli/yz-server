package com.yz.app;

 

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.yz.pay.YzPayCertService;
import com.yz.redis.RedisService;
import com.yz.service.ZmSupplementService;
import com.yz.util.ApplicationContextUtil; 

@SpringBootApplication(scanBasePackages = {
		"com.yz.conf", 
		"com.yz.service",
		"com.yz.redis",
		"com.yz.dao",   
		"com.yz.datasource",   
		"com.yz.convert",
		"com.yz.dubbo",
		"com.yz.cache",
		"com.yz.mq",
		"com.yz.trace.aspect",
		"com.yz.edu.domain.*",
		"com.yz.report",
		"com.yz.oss",
}, exclude = { WebSocketAutoConfiguration.class,
		RedisAutoConfiguration.class, DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class })
public class MyFixApplication {
	/** 
	 * {@link YzPayCertService}
	 * @desc 用户启动类
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		new SpringApplicationBuilder(MyFixApplication.class).bannerMode(Mode.CONSOLE).run(args);
		String operType = RedisService.getRedisService().get("fixOperType");
		if(operType.equals("1")){  // push 批量网报
			ApplicationContextUtil.getBean(ZmSupplementService.class).pushDataToRedis();
		}else if(operType.equals("2")){ //找回预报名
			ApplicationContextUtil.getBean(ZmSupplementService.class).pushDataToRedisForfindParam();
		}else if(operType.equals("3")){ //获取预报名
			ApplicationContextUtil.getBean(ZmSupplementService.class).pushDataToRedisForParam();
		}else if(operType.equals("4")){ //学历验证
			ApplicationContextUtil.getBean(ZmSupplementService.class).pushEducationCheck();
		}else if(operType.equals("5")){//学历查询
			ApplicationContextUtil.getBean(ZmSupplementService.class).pushEducationGet();
		}else if(operType.equals("6")){//主页学历查询
			ApplicationContextUtil.getBean(ZmSupplementService.class).pushEducationForLook();
		}else if(operType.equals("8")){//修改学历信息
			ApplicationContextUtil.getBean(ZmSupplementService.class).pushEducationForHuKou();
		}else if(operType.equals("7")){//更新待确认状态查询
			ApplicationContextUtil.getBean(ZmSupplementService.class).pushDataToSceneConfirm();
		}else if(operType.equals("9")){//扣除智米
			ApplicationContextUtil.getBean(ZmSupplementService.class).exchangeDeductionZhiMi();
		}
		
	}
	 
}
