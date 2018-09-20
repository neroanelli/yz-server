package com.yz.report;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.yz.cache.handler.RedisCacheHandler;
import com.yz.cache.rule.RedisCacheRule;
import com.yz.constants.CommonConstants;
import com.yz.redis.RedisService;
import com.yz.report.handler.ReportResultHandler;
import com.yz.util.ApplicationContextUtil;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;
import com.yz.util.MethodUtil;
import com.yz.util.RegUtils;
import com.yz.util.StringUtil;

@Component(value = "reportJdbcDao")
public class ReportJdbcDao extends NamedParameterJdbcDaoSupport implements CommonConstants {

	private Logger logger = LoggerFactory.getLogger(ReportJdbcDao.class);

	private ApplicationContext reportSpringContext = ApplicationContextUtil.getApplicationContext();

	@Autowired(required = true)
	private DataSource dataSource;

	@Value("${report.devMode:wip}")
	private String devMode; // 开发模式 wip 生产环境 启用redis缓存repCode

	@Autowired
	private RedisCacheRule redisCacheRule;

	@PostConstruct
	public void init() {
		this.setJdbcTemplate(createJdbcTemplate(dataSource));
	}

	/**
	 *
	 * @param repCode
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getRepConf(String repCode) {
		Assert.notNull(repCode);
		if (StringUtil.equalsIgnoreCase(REPORT_MODE_DEV, devMode)) {
			return this.getDbRepConf(repCode);
		}
		String infoStr = RedisService.getRedisService().hget(YZ_REP_CODE_KEY, repCode);
		Map<String, Object> result = JsonUtil.str2Object(infoStr, Map.class);
		if (result == null || result.isEmpty()) {
			result = this.getDbRepConf(repCode);
			RedisService.getRedisService().hset(YZ_REP_CODE_KEY, repCode, JsonUtil.object2String(result));
		}
		Assert.notEmpty(result, "通过RepCode【“" + repCode + "”】查找失败，请检查配置是否正确。");
		return result;
	}

	/**
	 *
	 * @param repCode
	 * @return
	 */
	private Map<String, Object> getDbRepConf(String repCode) {
		Map<String, Object> param = new HashMap<>();
		param.put("REP_CODE", repCode);
		Map<String, Object> result = this.getNamedParameterJdbcTemplate().queryForMap(YZ_REP_SQL, param);
		Assert.notEmpty(result, "通过RepCode【“" + repCode + "”】查找失败，请检查配置是否正确。");
		return result;
	}

	/**
	 *
	 * @param repCode
	 * @param paramMap
	 * @param cls
	 * @return
	 */
	public <T> List<T> queryList(String repCode, Map<String, Object> paramMap, Class<T> cls) {
		Map<String, Object> repMap = this.getDbRepConf(repCode);
		String sql = String.valueOf(repMap.get("repSql"));
		try {
			logger.info("executeSql:{},repCode:{},param:{}", sql, repCode, JsonUtil.object2String(paramMap));
			return this.getNamedParameterJdbcTemplate().queryForList(sql, paramMap, cls);
		} catch (Exception ex) {
			logger.error("repCode{}.error:{}", repCode, ExceptionUtil.getStackTrace(ex));
			return null;
		}
	}

	/**
	 * 
	 * @param repCode
	 * @param paramMap
	 * @param rowMapper
	 * @return
	 */
	public <T> List<T> queryList(String repCode, Map<String, Object> paramMap, RowMapper<T> rowMapper) {
		Map<String, Object> repMap = this.getDbRepConf(repCode);
		String sql = String.valueOf(repMap.get("repSql"));
		try {
			logger.info("executeSql:{},repCode:{},param:{}", sql, repCode, JsonUtil.object2String(paramMap));
			return this.getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
		} catch (Exception ex) {
			logger.error("repCode{}.error:{}", repCode, ExceptionUtil.getStackTrace(ex));
			return null;
		}

	}

