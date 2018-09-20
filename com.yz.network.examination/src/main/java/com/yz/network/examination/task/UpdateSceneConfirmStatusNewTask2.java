package com.yz.network.examination.task;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob; 
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.dao.RegNetWorkExamFrmMapper;
import com.yz.network.examination.form.GetConfrimInfoNetWorkExamForm;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;

/**
 * 专跑已预约确认的学员
 * @desc 更新考前确认状态
 * @author zhuliping
 *
 **/
@Component(value = "UpdateSceneConfirmStatusNewTask2")
public class UpdateSceneConfirmStatusNewTask2 extends AbstractNetworkExamJob implements SimpleJob, NetWorkExamConstants {

	@Autowired
	private NetWorkExamStarter netWorkExamStarter;

	@Autowired
	RegNetWorkExamFrmMapper netWorkExamFrmMapper;

	@Value("${task.shardingTotalCount:1}")
	private int shardingTotalCount; // 任务的分片数
	
	@PostConstruct
	public void initTask() {
		setCorn("0/20 * * * * ?");
		setDesc(NETWORKEXAMINATION_BASEINFO_DESC);
		setJobType(JOB_TYPE_SIMPLE);
		setShardingTotalCount(shardingTotalCount);
	}

	@Override
	public void execute(ShardingContext shardingContext) {
		List<String> list = RedisService.getRedisService().lpop(ALREADYRESERVD_DATA_QUEUE, 10, 0);
		
		list.forEach(pstring -> {
			Map<String, String> p = JsonUtil.str2Object(pstring, Map.class);
			logger.info("更新学员现场确认操作：{}",pstring);			
			String learnId = p.get("learn_id");
			GetConfrimInfoNetWorkExamForm baseInfoNetWorkExamForm = new GetConfrimInfoNetWorkExamForm(learnId);
			baseInfoNetWorkExamForm.setNeedLogin(true);
			netWorkExamStarter.start(baseInfoNetWorkExamForm);
		});
	}
}
