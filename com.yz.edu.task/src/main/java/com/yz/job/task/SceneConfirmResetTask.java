package com.yz.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.yz.job.common.AbstractSimpleTask;
import com.yz.job.common.YzJob;
import com.yz.job.service.SceneConfirmResetService;
import com.yz.task.YzTaskConstants;

/**
 * 
 * @desc 重置学员现场确认task
 * @author Administrator  --0 0/5 * * * ?
 *
 */
@Component(value = "sceneConfirmResetTask")
@YzJob(taskDesc = YzTaskConstants.YZ_SceneConfirmReset_TASK_DESC, cron = "0 30 21 * * ?", shardingTotalCount = 1)
public class SceneConfirmResetTask extends AbstractSimpleTask {

	@Autowired
	private SceneConfirmResetService sceneConfirmService;

	@Override
	public void executeOther(ShardingContext shardingContext) {
		sceneConfirmService.reSetJobUpdate();
	}
}
