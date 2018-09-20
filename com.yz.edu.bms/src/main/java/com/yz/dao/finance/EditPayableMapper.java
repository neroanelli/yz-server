package com.yz.dao.finance;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.finance.EditPayableQuery;
import com.yz.model.finance.stdfee.BdPayableInfoResponse;

public interface EditPayableMapper {

	/**
	 * 查询学员缴费
	 * 
	 * @param userId
	 * @return
	 */
	List<BdPayableInfoResponse> getEditPayableList(@Param("query") EditPayableQuery query);

	/**
	 * 查询学员缴费
	 * 
	 * @param userId
	 * @return
	 */
	BdPayableInfoResponse getStudent(@Param("learnId") String learnId);

	/**
	 * 更改应缴费用
	 * 
	 * @param learnId
	 * @param itemCodes
	 * @param amounts
	 */
	void editPayables(@Param("subOrderNo") String subOrderNo, @Param("amount") String amount);

	/**
	 * 更新订单金额
	 * 
	 * @param orderNo
	 * @param amount
	 */
	void updateOrder(@Param("orderNo") String orderNo, @Param("amount") String amount);
}
