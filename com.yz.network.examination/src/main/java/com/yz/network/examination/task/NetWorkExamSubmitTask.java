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
import com.yz.network.examination.form.RegNetWorkExamForm;
import com.yz.network.examination.format.YzNetWorkFormater;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;



/**
 * 
 * @desc 批量网报任务(专升本)
 * @author lingdian
 *
 */
@Component(value = "netWorkExamSubmitTask")
public class NetWorkExamSubmitTask extends AbstractNetworkExamJob implements SimpleJob, NetWorkExamConstants {
	
	@Autowired
	private NetWorkExamStarter start ; 
	 
	@Value("${task.shardingTotalCount:1}") 
	private int shardingTotalCount; // 任务的分片数
	
	/**
	 * @desc 网报调度表单
	 */
	@PostConstruct
	public void initTask(){
		setCorn("0/30 * * * * ?");
		setDesc(NETWORKEXAMINATION_SUBMIT_DESC);
		setJobType(JOB_TYPE_SIMPLE);
		setShardingTotalCount(shardingTotalCount);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(ShardingContext shardingContext) {
		//从redis获取上一次取数据的最大值 
	    List<String>regListInfo = RedisService.getRedisService().lpop(NETWORKEXAMINATION_DATA_QUEUE, 5, 0);
		regListInfo.parallelStream().map(v->JsonUtil.str2Object(v,Map.class)).forEach(map->{
			    Map<String,String>r = (Map<String,String>)map;
				RegNetWorkExamForm regForm = new RegNetWorkExamForm(r.get("learn_id"));
				regForm.addParam(r);
				regForm.addParam("pwd",NETTO_PWD_PREFIX+YzNetWorkFormater.pwd.formater(r.get("zjdm"), null));
				regForm.addParam("hidden_yzdm","1"); //写死
				regForm.addParam("czbj","0");
				String txdz = r.get("txdz");
				if(r.get("zsbpc1bkyx1").equals("11540")){  //广金特殊处理
					regForm.delParam("txdz");
					regForm.addParam("txdz","036"+(txdz.length()>5?txdz.substring(txdz.length() - 5, txdz.length()):txdz)+NETTO_ADDRESS_SUFFIX);
				}else{
					regForm.delParam("txdz");
					regForm.addParam("txdz",(txdz.length()>5?txdz.substring(txdz.length() - 5, txdz.length()):txdz)+NETTO_ADDRESS_SUFFIX);
				}
				start.start(regForm); 
		});
	}

}
