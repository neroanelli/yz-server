package com.yz.edu.domain.loader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.yz.edu.domain.JdExchangeRecords;
import com.yz.edu.domain.JdGoodsSalesDomain;
import com.yz.util.JsonUtil;

@Component(value = "jdGoodsSalesLoader")
@DomainLoader(cls = JdGoodsSalesDomain.class)
public class JdGoodsSalesLoader extends BaseDomainLoader<JdGoodsSalesDomain> {

	@Override
	public JdGoodsSalesDomain load(Object key, Class<?> cls) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("salesId", key);
		logger.info("JdGoodsSalesLoader.param:{}", JsonUtil.object2String(param));
		JdGoodsSalesDomain salesDomain = this.reportJdbcDao.queryObject("queryJdGoodsSales", param,
				JdGoodsSalesDomain.class);
		salesDomain.setRecords(queryGoodsSalesRecord(param));
		return salesDomain;
	}

	/**
	 * 
	 * @desc 根据salesId 查询兑换记录
	 * @param param
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JdExchangeRecords queryGoodsSalesRecord(Map<String, Object> param) {
		JdExchangeRecords data = new JdExchangeRecords();
		this.reportJdbcDao.queryList("queryGoodsSalesRecord", param, new RowMapper<Object>() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				data.addJdExchangeRecord(
						rs.getString("orderNo"),
						rs.getString("userId"), 
						rs.getDate("exchangeDate").getTime(),
						rs.getInt("exchangeCount"));
				return null;
			}
		});
		return data;
	}

}
