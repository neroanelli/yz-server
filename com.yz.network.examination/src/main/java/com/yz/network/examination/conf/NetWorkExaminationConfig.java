package com.yz.network.examination.conf;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.yz.yunsu.DiscernBean;
import com.yz.yunsu.YunsuConfig;

@Configuration
public class NetWorkExaminationConfig  {
	
	@Value("${task.serverList}")
	private String serverList;
	
	@Value("${task.namespace}")
	private String namespace;

	@Bean(name = "yunsuConfig")
	public YunsuConfig initConfig() {
		return new YunsuConfig();
	}

	@Bean(name = "yzDiscernBean")
	public DiscernBean initDiscernBean(@Autowired YunsuConfig config) {
		DiscernBean.init(config);
		return null;
	}
 
	
	@Bean(name = "networkExamRegistry")
	public ZookeeperRegistryCenter regCenter() { 
		ZookeeperConfiguration conf = new ZookeeperConfiguration(serverList, namespace);
		conf.setConnectionTimeoutMilliseconds(300000);
		conf.setMaxRetries(10);
		conf.setSessionTimeoutMilliseconds(300000);
		ZookeeperRegistryCenter registry = new ZookeeperRegistryCenter( conf);
		registry.init();
		return registry;
	}
}
