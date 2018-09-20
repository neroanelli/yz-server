package com.yz.network.examination.form;

import java.nio.charset.Charset;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.process.NetWorkExamFlow.NetWorkExamFlowEnum;
import com.yz.redis.RedisService;

/**
 * 
 * @desc 网报系统{注册}
 * @author lingdian
 *
 */
@YzNetWorkForm(
		 persistenceFrm = false,
		trigger = NetWorkExamFlowEnum.UN_INIT,
		handler = NetWorkExamConstants.REG_NETWORKEXAMINATION_HANDLER 
		,compensateCmd=NetWorkExamConstants.RETRY_NETWORKEXAMCOMPENSATE
		,interceptor = NetWorkExamConstants.VAILCODE_NETWORKEXAMINATION_INTERCEPTOR)
public class RegNetWorkExamForm extends BaseNetWorkExamForm {

	
	public RegNetWorkExamForm() {
		super();
		this.setName(REG_NETWORK_EXAM_NAME); 
	}
	
	public RegNetWorkExamForm(String id) {
		super(id);
		this.setName(REG_NETWORK_EXAM_NAME); 
	}


	@Override
	public HttpUriRequest getRequest() { 
		HttpPost reg = new HttpPost(REG_NETWORK_EXAM_ACTION);
		reg.setHeader("Cookie", RedisService.getRedisService().get("networksession"));
		reg.setEntity(new UrlEncodedFormEntity(wrapperParm(), Charset.forName("gbk")));
		return reg;
	} 
	

	@Override
	 // 判断该学员是否可以进行网报 ？
	public boolean isStart() { 
		return true;
	}
}
