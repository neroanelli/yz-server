package com.yz.job.mq;

import com.google.common.collect.Maps;
import com.yz.mq.Event;
import com.yz.mq.KafkaConfig;
import com.yz.serializ.MQFstSerializer;
import com.yz.util.ExceptionUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @author lingdian
 */
@Configuration
public class MQConsumer {

	private static Logger log = LoggerFactory.getLogger(MQConsumer.class);
	private int pollTime = 5;
	private KafkaConsumer<String, Event> consumer;
	private volatile Status status = Status.INIT;

	/**
	 * 
	 * @param info
	 * @param groupId
	 */
	public void init(KafkaConfig info, String groupId) {
		Map<String, Object> props = Maps.newHashMap();
		props.put("key.deserializer", StringDeserializer.class);
		props.put("value.deserializer", MQFstSerializer.class);
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "5000");
		props.put("fetch.min.bytes", "1");
		props.put("max.partition.fetch.bytes", 102400);
		props.put("enable.auto.commit", "true");
		props.put("session.timeout.ms", "30000");
		props.put("bootstrap.servers", info.getBootstrapServers());
		props.put("group.id", groupId);
		consumer = new KafkaConsumer<>(props);
		Runtime.getRuntime().addShutdownHook(new Thread(this::close));
	}

	public void subscribe(List<String> topics) {
		if (consumer == null) {
			throw new IllegalArgumentException("cosumer init error, please check your config! ");
		}
		consumer.subscribe(topics, new ConsumerRebalanceListener() {
			long assigneStart;

			public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
				this.assigneStart = System.currentTimeMillis();
				log.info("mq-client Revoked ");
			}

			public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
				for (TopicPartition topicPartition : partitions) {
					log.info(
							"mq-client Assigned ,topic: {}, partitions: {} duration: "
									+ (System.currentTimeMillis() - this.assigneStart) + " ms",
							topicPartition.topic(), topicPartition.partition());
				}
			}
		});
		this.status = Status.RUNNING;
	}

	/**
	 * 
	 * @param callback
	 */
	public void consumer(MQConsumerCallBack<Event> callback) {
		if (MQConsumer.this.status == MQConsumer.Status.RUNNING) {
			try {
				ConsumerRecords<String, Event> records = this.consumer.poll(MQConsumer.this.pollTime);
				// HashMultimap<String, Event> events = HashMultimap.create();
				records.forEach(record -> {
					if ((record == null) || (record.value() == null)) {
						log.info("the record or record value is null, skip it!");
					} else {
						callback.execute(record.value());
					}
				});

			} catch (Exception e) {
				log.error("poll exception, e:", ExceptionUtil.getStackTrace(e));
			}
		}
	}

	private void close() {
		this.status = Status.STOPPING;
		log.info("MQ Consumer is stopping.......");
		consumer.unsubscribe();
		consumer.close();
		log.info("MQ Consumer is closed!");
		this.status = Status.STOPPED;
	}

	enum Status {
		INIT, RUNNING, STOPPING, STOPPED;

		Status() {
		}
	}

}
