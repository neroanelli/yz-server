package com.yz.job.service;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.yz.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JPushService {

    private static Logger logger = LoggerFactory.getLogger(JPushService.class);

    private static final int REGISTRATION_ID_LIMIT  = 1000; // 设备ID限制数量

    @Value("${jpush.masterSecret}")
    private String masterSecret;

    @Value("${jpush.appKey}")
    private String appKey;

    /**
     * 极光推送消息（分批推送，每1000个设备ID推送一次）
     * @param registrationIds 设备ID
     * @param msg 消息内容
     */
    public void pushMsg(List<String> registrationIds, String msg){
        int size = registrationIds.size();
        if (REGISTRATION_ID_LIMIT < size) {
            int part = size / REGISTRATION_ID_LIMIT;
            for (int i = 0; i < part; i++) {
                List<String> subList = registrationIds.subList(0, REGISTRATION_ID_LIMIT);
                jpush(subList, msg);
                // 剔除
                registrationIds.subList(0, REGISTRATION_ID_LIMIT).clear();
            }
            // 最的剩下的数据
            if(!registrationIds.isEmpty()){
                jpush(registrationIds,msg);
            }
        } else {
            jpush(registrationIds, msg);
        }
    }

    public void jpush(List<String> registrationIds, String msg){
        registrationIds = registrationIds.stream().filter(x-> !StringUtil.isEmpty(x)).collect(Collectors.toList());
        if(registrationIds.size()==0){
            return;
        }
        JPushClient jPushClient = new JPushClient(masterSecret,appKey,null, ClientConfig.getInstance());
        PushPayload payload = buildPushObject_registrationId_alert(registrationIds,msg);
        try {
            PushResult result = jPushClient.sendPush(payload);
            logger.info("jpush result: " + result.toString());
        }catch (APIConnectionException e) {
            logger.error("jpush connection error, should retry later", e);
        } catch (APIRequestException e) {
            logger.error("jpush should review the error, and fix the request", e);
            logger.info("jpush HTTP Status: " + e.getStatus());
            logger.info("jpush Error Code: " + e.getErrorCode());
            logger.info("jpush Error Message: " + e.getErrorMessage());
        }
    }

    private static PushPayload buildPushObject_registrationId_alert(List<String> registrationIds, String msg) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(registrationIds))
                .setNotification(Notification.alert(msg))
                .build();
    }
}
