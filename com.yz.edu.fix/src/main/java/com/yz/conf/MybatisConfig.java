package com.yz.conf;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yz.conf.MultiDataSourceConfig;
import com.yz.conf.YzSysConfig;
import com.yz.mq.KafkaConfig;
import com.yz.mq.MQProducer;
import com.yz.oss.OSSConfig;
import com.yz.oss.OSSUtil;
import com.yz.redis.YzRedisBean;
import com.yz.redis.YzRedisFactory;
import com.yz.util.ApplicationContextUtil; 

@Configuration
@AutoConfigureAfter(value ={ MultiDataSourceConfig.class }) 
public class MybatisConfig implements ApplicationContextAware {

	@Bean
	public MapperScannerConfigurer basicMapperScannerConfigurer() {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionTemplateBeanName("sqlSessionTemplate");
		mapperScannerConfigurer.setBasePackage("com.yz.dao");
		return mapperScannerConfigurer;
	}
 
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextUtil.setApplicationContext(applicationContext);
	
	}
	
	
	@Bean(name="yzRedisBean")
	public YzRedisBean getRedisBean() {
		return new YzRedisBean();
	}
	
	@Bean(name="yzRedisFactory")
	public Object initRedisFactory(@Autowired YzRedisBean bean) {
		YzRedisFactory factory = YzRedisFactory.getInstance();
		factory.init(bean);
		return factory;
	} 
	
	@Bean(name = "yzKafkaConfig")
	public KafkaConfig getKafkaConfig() {
		return new KafkaConfig();
	}

	@Bean(name = "mqProducer")
	public MQProducer initMqProducer(@Autowired KafkaConfig config) {
		MQProducer.getMQProducer().init(config);
		return null;
	}
	
	@Bean(name="yzSysConfig")
	public YzSysConfig getYzSysConfig() {
		return new YzSysConfig();
	}
	
	@Bean(name = "yzOSSConfig")
	public OSSConfig initOssConfig() {
		return new OSSConfig();
	}

	@Bean(name = "yzOSSUtil")
	public OSSUtil yzOSSUtil(@Autowired OSSConfig config) {
		OSSUtil.init(config);
		return null;
	}
}
