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
import com.yz.network.examination.form.DiplomaNetWorkForm; 
import com.yz.network.examination.form.LoginNetWorkExamForm;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;

/**
 * 
 * @desc 学历验证task
 * @author lingdian
 *
 */
//@Component(value = "updateDiplomaNetWorkExamTask")
public class UpdateDiplomaNetWorkExamTask extends AbstractNetworkExamJob implements SimpleJob, NetWorkExamConstants{

	@Autowired
	private NetWorkExamStarter start ; 
	
	@Value("${task.shardingTotalCount:1}") 
	private int shardingTotalCount; // 任务的分片数
	
	@PostConstruct
	public void initTask(){
		setCorn("0/5 * * * * ?");
		setDesc(NETWORKEXAMINATION_CHECK_DIPLOMA_DESC);
		setJobType(JOB_TYPE_SIMPLE);
		setShardingTotalCount(shardingTotalCount);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(ShardingContext shardingContext) {
		List<String> regListInfo = RedisService.getRedisService().lpop(EDUCATION_UPDATE_DATA_QUEUE, 5, 0);
		regListInfo.parallelStream().map(v->JsonUtil.str2Object(v,Map.class)).forEach(map->{
			    Map<String,String>r = (Map<String,String>)map;
			    LoginNetWorkExamForm loginNetWorkExamForm =new LoginNetWorkExamForm(r.get("learnId"));
			    loginNetWorkExamForm.addParam("id",r.get("username"));
			    loginNetWorkExamForm.addParam("pwd",r.get("password"));
			    loginNetWorkExamForm.addParam("dlfs","1");
			    loginNetWorkExamForm.addValidCode();
			    start.start(loginNetWorkExamForm);
			    if(loginNetWorkExamForm.getValue().isOk())
			    {
			    	  DiplomaNetWorkForm form = new DiplomaNetWorkForm(r.get("learnId"));
					  start.start(form);
			    } });
	}

}
