package com.yz.service;

import com.yz.constants.FinanceConstants;
import com.yz.core.constants.StudentConstants;
import com.yz.dao.BdsPaymentItemMapper;
import com.yz.edu.trace.annotation.YzTrace;
import com.yz.generator.IDGenerator;
import com.yz.model.communi.Body;
import com.yz.pay.GwPaymentService;
import com.yz.util.AmountUtil;
import com.yz.util.BigDecimalUtil;
import com.yz.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 图像采集
 */
@Service
@Transactional
public class BdsPaymentItemService {
    private static final Logger log = LoggerFactory.getLogger(BdsPaymentItemService.class);

    @Autowired
    private GwPaymentService paymentService;

    @Autowired
    private BdsPaymentItemMapper bdsPaymenItemtMapper;

    //请求二维码
    @YzTrace(remark = "付款二维码生成")
    public Object imapPayByQRCode(Body body) {
        String amount = body.getString("amount");
        //订单号逻辑处理,判断是否生成订单
        String orderNo = body.getString("orderNo");
        boolean isCreate = false;
        if (StringUtil.isBlank(orderNo)){
            isCreate= true;
        }else {
            int i = bdsPaymenItemtMapper.selectPayByOrderNo(orderNo);
            if (i == 0){
                //订单号有误，重新生成订单
                isCreate= true;
            }
        }

        if (isCreate) {
            orderNo = creatrOrder(body);
            //判断是否为图像采集更新跟进任务的订单号
            if (StudentConstants.PAY_ITEM_IMAQ.equals(body.getString("payItem"))) {
                bdsPaymenItemtMapper.updateSPCByPicCollectId(body.getString("picCollectId"), orderNo);
            }
        }

        //获取收费二维码链接
        Map<String, String> postData = new HashMap<String, String>();
        postData.put("orderAmount", amount);
        postData.put("serialNo", orderNo);
        postData.put("payType", FinanceConstants.PAYMENT_TYPE_WECHAT);

        //判断是否为图像采集
        if (StudentConstants.PAY_ITEM_IMAQ.equals(body.getString("payItem"))) {
            postData.put("dealType", FinanceConstants.PAYMENT_DEAL_TYPE_IMAQ);
            log.info("-------- 学员图像采集付款二维码申请，learnId ： " + body.getString("learnId"));
        }

        postData.put("tradeType", FinanceConstants.TRADE_TYPE_NATIVE);
        postData.put("productId", orderNo);
        // 微信支付
        Map<String, Object> result = paymentService.payment(postData);
        result.put("orderNo",orderNo);
        return result;
    }

    //生成订单
    public String creatrOrder(Body body){
        String orderNo = IDGenerator.generatororderNo(FinanceConstants.DEFAULT_FINANCE_CODE);
        Map<String,String> map = new HashMap<String, String>();
        map.put("orderNo",orderNo);
        map.put("paymentType", FinanceConstants.PAYMENT_TYPE_WECHAT);
        map.put("payItem", body.getString("payItem"));
        map.put("learnId",body.getString("learnId"));
        map.put("amount", body.getString("amount"));
        bdsPaymenItemtMapper.insertOrder(map);
        log.info("-------- 订单生成，orderNo ： " + orderNo);
        return orderNo;
    }


    /**
     * 接口成功回调
     * @param orderNo
     * @param outSerialNo
     * @param wechatAmount
     * @return
     */
    @YzTrace(remark = "付款二维码接口成功回调")
    public boolean itemPaySuccess(String orderNo, String outSerialNo, String wechatAmount) {
        log.info("--------------------------图像采集支付成功流程开始，订单单号为：" + orderNo);

        //查询是否订单缴费过
        int exist = bdsPaymenItemtMapper.selectIsPayByOrderNo(orderNo);

        if (exist > 0) {
            log.error("--------------------------图像采集支付重复回调，订单单号id：" + orderNo);
            return false;
        }

        // 转换为 单位；元
        wechatAmount = BigDecimalUtil.divide(wechatAmount, "100");

        String amount = bdsPaymenItemtMapper.selectPayAmountByOrderNo(orderNo);

        // 缴费金额不一致
        if (AmountUtil.str2Amount(wechatAmount).compareTo(AmountUtil.str2Amount(amount)) != 0) {
            log.error("--------------------------在线支付回调金额与付款金额不匹配，订单单号id：" + orderNo);
            return false;
        }

        // 支付成功,更新订单信息
        bdsPaymenItemtMapper.updateOrderStatusByOrderNo(orderNo,wechatAmount,outSerialNo);
        log.info("--------------------------图像采集支付成功流程结束，订单单号为：{" + orderNo + "}金额为 ：" + wechatAmount);
        return true;
    }
}
