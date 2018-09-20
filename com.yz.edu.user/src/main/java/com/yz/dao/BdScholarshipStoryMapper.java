package com.yz.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.scholarshipStory.BdScholarshipStory;



public interface BdScholarshipStoryMapper {

	List<BdScholarshipStory> selectBdScholarshipStory();
	
	
	List<BdScholarshipStory> selectBdScholarshipStoryInfoById(@Param("scholarshipId") String scholarshipId);
}
