package com.yz.edu.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.edu.constants.YzDomainConstants;
import com.yz.serializ.FstSerializer;
 
/**
 * 
 * @desc domain 域基类
 * @author Administrator
 *
 */
public abstract class YzBaseDomain implements java.io.Serializable ,YzDomainConstants{

	protected transient static final Logger logger =LoggerFactory.getLogger(YzBaseDomain.class);
 
	private transient static final long serialVersionUID = 1L;

	private transient int expire = 3600*24;//过期时间  默认一天
	
	private int version = COMMON_DOMAIN_VERSION ; //domain版本号 
	
	private long lastModified ; // 最后修改时间

	
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	
	public long getLastModified() {
		return lastModified;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}
	
	public int getExpire() {
		return expire;
	} 
	
	
	/**
	 * 
	 * @desc 设置domain的序列化工具类 
	 * @return
	 */
	public FstSerializer getSerializer()
	{
	   return FstSerializer.getInstance();	
	}	
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	public int getVersion() {
		return version;
	}
	 
    /**
     * @desc 指定域的聚合根
     * @param key
     */
	public abstract void setId(Object key);

	/**
	 * @desc 获取域的聚合根
	 * @return
	 */
	public abstract Object getId();
}
