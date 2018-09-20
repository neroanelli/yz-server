package com.yz.network.examination.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.network.examination.dao.EducationCheckExamFrmMapper;

/**
 * 网报系统学历校验
 * 
 * @ClassName: EducationCheckExamFrmService
 * @Description: 专升本学历校验
 * @author zhanggh
 * @date 2018年8月31日
 *
 */
@Service(value = "educationCheckExamFrmService")
public class EducationCheckExamFrmService {
	private static final Logger log = LoggerFactory.getLogger(EducationCheckExamFrmService.class);

	@Autowired
	private EducationCheckExamFrmMapper checkMapper;

	/**
	 * 学历信息校验之后
	 * 
	 * @param learnId
	 * @param status
	 *            0-不合格/1-合格/2-通过（应届生）/3-往届待验/4-无需验证
	 */
	public void educationCheck(String learnId, String status) {
		checkMapper.updateEducationStatus(learnId, status);
	}

	public void educationCheck(String learnId, String username, String status, String remark) {
		checkMapper.updateEducationStatusTest(learnId, username, status, remark);
	}
	
	public void educationGet(String learnId, String username, String status, String remark) {
		checkMapper.updateEducationStatusGet(learnId, username, status, remark);
	}
}
