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
import com.yz.network.examination.form.GetEducationNetWorkForm;
import com.yz.network.examination.form.LoginNetWorkExamForm;
import com.yz.network.examination.service.EducationCheckExamFrmService;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;

/**
 * 
 * @desc 学历验证task
 * @author lingdian
 *
 */
@Component(value = "updateEducationNetWorkExamTask")
public class UpdateEducationNetWorkExamTask extends AbstractNetworkExamJob implements SimpleJob, NetWorkExamConstants {

	@Autowired
	private NetWorkExamStarter start;

	@Value("${task.shardingTotalCount:1}")
	private int shardingTotalCount; // 任务的分片数

	@Autowired
	private EducationCheckExamFrmService service;

	@PostConstruct
	public void initTask() {
		setCorn("0/5 * * * * ?");
		setDesc(NETWORKEXAMINATION_GET_DIPLOMA_DESC);
		setJobType(JOB_TYPE_SIMPLE);
		setShardingTotalCount(shardingTotalCount);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(ShardingContext shardingContext) {
		List<String> regListInfo = RedisService.getRedisService().lpop(EDUCATIONGET_DATA_QUEUE, 5, 0);
		for (String string : regListInfo) {
			Map<String, String> r = JsonUtil.str2Object(string, Map.class);
			LoginNetWorkExamForm loginNetWorkExamForm = new LoginNetWorkExamForm(r.get("learnId"));
			loginNetWorkExamForm.addParam("id", r.get("username"));
			loginNetWorkExamForm.addParam("pwd", r.get("password"));
			loginNetWorkExamForm.addParam("dlfs", "1");
			loginNetWorkExamForm.addValidCode();
			start.start(loginNetWorkExamForm);
			if (loginNetWorkExamForm.getValue().isOk()) {
				GetEducationNetWorkForm form = new GetEducationNetWorkForm(r.get("learnId"));
				start.start(form);
			} else {
				if (loginNetWorkExamForm.getValue() != null && loginNetWorkExamForm.getValue().getResult() != null) {
					service.educationGet(loginNetWorkExamForm.getId(), r.get("username"),
							loginNetWorkExamForm.getValue().isOk() + "",
							loginNetWorkExamForm.getValue().getResult().toString());
				} else {
					service.educationGet(loginNetWorkExamForm.getId(), r.get("username"), "error", "登录异常");
				}
			}
		}
	}

}
