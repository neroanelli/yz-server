package com.yz.edu.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yz.edu.constants.YzDomainConstants;
import com.yz.edu.consumer.DomainConsumeVo;
import com.yz.edu.model.EventSourceObject;
import com.yz.edu.mq.Processor;
import com.yz.mq.Event;
import com.yz.util.JsonUtil;

@Service(value = "eventSourceService")
@Processor(topic = YzDomainConstants.DOMAIN_EVNET_SOURCE_TOPIC)
/**
 * 
 * @desc 版本溯源
 * @author Administrator
 *
 */
public class EventSourceService extends TraceInfoService {

	private static final Logger logger = LoggerFactory.getLogger(EventSourceService.class);

	@Override
	public void execute(List<Event> events) throws Exception {
		if (events != null) {
			List<EventSourceObject> objs = events.parallelStream().map(v -> {
				DomainConsumeVo consumerVo = v.cast(v.getEvent(), DomainConsumeVo.class);
				EventSourceObject obj = EventSourceObject.wrapEventSourceObject(consumerVo);
				logger.info("EventSourceObject.param:{}", JsonUtil.object2String(obj));
				return obj;
			}).collect(Collectors.toList());
			this.saveTrace(objs);
		}
	}

}
