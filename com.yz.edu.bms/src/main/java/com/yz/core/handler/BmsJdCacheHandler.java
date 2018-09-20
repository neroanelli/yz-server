package com.yz.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yz.cache.common.CacheContent;
import com.yz.cache.handler.BaseRedisCacheHandler;
import com.yz.core.constants.AppConstants;
import com.yz.redis.RedisService;
import com.yz.util.ClassUtil;
import com.yz.util.StringUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LRUMap;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @desc 将数据存入缓存 Redis 缓存本地
 */
@Component(value = AppConstants.BMS_JD_CACHE_HANDLER)
public class BmsJdCacheHandler extends BaseRedisCacheHandler {


    private static LRUMap cache = new LRUMap(3000);

    @Override
    public void setCache(String redisName, String key, Object result,int cacheExpire) {
        RedisService.getRedisService(redisName).putObject(key, result,cacheExpire);
        cache.put(key, result);
        logger.info("setLocalCache:{}", key);
    }

    @SuppressWarnings({ "unchecked","rawtypes" })
	@Override
    public Object getCache(String redisName, String key, Class<?> cls) {
        if (cache.containsKey(key)) {
            logger.info("getLocalCache:{}", key);
            return cache.get(key);
        }
        String str = RedisService.getRedisService(redisName).get(key);
        if (StringUtil.isBlank(str)) {
            return null;
        }
        Object obj = JSON.parse(str);
        if(CacheContent.getInstance().getBoundSql()!=null)
        {
        	if (obj instanceof JSONObject) {
    			if (!cls.isInstance(Map.class)) {
    				obj = JSON.toJavaObject((JSONObject) obj, cls);
    			} 
    		} else if (obj instanceof JSONArray) {
    			List result = new ArrayList<>();
    			JSONArray arr = (JSONArray) obj;
    			Iterator<Object> iter = arr.iterator();
    			while (iter.hasNext()) {
    				Object next = iter.next();
    				if (ClassUtil.isBaseDataType(next.getClass())) {
    					result.add(next);
    				} else if (next instanceof JSONObject) {
    					result.add(JSON.toJavaObject((JSONObject) next, cls));
    				}
    			} 
    			obj= result;
    		}
        	logger.error("data.error:{}.please check again!", obj);
        }
        cache.put(key, obj);
        return obj;
    }

}
