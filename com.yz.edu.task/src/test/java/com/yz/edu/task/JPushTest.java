package com.yz.edu.task;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.yz.job.app.MyTaskApplication;
import com.yz.util.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyTaskApplication.class)
public class JPushTest {
    @Test
    public void push(){
        JPushClient jPushClient = new JPushClient("cbd9d97e948e1952a496356b","c4ce820ba819794db99088ee",null, ClientConfig.getInstance());
        List<String> registrationIds = new ArrayList<String>();
        registrationIds.add("1507bfd3f7941245252"); //anroid
        //registrationIds.add("161a3797c8545ef35c7"); //ios
        registrationIds.add("");
        registrationIds.add(null);
        registrationIds = registrationIds.stream().filter(x-> !StringUtil.isEmpty(x)).collect(Collectors.toList());
        PushPayload payload = buildPushObject_registrationId_alert(registrationIds,"我是程序测试3");
        try {
            PushResult result = jPushClient.sendPush(payload);
            System.out.println(result);
        }catch (APIConnectionException e) {
            // Connection error, should retry later
            //LOG.error("Connection error, should retry later", e);

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            //LOG.error("Should review the error, and fix the request", e);
            //LOG.info("HTTP Status: " + e.getStatus());
            //LOG.info("Error Code: " + e.getErrorCode());
            //LOG.info("Error Message: " + e.getErrorMessage());
        }
    }

    public static PushPayload buildPushObject_registrationId_alert(List<String> registrationIds,String msg) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(registrationIds))
                .setNotification(Notification.alert(msg))
                .build();
    }
}
