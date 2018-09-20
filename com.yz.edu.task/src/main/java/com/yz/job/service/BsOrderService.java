package com.yz.job.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.conf.YzSysConfig;
import com.yz.constants.FinanceConstants; 
import com.yz.edu.cmd.JdExchangeCmd; 
import com.yz.edu.domain.JdGoodsSalesDomain;
import com.yz.edu.domain.executor.JdExchangeExecutor; 
import com.yz.job.dao.BsOrderMapper;
import com.yz.job.model.BsLogistics;
import com.yz.job.model.BsOrderInfo;
import com.yz.job.model.BsSalesOrderInfo;
import com.yz.job.model.GsExChangePart;
import com.yz.redis.RedisService; 
import com.yz.util.JsonUtil; 

@Service(value = "bsOrderService")
public class BsOrderService {

	private static Logger logger = LoggerFactory.getLogger(BsOrderService.class);

	@Autowired
	private BsOrderMapper bsOrderMapper;
	
	@Autowired
	private YzSysConfig yzSysConfig;

	/**
	 * 
	 * @desc 查询需要同步的京东订单
	 * 
	 * @return
	 */
	public List<BsOrderInfo> querySynchronousJdOrderList() {
		logger.info("BsOrderService.querySynchronousJdOrderList.invoke");
		return bsOrderMapper.querySynchronousJdOrderList();
	}

	public void updateOrderStatus(List<BsOrderInfo> resultList) {
		logger.info("updateOrderStatus:{}.invoke", JsonUtil.object2String(resultList));
		bsOrderMapper.updateOrderStatus(resultList);
	}

	public void updateOrderCompleteTime(String jdOrderId, String orderTime) {
		logger.info("updateOrderCompletTime:{}{}.invoke", jdOrderId, orderTime);
		bsOrderMapper.updateOrderCompletTime(jdOrderId, orderTime);
	}

