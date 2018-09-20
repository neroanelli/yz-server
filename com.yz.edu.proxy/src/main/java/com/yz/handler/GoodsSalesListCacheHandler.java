package com.yz.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.yz.cache.handler.BccCacheHandler;
import com.yz.redis.RedisService;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;

@Component(value = "goodsSalesListCacheHandler")
/**
 * @desc 商品列表缓存处理程序
 * @author lingdian
 *
 */
public class GoodsSalesListCacheHandler extends BccCacheHandler {

	@SuppressWarnings("unchecked")
	@Override
	public void setCache(String redisName, String key, Object result, int cacheExpire) {
		super.setCache(redisName, key, result, cacheExpire); // 调用父类设置缓存函数
		net.sf.json.JSONObject jsonObject = (net.sf.json.JSONObject) result;
		jsonObject.getJSONArray("list").parallelStream().forEach(data -> {
			net.sf.json.JSONObject obj = (net.sf.json.JSONObject) data;
			RedisService.getRedisService().setex("yzGoodsSalesList-goodsCount-" + obj.getString("salesId"),
					obj.getString("goodsCount"), 86400);
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getCache(String redisName, String key, Class<?> cls) {
		logger.info("loadKey{} from Redis", key);
		JSONObject jsonObject = (JSONObject) super.getCache(redisName, key, Map.class);
		if (jsonObject != null && !jsonObject.isEmpty()) {
			List<Map> list = (List<Map>) jsonObject.get("list");
			list.parallelStream().forEach(v -> {
				JSONObject obj = (JSONObject) v;
				String salesType = obj.getString("salesType");
				String goodsCount = RedisService.getRedisService().get("yzGoodsSalesList-goodsCount-" + obj.getString("salesId"));
				if (StringUtil.isNotBlank(goodsCount)) {
					obj.put("goodsCount", goodsCount);
				}
				// 抽奖 
				if (StringUtil.equalsIgnoreCase(salesType, "2")) {
					String salesStatus = "2";
					Date startDate = DateUtil.convertDateStrToDate(StringUtil.obj2String(obj.getString("startTime")), DateUtil.YYYYMMDDHHMMSS_SPLIT);
					Date currentDate = new Date();
					if (StringUtil.hasValue(StringUtil.obj2String(obj.getString("endTime")))) {
						salesStatus = "1"; // 已经结束
					} else {
						if (currentDate.getTime() < startDate.getTime()) {
							salesStatus = "2"; // 即将开始
						} else if (currentDate.getTime() > startDate.getTime()) {
							salesStatus = "3"; // 进行中
						}
					}
					obj.put("salesStatus", salesStatus);
				} else { //兑换 竞拍 
					String salesStatus = "2";
					Date startDate = DateUtil.convertDateStrToDate(StringUtil.obj2String(obj.getString("startTime")),
							DateUtil.YYYYMMDDHHMMSS_SPLIT);
					Date endDate = DateUtil.convertDateStrToDate(StringUtil.obj2String(obj.getString("endTime")),
							DateUtil.YYYYMMDDHHMMSS_SPLIT);
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
					obj.put("salesStatus", salesStatus);
				}
			});
		}
		return jsonObject;
	}
}
