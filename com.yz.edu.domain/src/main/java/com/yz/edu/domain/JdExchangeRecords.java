package com.yz.edu.domain;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.yz.util.StringUtil;

/**
 * 
 * 
 * @author Administrator
 *
 */
public class JdExchangeRecords implements java.io.Serializable {

	private List<JdExchangeRecord> records = null;

	public JdExchangeRecords() {
		records = Lists.newArrayList();
	}
	
	
	/**
	 * @desc 根据userId 查询兑换数量
	 * @param userId
	 * @param count
	 * @return
	 */
	public long countRecord(String userId)
	{ 
	   return this.records.parallelStream().
			   filter(v->StringUtil.equalsIgnoreCase(userId, v.getUserId()))
			   .map(JdExchangeRecord::getExchangeCount)
			   .reduce((x,y)->x+y).orElse(0);
	}

	/**
	 * 
	 * @param userId
	 * @param count
	 * @return
	 */
	public JdExchangeRecords addJdExchangeRecord(String orderNo,String userId, int count) {
		return this.addJdExchangeRecord(orderNo,userId, new Date().getTime(), count);
	}

	/**
	 * 
	 * @param 订单号
	 * @param userId
	 * @param time
	 * @param count
	 * @return
	 */
	public JdExchangeRecords addJdExchangeRecord(String orderNo,String userId, long time, int count) {
		JdExchangeRecord record = new JdExchangeRecord();
		record.setExchangeDate(time);
		record.setUserId(userId);
		record.setExchangeCount(count);
		record.setOrderNo(orderNo);
		this.records.add(record);
		return this;
	}

	/**
	 * 
	 * @param userId
	 * @param count
	 * @return
	 */
	public boolean checkExchangeRecord(String userId, int count) {
		List<JdExchangeRecord> datas = this.records.parallelStream()
				.filter(v -> StringUtil.equalsIgnoreCase(userId, v.getUserId())).collect(Collectors.toList());
		if (datas != null) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 
	 * @author Administrator
	 *
	 */
	private class JdExchangeRecord implements java.io.Serializable {
		
		private String userId; // 用户Id

		private String orderNo;//订单号 
		
		private Long exchangeDate; // 兑换时间

		private int exchangeCount; // 兑换数量
		
		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}
		
		public String getOrderNo() {
			return orderNo;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public Long getExchangeDate() {
			return exchangeDate;
		}

		public void setExchangeDate(Long exchangeDate) {
			this.exchangeDate = exchangeDate;
		}

		public int getExchangeCount() {
			return exchangeCount;
		}

		public void setExchangeCount(int exchangeCount) {
			this.exchangeCount = exchangeCount;
		}
	}
}
