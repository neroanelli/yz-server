package com.yz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.dao.BdScholarshipStoryMapper;
import com.yz.model.scholarshipStory.BdScholarshipStory;

import net.sf.json.JSONArray;

@Service
@Transactional
public class BdScholarshipStoryService {

	@Autowired
	private BdScholarshipStoryMapper bdScholarshipStoryMapper;
	
	public Object selectBdScholarshipStory(){
		return JSONArray.fromObject(bdScholarshipStoryMapper.selectBdScholarshipStory());
	}
	
	public Object selectBdScholarshipStoryInfoById(String scholarshipId){
		return JSONArray.fromObject(bdScholarshipStoryMapper.selectBdScholarshipStoryInfoById(scholarshipId));
	}
}
