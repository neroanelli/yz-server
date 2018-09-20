package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface StudentOutApi {

	@YzService(sysBelong="us",methodName="studentOut",methodRemark="申请退学",needLogin=true)
	void studentOut(Header header, Body body) throws IRpcException;
	
	@YzService(sysBelong="us",methodName="studentOutList",methodRemark="申请退学记录",needLogin=true)
	Object studentOutList(Header header, Body body) throws IRpcException;
}
