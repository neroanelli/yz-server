package com.yz.network.examination.form;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.util.StringUtil;

/**
 * 
 * @desc  验证码表单 
 * @author lingdian 
 *
 */
@YzNetWorkForm(
		persistenceFrm= false,
   handler = NetWorkExamConstants.VAILCODE_NETWORKEXAMINATION_HANDLER ,
   interceptor = NetWorkExamConstants.VAILCODE_NETWORKEXAMINATION_INTERCEPTOR
  )
public class VailCodeNetWorkForm extends BaseNetWorkExamForm {
	
	
    public VailCodeNetWorkForm() { 
    	this.setName(VAILCODE_NETWORK_EXAM_NAME);
    	this.setNeedLogin(false);// 共享session 
    	this.setDomain(NetWorkExamConstants.NETWORKEXAMINATION_DOMAIN); 
	}
    
    
    public VailCodeNetWorkForm(String id) {
    	super(id);
    	this.setName(VAILCODE_NETWORK_EXAM_NAME);
    	this.setNeedLogin(false);// 共享session 
    	this.setDomain(NetWorkExamConstants.NETWORKEXAMINATION_DOMAIN);
	}
 
	@Override
	public HttpUriRequest getRequest() { 
        HttpGet httpGetVali = new HttpGet(VAILCODE_NETWORK_EXAM_ACTION+"?t=" +System.currentTimeMillis());
        httpGetVali.addHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        httpGetVali.setHeader("Accept", "*/*");
        httpGetVali.setHeader("connection", "Keep-Alive"); 
        httpGetVali.addHeader("Host", "www.ecogd.edu.cn");
        httpGetVali.addHeader("Cache-Control", "no-cache");
        httpGetVali.addHeader("Referer", "http://www.ecogd.edu.cn/cr/cgbm/login.jsp");
        httpGetVali.addHeader("Content-Type", "image/jpeg");
        httpGetVali.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
        return httpGetVali;
	}
	

	@Override
	public boolean isStart() { 
		return true;
	}

}
