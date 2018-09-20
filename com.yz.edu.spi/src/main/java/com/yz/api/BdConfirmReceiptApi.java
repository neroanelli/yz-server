package com.yz.api;

import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

/**
 * @Description:学员上传确认回执
 * @Author: luxing
 * @Date 2018\8\17 0017 14:23
 **/
public interface BdConfirmReceiptApi {

    /**
     * 公众号--远智学堂-更多 查看回执
     * @param header
     * @param body
     * @return
     */
    @YzService(sysBelong="bds",methodName="getReceiptInfo",methodRemark="查看回执",needLogin=true)
    public Object getReceiptInfo(Header header, Body body);

    /**
     *公众号--远智学堂-更多 上传回执
     * @param header
     * @param body
     * @return
     */
    @YzService(sysBelong="bds",methodName="uploadReceipt",methodRemark="上传回执",needLogin=true)
    public Object uploadReceipt(Header header, Body body);
}
