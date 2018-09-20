package com.yz.edu.cmd;

import com.yz.edu.context.DomainContext;
import com.yz.edu.domain.JdGoodsSalesDomain;
import com.yz.edu.domain.YzBaseDomain;
import com.yz.edu.domain.hook.YzDomainExecuteHook;

/**
 * @desc 过兑换域收集京东兑换的价格
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class JdExchangeCollectCmd extends BaseCommand {

	private String salesId; // 兑换Id
	
	public JdExchangeCollectCmd(String salesId)
	{
		this();
		this.salesId = salesId;
	}

	public JdExchangeCollectCmd() {
		setStep(0);
		setDataCollectCmd(true);
		addDomainExecHooks(new YzDomainExecuteHook() {
			@Override
			public void postExecute(YzBaseDomain domain, BaseCommand cmd) {
				JdGoodsSalesDomain salesDomain =(JdGoodsSalesDomain)domain;
				DomainContext.getInstance().setContextAttr("salesPrice", salesDomain.getSalesPrice());
				DomainContext.getInstance().setContextAttr("salesName", salesDomain.getSalesName());
			}
		});
	}

	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}

	public String getSalesId() {
		return salesId;
	}

	@Override
	public Object getId() {
		return salesId;
	}

	@Override
	public String getMethodName() {
		return "info";
	}

	@Override
	public Class<?> getDomainCls() {
		return JdGoodsSalesDomain.class;
	}

}
