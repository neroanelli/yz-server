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
import com.yz.network.examination.form.LoginNetWorkExamForm;
import com.yz.network.examination.form.UpdateNetWorkExamForm;
import com.yz.network.examination.format.YzNetWorkFormater;
import com.yz.network.examination.service.EducationCheckExamFrmService;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;

/**
 * 
 * @desc 资料更新task
 * @author lingdian
 *
 */
@Component(value = "updateHuKouExamTask")
public class UpdateHuKouExamTask extends AbstractNetworkExamJob implements SimpleJob, NetWorkExamConstants {

	@Autowired
	private NetWorkExamStarter start;

	@Value("${task.shardingTotalCount:1}")
	private int shardingTotalCount; // 任务的分片数

	@Autowired
	private EducationCheckExamFrmService service;

	@PostConstruct
	public void initTask() {
		setCorn("0/5 * * * * ?");
		setDesc(NETWORKEXAMINATION_UPDATE_DIPLOMA_DESC);
		setJobType(JOB_TYPE_SIMPLE);
		setShardingTotalCount(shardingTotalCount);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(ShardingContext shardingContext) {
		List<String> regListInfo = RedisService.getRedisService().lpop(EDUCATION_UPDATE_1_DATA_QUEUE, 5, 0);
		regListInfo.parallelStream().map(v -> JsonUtil.str2Object(v, Map.class)).forEach(map -> {
			Map<String, String> r = (Map<String, String>) map;
			LoginNetWorkExamForm loginNetWorkExamForm = new LoginNetWorkExamForm(r.get("learn_id"));
			loginNetWorkExamForm.addParam("id", r.get("username"));
			loginNetWorkExamForm.addParam("pwd", r.get("password"));
			loginNetWorkExamForm.addParam("dlfs", "1");
			loginNetWorkExamForm.addValidCode();
			start.start(loginNetWorkExamForm);
			if (loginNetWorkExamForm.getValue() != null && loginNetWorkExamForm.getValue().isOk()) {
				UpdateNetWorkExamForm regForm = new UpdateNetWorkExamForm(r.get("learn_id"));
				logger.error("loginNetWorkExamForm.getValue:learn_id:{},username:{}", r.get("learn_id"),
						r.get("username"));
				regForm.addParam(r);
				regForm.addParam("pwd", NETTO_PWD_PREFIX + YzNetWorkFormater.pwd.formater(r.get("zjdm"), null));
				regForm.addParam("hidden_yzdm", "1"); // 写死
				regForm.addParam("ksid", r.get("username"));
				regForm.addParam("czbj", "1");
				String txdz = r.get("txdz");
				if (r.get("zsbpc1bkyx1").equals("11540")) { // 广金特殊处理
					regForm.delParam("txdz");
					regForm.addParam("txdz",
							"036" + (txdz.length() > 5 ? txdz.substring(txdz.length() - 5, txdz.length()) : txdz)
									+ NETTO_ADDRESS_SUFFIX);
				} else {
					regForm.delParam("txdz");
					regForm.addParam("txdz",
							(txdz.length() > 5 ? txdz.substring(txdz.length() - 5, txdz.length()) : txdz)
									+ NETTO_ADDRESS_SUFFIX);
				}
				start.start(regForm);
			}
		});
	}
}
