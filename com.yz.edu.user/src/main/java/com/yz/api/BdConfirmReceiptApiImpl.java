package com.yz.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BdConfirmReceiptService;
import com.yz.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: luxing
 * @Date 2018\8\17 0017 14:34
 **/
@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdConfirmReceiptApiImpl implements BdConfirmReceiptApi{

    @Autowired
    private BdConfirmReceiptService bdConfirmReceiptService;

    @Override
    public Object getReceiptInfo(Header header, Body body){
        String learnId = body.getString("learnId");
        Assert.hasText(learnId, "学业id不能为空");
        return bdConfirmReceiptService.getReceiptInfo(learnId);
    }

    @Override
    public Object uploadReceipt(Header header, Body body){
        String learnId = body.getString("learnId");
        String stdId = body.getString("stdId");
        String userId = header.getUserId();
        String tempUrl = body.getString("tempUrl");
        String examNo = body.getString("examNo");
        String userName = body.getString("userName");
        String isUpdate = body.getString("isUpdate");//0没有更改提交，1更改后提交
        Assert.hasText(learnId, "学员信息不能为空");
        Assert.hasText(stdId, "学员信息不能为空");
        Assert.hasText(userId, "学员信息不能为空");
        Assert.hasText(tempUrl, "上传附件不能为空");
        Assert.hasText(examNo, "考生号不能为空");
        return bdConfirmReceiptService.uploadReceipt(learnId,stdId,tempUrl,examNo,userId,userName,isUpdate);
    }
}
