package com.yz.util;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps; 
/**
 * json工具类
 * @author cyf
 *
 */
public class JsonUtil {
	
	private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	
	
	private static SerializerFeature[] serializerFeature = { SerializerFeature.PrettyFormat,
			SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty,
			SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteNullListAsEmpty };

	
	
	/**
	 * 对象转JSON
	 * @param o
	 * @return
	 */
	public static String object2String(Object o) {
		if(o == null)
			return null;
		if(ClassUtil.isBaseDataType(o.getClass()))
			return o.toString();
		return JSON.toJSONString(o, serializerFeature);
	}
	
	
	/**
	 * map转换为对像
	 * 
	 * @param s
	 * @param clz
	 * @return
	 */
	public static <T> T map2Object(Map s, Class<T> clz) {
		return JSON.parseObject(object2String(s), clz);
	}
	
	
	/**
	 * json串转换为对象
	 * @param s
	 * @return
	 */
	public static Object str2Object(String s) {
		if(!StringUtil.hasValue(s))
			return null;
		
		if(!s.startsWith("{")) {
			if(s.startsWith("["))
				return JSONArray.parseArray(s);
			else 
				return s;
		}
		
		return JSONObject.parseObject(s);
	}
 
	
	/**
	 * 转换类型
	 * 
	 * @param s
	 * @param clz
	 * @return
	 */
	public static <T> T str2Object(String jsonStr, Class<T> clz) {
		if (StringUtil.isBlank(jsonStr)) {
			logger.error("json is null,param error!");
			return null;
		}
		return (T) JSON.parseObject(jsonStr, clz);
	}
	

	

	/**
	 * 
	 * @param jsonStr
	 * @param clz
	 * @return
	 */
	public static <T> List<T> str2List(String jsonStr, Class<T> clz) {
		if (StringUtil.isBlank(jsonStr)) {
			logger.error("json is null,param error!");
			return null;
		}
		return JSON.parseArray(jsonStr, clz);
	}
	
	
	// Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
	public static Map<String, Object> object2Map(Object obj) {
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = Maps.newHashMap();
		try {
			 map.putAll((JSONObject)JSON.toJSON(obj));
		} catch (Exception e) {
			logger.error("transBean2Map Error " + e);
		}
		return map;
	}
}
