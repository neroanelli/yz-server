package com.yz.edu.consumer;

import com.yz.edu.cmd.BaseCommand;
import com.yz.edu.domain.YzBaseDomain;
import com.yz.model.BaseEvent;

/**
 * 
 * 
 * @desc domain 异步消费的vo
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class DomainConsumeVo extends BaseEvent {

	public DomainConsumeVo() {
		setTrace(true);
	}

	private YzBaseDomain domain;

	private BaseCommand cmd; 
	
	private String appName ; // 应用名称
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getAppName() {
		return appName;
	}

	public YzBaseDomain getDomain() {
		return domain;
	}

	public void setDomain(YzBaseDomain domain) {
		this.domain = domain;
	}

	public BaseCommand getCmd() {
		return cmd;
	}

	public void setCmd(BaseCommand cmd) {
		this.cmd = cmd;
	}
}