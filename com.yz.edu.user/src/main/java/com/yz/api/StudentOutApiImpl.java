package com.yz.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BdStudentOutService;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class StudentOutApiImpl implements StudentOutApi {

	@Autowired
	private BdStudentOutService bdStudentOutService;

	@Override
	public void studentOut(Header header, Body body) throws IRpcException {
		String learnId = body.getString("learnId");// 学籍ID
		String tempUrl = body.getString("tempUrl");// 上传的附件临时地址
		String remark = body.getString("remark");// 备注，原因
		String reason = body.getString("reason");
		String userId = header.getUserId();
		String empId = header.getEmpId();
		String realName = body.getString("realName");
		if (body.containsKey("userId")) {
			userId = body.getString("userId");
		}
		bdStudentOutService.addStudentOut(empId, learnId, userId, realName, tempUrl,reason, remark);
	}
	
	/**
	 * 退学记录
	 */
	@Override
	public Object studentOutList(Header header, Body body) throws IRpcException {
		String learnId = body.getString("learnId");// 学籍ID
		return bdStudentOutService.studentOutList(learnId);
	}

}
