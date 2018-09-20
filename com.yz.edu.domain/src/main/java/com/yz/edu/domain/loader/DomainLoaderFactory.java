package com.yz.edu.domain.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

import com.google.common.collect.Maps;
import com.yz.edu.cmd.BaseCommand;
import com.yz.edu.domain.YzBaseDomain;
import com.yz.exception.CustomException;
import com.yz.util.ApplicationContextUtil;
import com.yz.util.ClassUtil;
import java.util.Map;

/**
 * 
 * @author Administrator
 *
 */
public class DomainLoaderFactory {

	private Logger logger = LoggerFactory.getLogger(DomainLoaderFactory.class);

	private Map<Class<?>, BaseDomainLoader<? extends YzBaseDomain>> domainMap = Maps.newConcurrentMap();

	private enum DomainLoaderFactoryHolder {
		_instance;
		private DomainLoaderFactory factory = new DomainLoaderFactory();

		public DomainLoaderFactory getFactory() {
			return this.factory;
		}
	}

	private DomainLoaderFactory() {

	}

	/**
	 * 
	 * @return
	 */
	public static DomainLoaderFactory getInstance() {
		return DomainLoaderFactoryHolder._instance.getFactory();
	}

	/**
	 * 
	 * @param cmd
	 * @return
	 */
	public BaseDomainLoader<? extends YzBaseDomain> getDomainLoader(Class<?> cls) {
		if (domainMap.containsKey(cls)) {
			return this.domainMap.get(cls);
		}
		return this.createDomainLoader(cls);
	}

	/**
	 * 
	 * @param cmd
	 * @return
	 */
	public BaseDomainLoader<? extends YzBaseDomain> getDomainLoader(BaseCommand cmd) {
		return this.getDomainLoader(cmd.getDomainCls());
	}

	/**
	 * 
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private BaseDomainLoader<? extends YzBaseDomain> createDomainLoader(Class<?> domainCls) {
		String[] str = ApplicationContextUtil.getApplicationContext().getBeanNamesForAnnotation(DomainLoader.class);
		if (str == null) {
			logger.error("not find cls:{}.loader", domainCls.getName());
			throw new CustomException("not find cls:{" + domainCls.getName() + "}.loader");
		}
		BaseDomainLoader<? extends YzBaseDomain> parentLoader = null;
		BaseDomainLoader<? extends YzBaseDomain> domain = null;
		Class<?> cls;
		for (String name : str) {
			domain = ApplicationContextUtil.getBean(name, BaseDomainLoader.class);
			if (AopUtils.isAopProxy(domain)) {
				cls = AopUtils.getTargetClass(domain);
			} else {
				cls = domain.getClass();
			}
			DomainLoader loader = cls.getAnnotation(DomainLoader.class);
			if (domainCls == loader.cls()) {
				this.domainMap.put(domainCls, domain);
				return domain;
			}
			if (ClassUtil.isAssignable(domainCls, loader.cls())) { //
				parentLoader = domain;
			}
		}
		if (parentLoader != null) {
			this.domainMap.put(domainCls, parentLoader);
			return parentLoader;
		}
		throw new CustomException("not find cls:{" + domainCls.getName() + "}.loader");
	}

	/**
	 * 
	 * @param command
	 * @param arr
	 * @return
	 */
	public YzBaseDomain loadDomain(BaseCommand command, Object arr) {
		Object id = command.getId();
		if (arr == null) {
			if (null == id)
				return null;
			return getDomainLoader(command).load(id, command.getDomainCls());
		} else {
			YzBaseDomain domain = (YzBaseDomain) arr;
			if (domain.getVersion() != command.getVersion()) // cmdId域domain版本号不一致  将强制刷新域仓储数据
			{
				return getDomainLoader(command).load(id, command.getDomainCls());
			}
			return domain;
		}
	}

}
