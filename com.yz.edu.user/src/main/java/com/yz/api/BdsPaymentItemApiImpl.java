package com.yz.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BdsPaymentItemService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdsPaymentItemApiImpl implements BdsPaymenItemtApi {

	@Autowired
	private BdsPaymentItemService bdsPaymentItemService;

	@Override
	public Object itemPayByQRCode(Header header, Body body) throws IRpcException {
		return bdsPaymentItemService.imapPayByQRCode(body);
	}

	@Override
	public boolean itemPaySuccess(Header header, Body body) throws IRpcException {
		String orderNo=body.getString("serialNo");
		String outSerialNo=body.getString("outOrderNo");
		String wechatAmount=body.getString("amount");
		return bdsPaymentItemService.itemPaySuccess(orderNo, outSerialNo, wechatAmount);
	}
}
