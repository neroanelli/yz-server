package com.yz.network.examination.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @desc 网报系统补偿任务
 * @author lingdian
 *
 */
@Component(value = "netWorkExamCompensateTask")
public class NetWorkExamCompensateTask extends AbstractNetworkExamJob implements SimpleJob, NetWorkExamConstants {

	@Autowired
	private NetWorkExamStarter netWorkExamStarter;

	public NetWorkExamCompensateTask() {
		setCorn("0/5 * * * * ?");
		setDesc(NETWORKEXAMINATION_COMPENSATE_DESC);
		setJobType(JOB_TYPE_SIMPLE);
		setShardingTotalCount(5);
	}

	@Override
	public void execute(ShardingContext shardingContext) {
		String frmStr = RedisService.getRedisService().lpopDbIndex(NETWORKEXAMINATION_COMPENSATE_QUEUE, 0);
		if (StringUtil.isNotBlank(frmStr)) {
			BaseNetWorkExamForm form = JsonUtil.str2Object(frmStr, BaseNetWorkExamForm.class);
			logger.info("{}.CompensateTask.start:frmStr{},",NETWORKEXAMINATION_COMPENSATE_DESC, frmStr);
			netWorkExamStarter.start(form);
		}
	} 
}
