package com.yz.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Map; /**
 * 图像采集
 */
public interface BdsPaymentItemMapper {
    /**
     * 生成订单
     * @param map
     */
    void insertOrder(Map<String, String> map);

    void updateQRCode(@Param("orderNo") String orderNo, @Param("codeUrl") String codeUrl);

    int selectPayByOrderNo(String orderNo);

    /**
     * 查询订单是否已缴费
     * @param orderNo
     * @return
     */
    int selectIsPayByOrderNo(@Param("orderNo") String orderNo);

    String selectPayAmountByOrderNo(@Param("orderNo") String orderNo);

    int updateOrderStatusByOrderNo(@Param("orderNo") String orderNo, @Param("wechatAmount") String wechatAmount, @Param("outSerialNo") String outSerialNo);

    /**
     * 更新任务表对应的订单链接
     * @param picCollectId
     * @param orderNo
     */
    void updateSPCByPicCollectId(@Param("picCollectId") String picCollectId, @Param("orderNo") String orderNo);
}
