package com.yz.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.GsLotteryService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class GsLotteryApiImpl implements GsLotteryApi {

	@Autowired
	private GsLotteryService lotteryService;

	@Override
	public Object getLotteryInfo(Header header, Body body) throws IRpcException {
		return lotteryService.getLotteryInfo(body.getString("scholarship"), header.getUserId());
	}

	@Override
	public Object lottery(Header header, Body body) throws IRpcException {
		return lotteryService.lottery(header.getUserId(), body.getString("lotteryId"));
	}

	@Override
	public Object getWinningInfo(Header header, Body body) throws IRpcException {
		return lotteryService.getWinningInfo(header,body);
	}

	@Override
	public void setPrizeAddress(Header header, Body body) throws IRpcException {
		lotteryService.setPrizeAddress(header, body);
	}

	@Override
	public Object getAllWinningInfo(Header header, Body body) throws IRpcException {
		return lotteryService.getAllWinningInfo(header, body);
	}

}
