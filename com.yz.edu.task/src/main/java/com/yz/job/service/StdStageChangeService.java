package com.yz.job.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.job.common.YzJobInfo;
import com.yz.job.common.YzTaskContext;
import com.yz.job.dao.StdStageChangeMapper;

@Service
public class StdStageChangeService {
	
	@Autowired
	private StdStageChangeMapper stdStageChangeMapper;
	
	/**
	 * 更改报读类型
	 * @param scholarship
	 * @param sourceStdStage
	 * @param targetStdStage
	 */
	public void changeStdStageByCond(String scholarship,String sourceStdStage,String targetStdStage){
		int enrollNum = stdStageChangeMapper.getEnrollNumByCond(scholarship, sourceStdStage);
		if(enrollNum >0){
			stdStageChangeMapper.changeStdStageByCond(scholarship, sourceStdStage, targetStdStage);
			// 增加task日志
			YzJobInfo info = YzTaskContext.getTaskContext().getContext(YzJobInfo.class);
			YzTaskContext.getTaskContext().addEventDetail("优惠类型:"+scholarship, String.format(info.getLogFormat(),
					sourceStdStage,targetStdStage,enrollNum));
		}
		
	}

}
