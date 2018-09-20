package com.yz.job.dao;

import org.apache.ibatis.annotations.Param;

/**
 * 更改学员状态
 * @author lx
 * @date 2018年8月2日 下午6:27:03
 */
public interface StdStageChangeMapper {

	//获取要更改状态的学员数量
	public int getEnrollNumByCond(@Param("scholarship") String scholarship,@Param("sourceStdStage") String sourceStdStage);
	//修改学员报读状态
	public void changeStdStageByCond(@Param("scholarship") String scholarship,@Param("sourceStdStage") String sourceStdStage,@Param("targetStdStage") String targetStdStage);
}
