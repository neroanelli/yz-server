package com.yz.dao.operate;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


import com.yz.model.operate.BdScholarshipStory;

public interface BdScholarshipStoryMapper {

	/**
	 * 获取奖学金故事
	 * @param oaDiplomaTask
	 * @return
	 */
	List<BdScholarshipStory> getScholarshipStory(BdScholarshipStory scholarshipStory);
	
	
	/**
	 * 新增奖学金故事
	 * @param scholarshipStory
	 */
	void insert(BdScholarshipStory scholarshipStory);
	
	/**
	 * 删除
	 * @param diplomaId
	 */
	void deleteScholarshipStory(@Param("scholarshipId") String scholarshipId);
	
	/**
	 * 修改禁用和启用
	 * @param diplomaId
	 */
	void updateIsAllow(BdScholarshipStory scholarshipStory);
	
	 /**
     * 根据ID批量删除信息
     * @param delIds
     */
    void deleteScholarshipStoryByIdArr(@Param("scholarshipIds") String[] scholarshipIds);
	/**
	 * 修改
	 * @param diplomaId
	 */
	void updateScholarshipStory(BdScholarshipStory scholarshipStory);
	
	/**
	 * 得到一个奖学金故事对象
	 * @param diplomaId
	 * @return
	 */
	Map<String,Object> getOneScholarshipStory(@Param("scholarshipId") String scholarshipId);
}
