package com.yz.network.examination.dao;

import org.apache.ibatis.annotations.Param;

/**
 * 学历验证
 */
public interface EducationCheckExamFrmMapper {

	/**
	 * 更新学历验证状态
	 * @param status 0-不合格/1-合格/2-通过（应届生）/3-往届待验/4-无需验证'
	 * @return
	 */
	public int updateEducationStatus(@Param("learnId") String learnId,@Param("status") String status);
	
	
	public int updateEducationStatusTest(@Param("learnId") String learnId,@Param("username") String username,@Param("status") String status,@Param("remark") String remark);
	
	public int updateEducationStatusGet(@Param("learnId") String learnId,@Param("username") String username,@Param("status") String status,@Param("remark") String remark);
}
