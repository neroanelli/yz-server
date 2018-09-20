package com.yz.network.examination.app;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
 
@SpringBootApplication(scanBasePackages = { "com.yz.cache", "com.yz.redis", "com.yz.datasource", "com.yz.conf",
		"com.yz.convert", "com.yz.yunsu", "com.yz.report", "com.yz.network.examination.task",
		"com.yz.network.examination.service", "com.yz.network.examination.controller",
		"com.yz.network.examination.handler", "com.yz.network.examination.interceptor",
		"com.yz.network.examination.conf", "com.yz.network.examination.http", "com.yz.network.examination.cmd",
		"com.yz.network.examination.croe.handler", "com.yz.network.examination.starter",
		"com.yz.network.examination.provider" }, exclude = { WebSocketAutoConfiguration.class,
				RedisAutoConfiguration.class, DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class,
				FreeMarkerAutoConfiguration.class })
public class YzNetWorkExaminationApp {

	/**
	 * @desc 启动应用程序入口
	 * @param args
	 */
	public static void main(String[] args) {
		new SpringApplicationBuilder(YzNetWorkExaminationApp.class).bannerMode(Mode.CONSOLE).web(true).run(args);
	}

}
