package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

/**
 * 学员项目缴费接口
 */
public interface BdsPaymenItemtApi {

    /**
     * 获取二维码支付链接
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
    public Object itemPayByQRCode(Header header, Body body) throws IRpcException;

    /**
     * 支付成功回调
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
    public boolean itemPaySuccess(Header header, Body body) throws IRpcException;

}
