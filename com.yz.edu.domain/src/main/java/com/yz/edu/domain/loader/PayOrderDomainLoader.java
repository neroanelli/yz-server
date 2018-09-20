package com.yz.edu.domain.loader;

import java.util.List;
import java.util.Map; 
import org.springframework.stereotype.Component;
import com.google.common.collect.Maps; 
import com.yz.edu.domain.YzPayOrderDomain;
import com.yz.edu.domain.YzPayOrderItem;
import com.yz.util.JsonUtil;

@Component(value = "payOrderDomainLoader")
@DomainLoader(cls = YzPayOrderDomain.class)
public class PayOrderDomainLoader extends BaseDomainLoader<YzPayOrderDomain> {

	@Override
	public YzPayOrderDomain load(Object key, Class<?> cls) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("learnId", key);
		logger.info("PayOrderDomainLoader.param:{}", JsonUtil.object2String(param));
		YzPayOrderDomain payOrderDomain = this.reportJdbcDao.queryObject("queryPayOrderDomain", param,
				YzPayOrderDomain.class);
		payOrderDomain.setFeeItems(queryFeeItemsRecord(param));
		return payOrderDomain;
	}

	/**
	 * 
	 * @desc 根据learnId 查询收费订单明细
	 * @param param
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<YzPayOrderItem> queryFeeItemsRecord(Map<String, Object> param) {
		List<YzPayOrderItem> data = this.reportJdbcDao.queryList("queryFeeItemsRecord", param, YzPayOrderItem.class);
		return data;
	}

}
