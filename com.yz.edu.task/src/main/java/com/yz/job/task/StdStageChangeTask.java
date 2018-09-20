package com.yz.job.task;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.yz.job.common.AbstractSimpleTask; 
import com.yz.job.common.YzJob;
import com.yz.job.constants.JobConstants;
import com.yz.job.service.StdStageChangeService;
import com.yz.task.YzTaskConstants;

/**
 * 
 * @desc 定时变更 学员状态 每天 8点,12点,18点
 * @author Administrator
 *
 */
@Component(value = "stdStageChangeTask")
@YzJob(taskDesc = YzTaskConstants.YZ_STD_STAGE_CHANGE_DESC, cron = "0 */5 * * * ?", shardingTotalCount = 1,
log =true,logFormat = JobConstants.JOB_STD_STGAGE_CHANGE_LOGFORMAT)
public class StdStageChangeTask extends AbstractSimpleTask {

	@Value("${changeStdStage.sourceStdStage}")
	private String sourceStdStage;                //源学员状态
	
	@Value("${changeStdStage.targetStdStage}")
	private String targetStdStage;               //目标学员状态
	
	@Value("${changeStdStage.scholarship}")
	private String scholarship;                  //优惠类型
	
	@Autowired
	private StdStageChangeService stdStageChangeService;

	@Override
	public void executeOther(ShardingContext shardingContext) {
		String[] strS = scholarship.split("/");
		for(int i=0;i<strS.length;i++){
			logger.info("changeInfo:{}",strS[i]+"-->"+sourceStdStage+"-->"+targetStdStage);
			stdStageChangeService.changeStdStageByCond(strS[i], sourceStdStage, targetStdStage);
		}
		
	}

 
}