	/**
	 * @desc 将京东订单保存到本地
	 * @param body
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void addJDNewBsOrder(JdGoodsSalesDomain domain, JdExchangeCmd cmd) {
		//域中只是预占JD库存,只要能进入这里,保证可以下单成功
		//调用确认预占库存订单接口 确认下单
		boolean ifSuccsss = false;
		if(yzSysConfig.getSubmitState().equals("1")){
			Map<String, Object> result = JdExchangeExecutor.confirmOrder(domain, cmd);
			logger.info("首次确认下单结果:{}",result);
			if(null == result){
				//重试二次
				for(int i =0;i<3;i++){
					result = JdExchangeExecutor.confirmOrder(domain, cmd);
					if(null != result){
						logger.info("首次确认失败后第["+i+"]再次确认的用户{}",cmd.getUserId());
						break;
					}
				}
				//重试后还不行
				logger.info("retry.error.result:{}", result.get("resultMessage"));
			}
			if(result.containsKey("success") && (boolean)result.get("success")){
				ifSuccsss = true;
			}else{
				logger.error("京东确认下单结果失败.error:{}", result.get("resultMessage"));
			}
		}else{
			ifSuccsss = true;
		}
		if(ifSuccsss){
			// 主订单
			logger.info("下单域json：{}",JsonUtil.object2String(cmd.getExtendAttr()));
			Map<String, Object> resultOjb = (Map<String, Object>) cmd.getExtendAttr().get("result");
			String jdOrderId = String.valueOf(resultOjb.get("jdOrderId")); // 京东订单号
			String freight = String.valueOf(resultOjb.get("freight")); // 运费
			String orderPrice = String.valueOf(resultOjb.get("orderPrice")); // 订单总价格
			String orderNakedPrice = String.valueOf(resultOjb.get("orderNakedPrice")); // 订单裸价
			String orderTaxPrice = String.valueOf(resultOjb.get("orderTaxPrice")); // 订单税额

			BsOrderInfo orderInfo = new BsOrderInfo();
			orderInfo.setOrderType("1");
			int salesPrice = domain.getSalesPrice().intValue();
			int count = cmd.getExchangeCount();
			orderInfo.setTransAmount(String.valueOf(salesPrice * count));
			orderInfo.setOrderStatus("6"); // 下单即待收货
			orderInfo.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
			orderInfo.setUnit(domain.getUnit());

			orderInfo.setUserId(cmd.getUserId());
			orderInfo.setUserName(cmd.getUserName());
			orderInfo.setMobile(cmd.getUserMobile());

			orderInfo.setJdOrderId(jdOrderId);
			orderInfo.setFreight(freight);
			orderInfo.setOrderPrice(orderPrice);
			orderInfo.setOrderNakedPrice(orderNakedPrice);
			orderInfo.setOrderTaxPrice(orderTaxPrice);
			orderInfo.setOrderNo(cmd.getThirdOrder());
			logger.info("order info......." + JsonUtil.object2String(orderInfo));
			bsOrderMapper.addNewBsOrder(orderInfo);

			List<Map<String, String>> skuMap = (List<Map<String, String>>) resultOjb.get("sku");
			logger.info("skuMap.result:{}",JsonUtil.object2String(skuMap)); 
			if (null != skuMap) {
				for (Map<String, String> map : skuMap) {

					BsSalesOrderInfo salesOrderInfo = new BsSalesOrderInfo();
					salesOrderInfo.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
					salesOrderInfo.setCostPrice(domain.getCostPrice());
					salesOrderInfo.setGoodsCount(String.valueOf(cmd.getExchangeCount()));
					salesOrderInfo.setGoodsId(domain.getGoodsId());
					salesOrderInfo.setGoodsName(domain.getGoodsName());
					salesOrderInfo.setOrderNo(orderInfo.getOrderNo());
					salesOrderInfo.setOriginalPrice(domain.getOriginalPrice());
					salesOrderInfo.setSalesId(domain.getSalesId());
					salesOrderInfo.setSalesName(domain.getSalesName());
					salesOrderInfo.setSalesType(domain.getSalesType());
					salesOrderInfo.setTransAmount(orderInfo.getTransAmount());
					salesOrderInfo.setUnitPrice(String.valueOf(domain.getSalesPrice()));
					salesOrderInfo.setUnit(domain.getUnit());
					salesOrderInfo.setUserId(cmd.getUserId());
					salesOrderInfo.setGoodsType(String.valueOf(domain.getGoodsType()));
					salesOrderInfo.setGoodsImg(domain.getGoodsImg());
					salesOrderInfo.setSubOrderStatus("6"); // 下单即待收货

					salesOrderInfo.setSkuId(String.valueOf(map.get("skuId")));
					salesOrderInfo.setCategory(String.valueOf(map.get("category")));
					salesOrderInfo.setNakedPrice(String.valueOf(map.get("nakedPrice")));
					salesOrderInfo.setName(String.valueOf(map.get("name")));
					salesOrderInfo.setNum(String.valueOf(map.get("num")));
					salesOrderInfo.setOid(String.valueOf(map.get("oid")));
					salesOrderInfo.setPrice(String.valueOf(map.get("price")));
					salesOrderInfo.setTax(String.valueOf(map.get("tax")));
					salesOrderInfo.setTaxPrice(String.valueOf(map.get("taxPrice")));
					salesOrderInfo.setType(String.valueOf(map.get("type")));
					//salesOrderInfo.setSubOrderNo(IDGenerator.generatorId());
					logger.info("salesOrderInfo:{}",JsonUtil.object2String(salesOrderInfo));
					bsOrderMapper.addNewBsSalesOrder(salesOrderInfo);
				}
			}

			// 收货地址信息
			BsLogistics logistics = new BsLogistics();
			logistics.setAddress(cmd.getAddress());
			logistics.setUserName(cmd.getName());
			logistics.setProvince(cmd.getProvinceCode());
			logistics.setCity(cmd.getCityCode());
			logistics.setDistrict(cmd.getDistrictCode());
			logistics.setStreet(cmd.getStreetCode());
			logistics.setLogisticsName("京东自营快递");
			logistics.setMobile(cmd.getMobile());
			logistics.setLogisticsStatus("0"); // 0 是新建 1 是妥投 2 是拒收
			logistics.setOrderNo(orderInfo.getOrderNo());
			logistics.setProvinceName(cmd.getProvinceName());
			logistics.setCityName(cmd.getCityName());
			logistics.setDistrictName(cmd.getDistrictName());
			logistics.setStreetName(cmd.getStreetName());
			logistics.setTransportNo(jdOrderId);
			//logistics.setLogisticsId(IDGenerator.generatorId());
			logger.info("logistics:{}",JsonUtil.object2String(logistics));
			bsOrderMapper.saveOrderLogistics(logistics);

			// 修改兑换活动可兑换的数量
			bsOrderMapper.updateSalesCount(cmd.getExchangeCount(), cmd.getSalesId());

			GsExChangePart partInfo = new GsExChangePart();
			// 保存兑换记录
			partInfo.setHeadImgUrl(cmd.getHeadImgUrl());
			partInfo.setMobile(cmd.getUserMobile());
			partInfo.setUserName(cmd.getUserName());
			partInfo.setUserId(cmd.getUserId());
			//partInfo.setExchangeId(IDGenerator.generatorId());
			partInfo.setExchangeCount(String.valueOf(cmd.getExchangeCount()));
			partInfo.setSalesId(cmd.getSalesId());
			partInfo.setOrderNo(cmd.getThirdOrder());
			partInfo.setStatus(0);
			logger.info("partInfo:{}",JsonUtil.object2String(partInfo));
			bsOrderMapper.addGsExchange(partInfo);
			setGoodsSalesDetailCache(domain.getSalesType(), cmd, partInfo); 
		}
		
	}

	/**
	 * @desc 设置更新缓存
	 * @param salesType
	 * @param cmd
	 * @param partInfo
	 */
	@SuppressWarnings("rawtypes")
	private void setGoodsSalesDetailCache(String salesType, JdExchangeCmd cmd, GsExChangePart partInfo) {
		StringBuilder sb = new StringBuilder("yzGoodsSalesList-*-").append(cmd.getSalesId()).append("-").append(salesType);
	    RedisService.getRedisService().delByPattern(sb.toString()); 
		RedisService.getRedisService().incrBy("yzGoodsSalesList-goodsCount-" + cmd.getSalesId(), cmd.getExchangeCount() * -1);
	}

}
