package com.yz.edu.domain.loader;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.yz.edu.domain.YzUserDomain;
import com.yz.util.JsonUtil;

/**
 * 
 * @desc userDomain 加载器
 * @author Administrator
 *
 */
@Component(value = "userDomainLoader")
@DomainLoader(cls = YzUserDomain.class)
public class UserDomainLoader extends BaseDomainLoader<YzUserDomain> {

	@Override
	public YzUserDomain load(Object key, Class<?> cls) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("userId", key);
		logger.info("UserDomainLoader.param:{}", JsonUtil.object2String(param));
		return this.reportJdbcDao.queryObject("queryUserDomain", param, YzUserDomain.class);
	}

}