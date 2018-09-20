package com.yz.api;


import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BdScholarshipStoryService;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdsScholarshipStoryApiImpl implements BdsScholarshipStoryApi {

	@Autowired
	private BdScholarshipStoryService bdScholarshipStoryService;
	


	@Override
	public Object scholarshipStoryList(Header header, Body body) throws IRpcException {
		
		return bdScholarshipStoryService.selectBdScholarshipStory();
	}



	@Override
	public Object selectBdScholarshipStoryInfoById(Header header, Body body) throws IRpcException {
		
		return bdScholarshipStoryService.selectBdScholarshipStoryInfoById(body.getString("scholarshipId"));
	}

}
