package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface BdsScholarshipStoryApi {

	
	/**
	 * 奖学金故事首页
	 * @param header
	 * @param body
	 * @return
	 * @throws BusinessException
	 */
	@YzService(sysBelong="bds",methodName="scholarshipStoryList",methodRemark="奖学金故事首页",needLogin=false)
	public Object scholarshipStoryList(Header header, Body body) throws IRpcException;
	
	/**
	 * 奖学金链接页
	 * @param header
	 * @param body
	 * @return
	 * @throws BusinessException
	 */
	@YzService(sysBelong="bds",methodName="selectBdScholarshipStoryInfoById",methodRemark="奖学金链接页",needLogin=false)
	public Object selectBdScholarshipStoryInfoById(Header header, Body body) throws IRpcException;
}
