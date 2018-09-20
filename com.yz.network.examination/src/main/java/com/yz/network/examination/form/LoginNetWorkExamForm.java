package com.yz.network.examination.form;

import java.nio.charset.Charset;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.provider.ValidCodeNetWorkExamProvider;
import com.yz.redis.RedisService;

/**
 * 
 * @desc 网报系统{登录}
 * @author lingdian
 *
 */
@YzNetWorkForm(handler = NetWorkExamConstants.LOGIN_NETWORKEXAMINATION_HANDLER)
public class LoginNetWorkExamForm extends BaseNetWorkExamForm {

	public LoginNetWorkExamForm() {
		super();
		this.setName(LOGIN_NETWORK_EXAM_NAME);
		this.setNeedLogin(true);
		this.setLoginFrm(true);
		this.addParam("dlfs","1"); 
		this.setFrmCode(NetWorkExamConstants.FRM_COMMON_REPCODE);
	}

	public LoginNetWorkExamForm(String id) {
		super(id);
		this.setName(LOGIN_NETWORK_EXAM_NAME);
		this.setNeedLogin(true);
		this.setLoginFrm(true);
		this.addParam("dlfs","1"); 
		this.setFrmCode(NetWorkExamConstants.FRM_COMMON_REPCODE);
	}

	public void addValidCode() { 
		this.delParam("verifyCodeImg"); //删除之前的参数 validCode？
		this.addParam("verifyCodeImg", context.getBean(ValidCodeNetWorkExamProvider.class));
	}

	@Override
	public HttpUriRequest getRequest() {
		HttpPost login = new HttpPost(LOGIN_NETWORK_EXAM_ACTION);
		login.addHeader("Content-Type", "application/x-www-form-urlencoded");
		//login.addHeader("Host",this.getDomain());
		login.setEntity(new UrlEncodedFormEntity(wrapperParm(), Charset.forName("gbk")));
		return login;
	}

	@Override
	public boolean isStart() {
		return RedisService.getRedisService().hexistsarr(this.getId(), "networkCookie");
	}

}