	/**
	 *
	 * @param repCode
	 * @param paramMap
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryObject(String repCode, Map<String, Object> paramMap, Class<T> cls) {
		RowMapper mapper = cls == Map.class ? new ColumnMapRowMapper() : new BeanPropertyRowMapper(cls);
		return (T)this.queryObject(repCode, paramMap, mapper);
	}

	/**
	 * 
	 * @param repMap
	 * @param paramMap
	 * @return
	 */
	private String calcCacheKey(String cacheNamespace, String cacheKey, Map<String, Object> paramMap) {
		StringBuilder sb = new StringBuilder(cacheNamespace);
		sb.append(redisCacheRule.getCacheName(cacheKey, paramMap));
		return sb.toString();
	}

	/**
	 *
	 * @param repCode
	 * @param paramMap
	 * @param rowMapper
	 * @return
	 */
	public <T> T queryObject(String repCode, Map<String, Object> paramMap, RowMapper<T> rowMapper) {
		Map<String, Object> repMap = this.getDbRepConf(repCode);
		String sql = String.valueOf(repMap.get("repSql"));
		try {
			logger.info("executeSql:{},repCode:{},param:{}", sql, repCode, JsonUtil.object2String(paramMap));
			String cacheHandler = String.valueOf(repMap.get("cacheHandler"));
			if (StringUtil.isBlank(cacheHandler)||StringUtil.equalsIgnoreCase(cacheHandler, "null")) {
				return this.getNamedParameterJdbcTemplate().queryForObject(sql, paramMap,rowMapper);
			}
			String cacheKey = this.calcCacheKey(String.valueOf(repMap.get("cacheNamespace")),
					String.valueOf(repMap.get("cacheKey")), paramMap);
			RedisCacheHandler handler = reportSpringContext.getBean(cacheHandler, RedisCacheHandler.class);
			Method method = MethodUtil.getAccessibleMethod(rowMapper.getClass(), "mapRow", new Class[]{ResultSet.class,int.class});
			Object result = handler.getCache(CommonConstants.REDIS_NAME_DEFAULT, cacheKey, method.getReturnType());
			if (result == null) {
				result = this.getNamedParameterJdbcTemplate().queryForObject(sql, paramMap,rowMapper);
				handler.setCache(CommonConstants.REDIS_NAME_DEFAULT, cacheKey, result, 3600 * 24 * 7);
			}
			return (T) result;
		} catch (IllegalArgumentException ex) {
			logger.debug("paramMap{}", JsonUtil.object2String(paramMap));
			logger.error("repCode{}.error:{}", repCode, ExceptionUtil.getStackTrace(ex));
			return null;
		}
	}

	/**
	 *
	 * @param repCode
	 * @param paramMap
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object getRepResultList(String repCode, Map<String, Object> paramMap, Class<?> cls) {
		Map<String, Object> repMap = this.getRepConf(repCode);
		String rSql = String.valueOf(repMap.get("repSql"));
		regSqlParam(rSql, paramMap);
		Assert.notNull(rSql);
		logger.info("executeSql:{},repCode:{},param:{}", rSql, repCode, JsonUtil.object2String(paramMap));
		RowMapper mapper = cls == Map.class ? new ColumnMapRowMapper() : new BeanPropertyRowMapper(cls);
		List result = this.getNamedParameterJdbcTemplate().query(rSql, paramMap, mapper);
		String handler = String.valueOf(repMap.get("repHandler"));
		if (StringUtil.isNotBlank(handler) && !StringUtil.equalsIgnoreCase(handler, "null")) {
			ReportResultHandler resulthandler = reportSpringContext.getBean(handler, ReportResultHandler.class);
			Object obj = resulthandler.execute(paramMap, result);
			return obj;
		}
		return result;
	}

	/**
	 *
	 * @param repCode
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object getRepResultList(String repCode, Map<String, Object> paramMap) {
		return this.getRepResultList(repCode, paramMap, Map.class);
	}

	/**
	 *
	 * @param rsSql
	 * @param queryParam
	 */
	private void regSqlParam(String rsSql, final Map<String, Object> queryParam) {
		RegUtils.findAndReplace("\\:\\s*[a-zA-Z\\.0-9\\_\\-\\?\\*\\/]+\\s*", rsSql, source -> {
			String s = StringUtil.trim(StringUtil.replace(source, ":", ""));
			if (queryParam.get(s) == null) {
				queryParam.put(s, "");
			}
			return source;
		});
	}

}
