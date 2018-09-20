package com.yz.network.examination.vo;

/**
 * 
 * @desc 去支付
 * @author lingdian
 *
 */
public class ToPayResult implements java.io.Serializable {

	private String cookie;// cookie信息 
	
	private String payUrl ; // 去支付地址 

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	} 
}
