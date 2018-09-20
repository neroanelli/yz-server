package com.yz.app;

import java.util.List;
import java.util.Map; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yz.conf.DubboConfig;
import com.yz.conf.ObStarConfig;
import com.yz.conf.ObStarEsConfig;
import com.yz.redis.RedisService;
import com.yz.report.ReportJdbcDao;
import com.yz.util.ApplicationContextUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@SpringBootApplication(scanBasePackages = { "com.yz.conf", "com.yz.service", "com.yz.redis", "com.yz.dao",
		"com.yz.datasource", "com.yz.convert", "com.yz.dubbo", "com.yz.cache", "com.yz.mq", "com.yz.trace.aspect",
		"com.yz.edu.domain.*", "com.yz.report", "com.yz.oss", }, exclude = {
				WebSocketAutoConfiguration.class,
				RedisAutoConfiguration.class, 
				DataSourceAutoConfiguration.class, 
				JdbcTemplateAutoConfiguration.class,
				DubboConfig.class,
				ObStarEsConfig.class,
				ObStarConfig.class })
public class YzPushDataApplication {
	
	private static Logger logger = LoggerFactory.getLogger(YzPushDataApplication.class);

	/**
	 * {@link YzPushDataApplication}
	 * 
	 * @desc 将制定数据推送到制定Redis队列 arg[0] repCode代码 arg[1] rep参数 arg[2] redis队列名称
	 * @param args
	 */
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		if (args == null || args.length == 0) {
			logger.error("args参数错误！！");
			System.exit(1);
			return;
		}
		new SpringApplicationBuilder(YzPushDataApplication.class).bannerMode(Mode.CONSOLE).run(args);
		ReportJdbcDao reportJdbcDao = ApplicationContextUtil.getBean(ReportJdbcDao.class);
		String repCode = args[0];
		String param = args[1];
		String queueName = args[2];
		logger.info("repCode:=>" + repCode + "; param:=>" + param + ";queueName:=>" + queueName);
		Map<String, Object> paramMap = Maps.newHashMap();
		if (StringUtil.isNotBlank(param)) {
			Lists.newArrayList(StringUtil.split(param, "&")).parallelStream().forEach(v -> {
				paramMap.put(StringUtil.substringBefore(v, "="), StringUtil.substringAfter(v, "="));
			});
		}
		List list = (List)reportJdbcDao.getRepResultList(repCode, paramMap, Map.class);
		list.parallelStream().forEach(v->	{
			String data = JsonUtil.object2String(v);
			RedisService.getRedisService().lpush(queueName, JsonUtil.object2String(data));
			logger.info("queueName:=>" + queueName + ";data:=>"+ data);
		});  
	}
}
