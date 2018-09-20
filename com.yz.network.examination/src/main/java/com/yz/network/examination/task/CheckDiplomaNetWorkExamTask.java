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
import com.yz.network.examination.form.EducationCheckExamForm;
import com.yz.network.examination.form.LoginNetWorkExamForm;
import com.yz.network.examination.service.EducationCheckExamFrmService;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @desc 学历验证task
 * @author lingdian
 *
 */
@Component(value = "checkDiplomaNetWorkExamTask")
public class CheckDiplomaNetWorkExamTask extends AbstractNetworkExamJob implements SimpleJob, NetWorkExamConstants {

	@Autowired
	private NetWorkExamStarter start;

	@Value("${task.shardingTotalCount:1}")
	private int shardingTotalCount; // 任务的分片数

	@Autowired
	private EducationCheckExamFrmService service;

	@PostConstruct
	public void initTask() {
		setCorn("0/5 * * * * ?");
		setDesc(NETWORKEXAMINATION_CHECK_DIPLOMA_DESC);
		setJobType(JOB_TYPE_SIMPLE);
		setShardingTotalCount(shardingTotalCount);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(ShardingContext shardingContext) {
		List<String> regListInfo = RedisService.getRedisService().lpop(EDUCATIONCHECK_DATA_QUEUE, 5, 0);
		for (String string : regListInfo) {
			Map<String, String> r = JsonUtil.str2Object(string, Map.class);
			LoginNetWorkExamForm loginNetWorkExamForm = new LoginNetWorkExamForm(r.get("learnId"));
			loginNetWorkExamForm.addParam("id", r.get("username"));
			loginNetWorkExamForm.addParam("pwd", r.get("password"));
			loginNetWorkExamForm.addParam("dlfs", "1");
			loginNetWorkExamForm.addValidCode();
			start.start(loginNetWorkExamForm);
			if (loginNetWorkExamForm.getValue().isOk()) {
				if (StringUtil.equalsIgnoreCase(loginNetWorkExamForm.getValue().getResult().toString(), "confirm")) {
					service.educationCheck(r.get("learnId"), "1");
				} else {
					EducationCheckExamForm form = new EducationCheckExamForm(r.get("learnId"));
					form.addParam("ybmh", r.get("username"));
					form.addParam("xjxl", "1");
					form.addParam("zjh", r.get("zjdm"));
					form.addParam("xm", r.get("xm"));
					start.start(form);
				}
			} else {
				// logger.error("login.error{}",
				// JsonUtil.object2String(loginNetWorkExamForm));
				service.educationCheck(r.get("learnId"), "05");
				service.educationCheck(r.get("learnId"), r.get("username") + " " + r.get("password"), "05",
						loginNetWorkExamForm.getValue().getResult().toString());
			}
		}
	}

}
