package com.yz.edu.domain.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yz.edu.domain.YzBaseDomain;
import com.yz.report.ReportJdbcDao;
 

/**
 * 
 * @author Administrator
 *
 * @param <T>
 */
public abstract class BaseDomainLoader<T extends YzBaseDomain> {
    
	public Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public ReportJdbcDao reportJdbcDao; 
	
	//通过域聚合跟 加载
	public abstract T load(Object key,Class<?>cls);

}
