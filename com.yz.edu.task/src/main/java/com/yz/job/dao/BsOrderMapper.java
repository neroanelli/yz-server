package com.yz.job.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.job.model.BsLogistics;
import com.yz.job.model.BsOrderInfo;
import com.yz.job.model.BsSalesOrderInfo;

public interface BsOrderMapper {
	
	/**
	 * 查询京东需要同步的订单信息
	 * @return
	 */
	public List<BsOrderInfo> querySynchronousJdOrderList();
	
	/**
	 * 同步京东订单状态
	 * @param resultList
	 */
	public void updateOrderStatus(@Param(value="orderlist")List<BsOrderInfo> resultList);
	
	/**
	 * 
	 * 
	 * @param jdOrderId
	 * @param orderTime
	 */
	public void updateOrderCompletTime(@Param(value="jdOrderId") String jdOrderId,@Param(value="orderTime") String orderTime);
	
	/**
	 * 
	 * @param orderInfo
	 */
	public void addNewBsOrder(BsOrderInfo orderInfo);
	
	/**
	 * @desc 保存子订单 
	 */
	public void addNewBsSalesOrder(BsSalesOrderInfo salesOrderInfo);
	
	/**
	 * 
	 * @desc 保存订单物流信息 
	 * @param logistics
	 */
	public void saveOrderLogistics(BsLogistics logistics);
	
	
	/**
	 * 修改某个活动的可兑换数量
	 * @param count
	 * @param salesId 
	 */
	void updateSalesCount(@Param("count") int count, @Param("salesId") String salesId);
	
	
	void addGsExchange(com.yz.job.model.GsExChangePart gsExChangePart);
}
