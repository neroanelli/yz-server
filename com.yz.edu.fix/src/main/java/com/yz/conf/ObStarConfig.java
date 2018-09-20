package com.yz.conf;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yz.mq.KafkaConfig;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Configuration
@AutoConfigureAfter(value = { ObStarEsConfig.class, KafkaConfig.class })
public class ObStarConfig {

	private static Logger logger = LoggerFactory.getLogger(ObStarConfig.class);

	private static final String OB_STAR_GROUP_ID = "yz-utrace-mq";

	@Bean(name = "yzObStarEsConfig")
	public ObStarEsConfig initObStarEsConfig() {
		return new ObStarEsConfig();
	}

	@Bean(name = "yzTransportClient")
	public TransportClient initTransportClient(@Autowired ObStarEsConfig config) throws Exception {
		Settings setting = Settings.builder()
				.put("client.transport.sniff", true)
				.put("client.transport.ignore_cluster_name", false)
				.put("client.transport.ping_timeout", "300s")
				.put("client.transport.nodes_sampler_interval", "300s")
				.put("cluster.name", config.getName())
				.build();
		TransportClient transportClient = new PreBuiltTransportClient(setting);
		String[] arr = StringUtil.split(config.getAddr(), "|");
		for (String str : arr) {
			String addr = StringUtil.substringBefore(str, ":");
			String port = StringUtil.substringAfter(str, ":");
			transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(addr),
					org.apache.commons.lang.math.NumberUtils.toInt(port)));
		}
		logger.info("config:{}", JsonUtil.object2String(config));
		return transportClient;
	}
}
