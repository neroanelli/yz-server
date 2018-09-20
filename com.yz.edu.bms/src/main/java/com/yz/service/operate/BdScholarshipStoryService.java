package com.yz.service.operate;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.operate.BdScholarshipStoryMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.operate.BdScholarshipStory;



@Service
@Transactional
public class BdScholarshipStoryService{

	@Autowired
	private BdScholarshipStoryMapper bdScholarshipStoryMapper;
	

	public IPageInfo<BdScholarshipStory> getScholarshipStory(int page,int pageSize,BdScholarshipStory scholarshipStory) {
		PageHelper.offsetPage(page, pageSize);
		List<BdScholarshipStory> scholarshipStoryList = bdScholarshipStoryMapper.getScholarshipStory(scholarshipStory);
		return new IPageInfo<>((Page<BdScholarshipStory>)scholarshipStoryList);
	}


	public void insert(BdScholarshipStory scholarshipStory) {
		scholarshipStory.setScholarshipId(IDGenerator.generatorId());
		scholarshipStory.setIsAllow("1");
		bdScholarshipStoryMapper.insert(scholarshipStory);
	}


	public void deleteScholarshipStory(String scholarshipId) {
		// TODO Auto-generated method stub
		bdScholarshipStoryMapper.deleteScholarshipStory(scholarshipId);
	}



	public void updateScholarshipStory(BdScholarshipStory scholarshipStory) {
		// TODO Auto-generated method stub
		bdScholarshipStoryMapper.updateScholarshipStory(scholarshipStory);
	}


	public void deleteScholarshipStoryByIdArr(String[] scholarshipIds){
		bdScholarshipStoryMapper.deleteScholarshipStoryByIdArr(scholarshipIds);
	}
	
	public void updateIsAllow(String scholarshipId,String isAllow){
		BdScholarshipStory scholarshipStory = new BdScholarshipStory();
		scholarshipStory.setScholarshipId(scholarshipId);
		scholarshipStory.setIsAllow(isAllow);
		BaseUser user = SessionUtil.getUser();
		scholarshipStory.setUpdateUser(user.getUserName());
		scholarshipStory.setUpdateUserId(user.getUserId());
		bdScholarshipStoryMapper.updateIsAllow(scholarshipStory);
	}

	public Map<String, Object> getOneScholarshipStory(String scholarshipId) {
		// TODO Auto-generated method stub
		return bdScholarshipStoryMapper.getOneScholarshipStory(scholarshipId);
	}

}
