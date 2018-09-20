package com.yz.edu.domain.executor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.constants.JDConstants;
import com.yz.edu.cmd.JdExchangeCmd;
import com.yz.edu.domain.JdGoodsSalesDomain;
import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.util.JsonUtil;

/**
 * 
 * @author lingdian
 *
 */
public class JdExchangeExecutor {

	private static Logger logger = LoggerFactory.getLogger(JdExchangeExecutor.class);

	/**
	 * 京东下单(预下单 ，锁单操作)
	 * 
	 * @param skuId
	 *            京东skuId
	 * @param exchangeCount
	 *            兑换个数
	 * @param saId
	 *            地址id
	 * @param jdGoodsType
	 *            京东商品类型 0-实物, 1- 实体卡
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> submitJdOrder(JdGoodsSalesDomain domain, JdExchangeCmd cmd) {
		String token = (domain.getJdGoodsType() == 0) ? cmd.getJdAccessToken() : cmd.getJdEntityCardToken();
		StringBuilder sb = new StringBuilder();
		sb.append("token=" + token);
		sb.append("&thirdOrder=").append(cmd.getThirdOrder());
		sb.append("&sku=[{\"skuId\":" + domain.getSkuId() + ",\"num\":" + cmd.getExchangeCount()
				+ ",\"bNeedGift\":true}]");
		sb.append("&name=").append(cmd.getName());
		sb.append("&province=").append(cmd.getProvinceCode());
		sb.append("&city=").append(cmd.getCityCode());
		sb.append("&county=").append(cmd.getDistrictCode());
		sb.append("&town=").append(cmd.getStreetCode());
		sb.append("&address=").append(cmd.getAddress());
		sb.append("&mobile=").append(cmd.getMobile());
		sb.append("&email=").append(cmd.getEmail());
		sb.append("&invoiceState=2"); // 集中开票,运营确认
		sb.append("&invoiceType=").append(cmd.getInvoiceType()); // 电子发票 1 普通发票
																	// // 2
																	// 增值税发票 3
																	// // 电子发票
		sb.append("&selectedInvoiceTitle=5"); // 发票类型 单位
		sb.append("&companyName=").append(JDConstants.COMPANY_NAME); // 发票抬头
		sb.append("&invoiceContent=1"); // 明细
		sb.append("&paymentType=4"); // 在线支付
		sb.append("&isUseBalance=1"); // 余额
		sb.append("&submitState=").append(cmd.getSubmitState()); // 不预占库存 0 //
																	// 预占(可通过接口取消),1
																	// // 不预占
		sb.append("&regCode=").append(cmd.getRegCode()); // 纳税号
		sb.append("&invoicePhone=").append(cmd.getInvoicePhone()); // 收增票人电话
		logger.info("京东下单请求参数:--------" + sb.toString());
		String orderResult = HttpUtil.sendPost(JDConstants.SUBMIT_ORDER_URL, sb.toString(),
				HttpTraceInterceptor.TRACE_INTERCEPTOR);
		logger.info("京东下单结果:-------" + orderResult);
		return JsonUtil.str2Object(orderResult, Map.class);
	}

	/**
	 * @desc 确认预占库存订单接口
	 * @param domain
	 * @param cmd
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> confirmOrder(JdGoodsSalesDomain domain, JdExchangeCmd cmd) {
		String token = (domain.getJdGoodsType() == 0) ? cmd.getJdAccessToken() : cmd.getJdEntityCardToken();
		Map<String, Object> resultOjb = (Map<String, Object>) cmd.getExtendAttr().get("result");
		if (resultOjb != null && resultOjb.containsKey("jdOrderId")) {
			StringBuilder sb = new StringBuilder();
			sb.append("token=").append(token);
			sb.append("&jdOrderId=").append(resultOjb.get("jdOrderId"));
			String orderResult = HttpUtil.sendPost(JDConstants.CONFIRMORDER_ORDER_URL, sb.toString(),
					HttpTraceInterceptor.TRACE_INTERCEPTOR);
			logger.info("jdOrderId:{},cancel.result:{}", resultOjb.get("jdOrderId"), orderResult);
			return JsonUtil.str2Object(orderResult, Map.class);
		}
		return null;
	}

	/**
	 * @desc 取消未确认订单接口
	 * @param domain
	 * @param cmd
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static void cancel(JdGoodsSalesDomain domain, JdExchangeCmd cmd) {
		String token = (domain.getJdGoodsType() == 0) ? cmd.getJdAccessToken() : cmd.getJdEntityCardToken();
		Map<String, Object> resultOjb = (Map<String, Object>) cmd.getExtendAttr().get("result");
		if (resultOjb != null && resultOjb.containsKey("jdOrderId")) {
			StringBuilder sb = new StringBuilder();
			sb.append("token=").append(token);
			sb.append("&jdOrderId=").append(resultOjb.get("jdOrderId"));
			String orderResult = HttpUtil.sendPost(JDConstants.CANCEL_ORDER_URL, sb.toString(),
					HttpTraceInterceptor.TRACE_INTERCEPTOR);
			logger.info("jdOrderId:{},cancel.result:{}", resultOjb.get("jdOrderId"), orderResult);
		}
	}
}
