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
import com.yz.network.examination.form.FindApplyNoNetWorkForm;
import com.yz.network.examination.format.YzNetWorkFormater;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;

/**
 * 找回预报名号
 * @author lx
 * @date 2018年9月1日 上午10:56:17
 */
@Component(value = "findNetWorkExamApplyNoTask")
public class FindApplyNoNetWorkExamTask extends AbstractNetworkExamJob implements SimpleJob, NetWorkExamConstants{

	@Autowired
	private NetWorkExamStarter start ; 

	@Value("${task.shardingTotalCount:1}") 
	private int shardingTotalCount; // 任务的分片数
	
	@PostConstruct
	public void initTask(){
		setCorn("0/5 * * * * ?");
		setDesc(NETWORKEXAMINATION_FIND_APPLYNO_DESC);
		setJobType(JOB_TYPE_SIMPLE);
		setShardingTotalCount(shardingTotalCount);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void execute(ShardingContext shardingContext) {
		List<String> regListInfo = RedisService.getRedisService().lpop(NETWORKEXAMINATION_DATA_QUEUE_FIND_APPLYNO, 5, 0);
		regListInfo.parallelStream().map(v->JsonUtil.str2Object(v,Map.class)).forEach(map->{
			    Map<String,String>r = (Map<String,String>)map;
			    
				FindApplyNoNetWorkForm applyForm = new FindApplyNoNetWorkForm(r.get("frm_id"));
				//解析出密码和考试县区代码
				applyForm.addParam("pwd",NETTO_PWD_PREFIX+YzNetWorkFormater.pwd.formater(r.get("zjdm"), null));
				applyForm.addParam("xqshort",r.get("xqshort"));
			    applyForm.addParam("zjh",r.get("zjdm"));
			    applyForm.addParam("xm",r.get("xm"));
			    
				start.start(applyForm); 
		});
	}

}
