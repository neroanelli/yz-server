package com.yz.handler;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.yz.cache.handler.BccCacheHandler;
import com.yz.redis.RedisService;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;

@Component(value = "goodsSalesDetailCacheHandler")
/**
 * @desc 商品详情缓存处理程序
 * @author lingdian
 *
 */
public class GoodsSalesDetailCacheHandler extends BccCacheHandler {

	@Override
	public void setCache(String redisName, String key, Object result, int cacheExpire) {
		super.setCache(redisName, key, result, cacheExpire); // 调用父类设置缓存函数
		net.sf.json.JSONObject obj = (net.sf.json.JSONObject) result;
		RedisService.getRedisService().setex("yzGoodsSalesList-goodsCount-" + obj.getString("salesId"),
				obj.getString("goodsCount"), 86400);
	}

	@Override
	public Object getCache(String redisName, String key, Class<?> cls) {
		JSONObject detail = (JSONObject) super.getCache(redisName, key, Map.class);
		if (detail != null && !detail.isEmpty()) {
			String salesId = StringUtil.split(key, "-")[1];
			String salesType = StringUtil.split(key, "-")[2];
			String goodsCount = RedisService.getRedisService().get("yzGoodsSalesList-goodsCount-" + salesId);
			if (StringUtil.isNotBlank(goodsCount)) {
				detail.put("goodsCount", goodsCount);
			}
			Date startDate = DateUtil.convertDateStrToDate(StringUtil.obj2String(detail.getString("startTime")),
					DateUtil.YYYYMMDDHHMMSS_SPLIT);
			Date endDate = DateUtil.convertDateStrToDate(StringUtil.obj2String(detail.getString("endTime")),
					DateUtil.YYYYMMDDHHMMSS_SPLIT);
			// 抽奖
			if (StringUtil.equalsIgnoreCase(salesType, "2")) {
				String salesStatus = "2";
				Date currentDate = new Date();
				if (endDate != null) {
					salesStatus = "1"; // 已经结束
				} else {
					if (currentDate.getTime() < startDate.getTime()) {
						salesStatus = "2"; // 即将开始
					} else if (currentDate.getTime() > startDate.getTime()) {
						salesStatus = "3"; // 进行中
					}
				}
				detail.put("salesStatus", salesStatus);
			} else { // 兑换 竞拍
				String salesStatus = "2";
				Date currentDate = new Date();
				if (currentDate.getTime() > endDate.getTime()) {
					salesStatus = "1"; // 已经结束
				} else {
					if (currentDate.getTime() < startDate.getTime()) {
						salesStatus = "2"; // 即将开始
					} else if (currentDate.getTime() > startDate.getTime()) {
						salesStatus = "3"; // 进行中
					}
				}
				detail.put("salesStatus", salesStatus);
			}
		}
		return detail;
	}
}
