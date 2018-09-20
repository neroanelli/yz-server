package com.yz.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.service.UsCertService;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class UsCertApiImpl implements UsCertApi {
	
	@Autowired
	private UsCertService certService;

	/*@Override
	public Map<String, String> refreshFollow(Body body) throws IRpcException {
		return certService.refreshFollow(body);
	}*/

	/*@Override
	public void clearFollow(String empId) {
		certService.clearFollow(empId);
	}*/

}
