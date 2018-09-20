package com.yz.network.examination.service;

import com.yz.generator.IDGenerator;
import com.yz.network.examination.constants.BusinessConstants;
import com.yz.network.examination.croe.util.SessionUtil;
import com.yz.network.examination.dao.QueueMapper;
import com.yz.network.examination.model.BdLearnQueueInfo;
import com.yz.network.examination.vo.LoginUser;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：林建彬.
 * @ Date       ：Created in 10:34 2018/8/8
 * @ Description：排号系统表现层
 */
@Service
@Transactional
public class QueueService {

    private static final Logger log = LoggerFactory.getLogger(QueueService.class);

    @Autowired
    private QueueMapper queueMapper;

    /**
     * 获取列表
     * @param pfsnLevel
     * @return
     */
    public List<BdLearnQueueInfo> getQueueList(String pfsnLevel) {
        //获取登录信息里的信息确定排号的人的地址限定
        LoginUser user = SessionUtil.getUser();

        return queueMapper.getQueueList(pfsnLevel, user.getCallNumCityCode());
    }

    /**
     * 下几位处理
     * @param pfsnLevel
     * @param number
     */
    public void updateNextStatus(String pfsnLevel, Integer number) {
        LoginUser user = SessionUtil.getUser();
        //先查询今天接下来的人
        List<String> queueIds = queueMapper.selectNextNumber(pfsnLevel, user.getCallNumCityCode(), number);
        //更新确认中为已完成
        queueMapper.updateNextStatus(pfsnLevel, user.getCallNumCityCode(), user.getUserId(), queueIds);
    }

    /**
     * 旁边未预约的插队操作
     * @param queueIds
     */
    public void jumpQueue(String[] queueIds) {
        RedisService redisService = RedisService.getRedisService();
        for (String queueId : queueIds) {
            Long queueNo = redisService.incrBy(BusinessConstants.BD_LEARN_QUEUE_NO, 1);
            queueMapper.jumpQueue(queueId,queueNo.toString());
        }
    }

    /**
     * 添加签到排号表记录接口
     * @param bdLearnQueueInfo
     * @return
     */
    public Map<String, String> addQueue(BdLearnQueueInfo bdLearnQueueInfo){
        log.info("------------------------------请求排号的学员数据：" + JsonUtil.object2String(bdLearnQueueInfo));
        Map<String, String> result = new HashMap<String, String>(2);
        //判断是否预约
        bdLearnQueueInfo.setQueueId(IDGenerator.generatorId());
        RedisService redisService = RedisService.getRedisService();
        Date now = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String nowString = f.format(now);
        //获取城市相对应编号的key
        String cityNoKey = null;
        //等待人数
        String waitNum = "0";
        if(nowString.equals(bdLearnQueueInfo.getPlaceConfirmTime())){
            //已预约,判断本科专科
            if("1".equals(bdLearnQueueInfo.getPfsnLevel())){
                bdLearnQueueInfo.setQueueType(BusinessConstants.BD_LEARN_QUEUE_TYPE_B);
                cityNoKey = BusinessConstants.BD_LEARN_QUEUE_CITY_NO_PREFIX + bdLearnQueueInfo.getCityCode() + "_"  + nowString + "_" + BusinessConstants.BD_LEARN_QUEUE_TYPE_B;
            }else {
                bdLearnQueueInfo.setQueueType(BusinessConstants.BD_LEARN_QUEUE_TYPE_A);
                cityNoKey = BusinessConstants.BD_LEARN_QUEUE_CITY_NO_PREFIX + bdLearnQueueInfo.getCityCode() + "_"  + nowString + "_" + BusinessConstants.BD_LEARN_QUEUE_TYPE_A;
            }
            //查询当前等待人数
            waitNum = queueMapper.selectWaitNum(bdLearnQueueInfo.getPfsnLevel(),bdLearnQueueInfo.getCityCode(), true);
            //排号，未预约不排号
            bdLearnQueueInfo.setQueueNo(redisService.incrBy(BusinessConstants.BD_LEARN_QUEUE_NO, 1).toString());
        }else {
            bdLearnQueueInfo.setQueueType(BusinessConstants.BD_LEARN_QUEUE_TYPE_D);
            cityNoKey = BusinessConstants.BD_LEARN_QUEUE_CITY_NO_PREFIX + bdLearnQueueInfo.getCityCode() + "_"  + nowString + "_" + BusinessConstants.BD_LEARN_QUEUE_TYPE_D;
            waitNum = queueMapper.selectWaitNum(bdLearnQueueInfo.getPfsnLevel(), bdLearnQueueInfo.getCityCode(), false);
        }
        Long a = redisService.incrBy(cityNoKey, 1);
        if(a==1){
            //设置过期时间
            redisService.expire(cityNoKey,3600*24);
        }
        bdLearnQueueInfo.setNo(a.toString());
        //格式化身份证,加星
        bdLearnQueueInfo.setIdCard(bdLearnQueueInfo.getIdCard().substring(0, 4) + "**********" + bdLearnQueueInfo.getIdCard().substring(14));
        //添加记录
        queueMapper.addQueue(bdLearnQueueInfo);
        result.put("queueType",bdLearnQueueInfo.getQueueType());
        result.put("no",a.toString());
        result.put("waitNum",waitNum);
        return result;
    }
}
