package com.yz.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.BdsPaymenItemtApi;
import com.yz.conf.YzSysConfig;
import com.yz.core.constants.StudentConstants;
import com.yz.dao.ImaqMapper;
import com.yz.exception.CustomException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ImaqService {

	private static final Logger log = LoggerFactory.getLogger(ImaqService.class);

	@Autowired
	private ImaqMapper imaqMapper;

	@Autowired
	private YzSysConfig yzSysConfig;

	@Reference(version = "1.0")
	private BdsPaymenItemtApi bdsPaymenItemtApi;

	public Object getQRCode(String picCollectId, String learnId) {
		//判断订单是否支付成功，防止误操作
		String orderNo = imaqMapper.getorderNoByPicCollectId(picCollectId);
		if (null != orderNo && orderNo.length() > 0){
			if(imaqMapper.getPayOrderByOrderNo(orderNo) > 0){
				throw new CustomException("你已成功支付过，请勿重复操作！");
			}
		}
		Header header = new Header();
		Body body = new Body();
		body.put("picCollectId",picCollectId);
		body.put("orderNo",orderNo);
		body.put("learnId",learnId);
		body.put("payItem", StudentConstants.PAY_ITEM_IMAQ);//标记该缴费为图像采集缴费
		body.put("amount", yzSysConfig.getPaymentItemImaqAmount());//费用
		return bdsPaymenItemtApi.itemPayByQRCode(header,body);
	}

    public Object getSPCInfoByLearnId(String learnId) {
        return imaqMapper.getSPCInfoByLearnId(learnId);
    }

	public String getSPCPictureNoByPicCollectId(String picCollectId) {
		return imaqMapper.getSPCPictureNoByPicCollectId(picCollectId);
	}

	public void updatePictureUrl(String picCollectId, String pictureFilename, String newPictureUrl) {
		imaqMapper.updatePictureUrl(picCollectId,pictureFilename,newPictureUrl);
	}

	public void infoError(String picCollectId, String errorMessage) {
		imaqMapper.infoError(picCollectId,errorMessage);
	}

	public boolean isFee(String orderNo) {
		return imaqMapper.getPayOrderByOrderNo(orderNo) > 0;
	}

	public void updatePictureStauts(String picCollectId) {
		imaqMapper.updatePictureStauts(picCollectId);
	}
}
