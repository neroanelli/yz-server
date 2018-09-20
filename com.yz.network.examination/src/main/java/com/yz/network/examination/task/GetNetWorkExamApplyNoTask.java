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
import com.yz.network.examination.form.GetApplyNoNetWorkForm;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;

/**
 * 获取预报名号
 * @author lx
 * @date 2018年9月1日 上午9:44:57
 */
@Component(value = "getNetWorkExamApplyNoTask")
public class GetNetWorkExamApplyNoTask extends AbstractNetworkExamJob implements SimpleJob, NetWorkExamConstants{

	@Autowired
	private NetWorkExamStarter start ; 
	
	@Value("${task.shardingTotalCount:1}") 
	private int shardingTotalCount; // 任务的分片数
	
	@PostConstruct
	public void initTask(){
		setCorn("0/5 * * * * ?");
		setDesc(NETWORKEXAMINATION_GET_APPLYNO_DESC);
		setJobType(JOB_TYPE_SIMPLE);
		setShardingTotalCount(shardingTotalCount);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void execute(ShardingContext shardingContext) {
		List<String> regListInfo = RedisService.getRedisService().lpop(NETWORKEXAMINATION_DATA_QUEUE_APPLYNO, 5, 0);
		regListInfo.parallelStream().map(v->JsonUtil.str2Object(v,Map.class)).forEach(map->{
			    Map<String,String>r = (Map<String,String>)map;
			    
				GetApplyNoNetWorkForm applyForm = new GetApplyNoNetWorkForm(r.get("frm_id"));
				//解析出密码和考试县区代码
				String xqShortAndPwd = r.get("frm_param");
			    String[] xqAndPwd = xqShortAndPwd.split("&");
			    String[] firstParam = xqAndPwd[0].split("=");
			    String[] secondParam = xqAndPwd[1].split("=");
			    applyForm.addParam(firstParam[0],firstParam[1]);
			    applyForm.addParam(secondParam[0],secondParam[1]);
			    
				start.start(applyForm); 
		});
	}

}
