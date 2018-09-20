package com.yz.network.examination.task;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.dao.RegNetWorkExamFrmMapper;
import com.yz.network.examination.form.GetBaseInfoNetWorkExamForm;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.redis.RedisService;

/**
 *
 * @desc 网报采集学员报考基本信息任务
 * @author jiangyt
 *
 **/
//@Component(value = "netWorkExamBaseInfoTask")
public class NetWorkExamBaseInfoTask extends AbstractNetworkExamJob implements SimpleJob, NetWorkExamConstants {

	@Autowired
	private NetWorkExamStarter netWorkExamStarter;

	@Autowired
	RegNetWorkExamFrmMapper netWorkExamFrmMapper;

	public NetWorkExamBaseInfoTask() {
		setCorn("0/30 * * * * ?");
		setDesc(NETWORKEXAMINATION_BASEINFO_DESC);
		setJobType(JOB_TYPE_SIMPLE);
		setShardingTotalCount(10);
	}

	@Override
	public void execute(ShardingContext shardingContext) {
		 int shard = shardingContext.getShardingItem();
		 System.out.println("shard:"+shard);
		 List<Map<String, String>> list =
		 netWorkExamFrmMapper.getSceneConfirmCollectStudent(shard);
		 list.forEach(p -> {
		 String learnId = p.get("learn_id");
		 if (RedisService.getRedisService().incr("NetWorkExamBaseInfoTask_" +
		 learnId, 300) == 1) {
		 GetBaseInfoNetWorkExamForm baseInfoNetWorkExamForm = new
		 GetBaseInfoNetWorkExamForm(learnId);
		 baseInfoNetWorkExamForm.setNeedLogin(true);
		 netWorkExamStarter.start(baseInfoNetWorkExamForm);
		 }
		
		 });
	}
}
