package com.yz.edu.context;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yz.edu.cmd.BaseCommand;
import com.yz.edu.domain.YzBaseDomain;
import com.yz.util.StringUtil; 

/**
 * 
 * 
 * @desc domain 调用上下文环境
 * @author Administrator
 *
 */
public class DomainContext {
	
	private Logger logger = LoggerFactory.getLogger(DomainContext.class);

	/**
	 * 
	 * @desc 存储上下文环境信息
	 * 
	 */
	private ThreadLocal<Map<Object, Object>> context = ThreadLocal.withInitial(Maps::newHashMap);

	private DomainContext() {
	}

	private static class DomainContextHodler {
		private static DomainContext _instance = new DomainContext();
	}

	/**
	 * 
	 * @return
	 */
	public static DomainContext getInstance() {
		return DomainContextHodler._instance;
	}

	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public DomainContext setContext(String key, Object obj) {
		if(obj==null || key==null)
		{
			logger.error("key:{},obj:{} is null ",key,obj);
			return this;
		}
		if (this.context.get().containsKey(key)) {
			Object data = this.context.get().get(key);
			List set = Lists.newArrayList();
			if (data instanceof List) {
				set.addAll((List) data);
			} else {
				set.add(data);
			}
			set.add(obj);
			this.context.get().put(key, set);
		} else {
			this.context.get().put(key, obj);
		}
		return this;
	}

	/**
	 * 
	 * @param cls
	 * @param obj
	 */
	public DomainContext setContext(Class<?> cls, Object obj) {
		setContext(cls.getSimpleName(), obj);
		return this;
	}

	/**
	 * 
	 * @param obj
	 */
	public DomainContext setContext(Object obj) {
		setContext(obj.getClass().getSimpleName(), obj);
		return this;
	}

	/**
	 * 
	 * @param cls
	 * @param obj
	 */
	public <T> T getContext(Class<?> cls) {
		Object obj = this.context.get().get(cls.getSimpleName());
		return (T) obj;
	}

	/**
	 * 
	 * @desc 根据class类名 获取其对应的Set集合
	 * @param cls
	 * @return
	 */
	public <T> List<T> getList(String key) {
		Object obj = this.context.get().get(key);
		if (obj == null)
			return null;
		if (obj instanceof List) {
			return (List<T>) obj;
		}
		List<T> set = Lists.newArrayList();
		set.add((T) obj);
		return set;
	}

	/**
	 * 
	 * @desc 根据class类名 获取其对应的Set集合
	 * @param cls
	 * @return
	 */
	public <T> List<T> getList(Class<?> cls) {
		return this.getList(cls.getSimpleName());
	}
	
	/**
	 * 
	 * @param key 
	 */
	public Object getContextAttr(String key ) { 
		return this.getContextAttr(key, StringUtil.EMPTY);
	}

	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public Object getContextAttr(String key, Object obj) {
		Object src = this.context.get().get(key);
		if (src == null) {
			return obj;
		}
		return src;
	}

	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public Object getContextAttr(byte[] key, Object obj) {
		Object src = this.context.get().get(key);
		if (src == null) {
			return obj;
		}
		return src;
	}

	
	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public DomainContext setContextAttr(BaseCommand cmd, YzBaseDomain domain) {
		this.context.get().put(cmd,domain);
		return this;
	}
	
	
	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public DomainContext setContextAttr(String key, Object obj) {
		this.context.get().put(key, obj);
		return this;
	}

	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public DomainContext setContextAttr(byte[] key, Object obj) {
		this.context.get().put(key, obj);
		return this;
	}

	/**
	 * 
	 * 
	 * @desc 清空上下文信息
	 * 
	 */
	public void removeContext() {
		this.context.remove();
	}
}
