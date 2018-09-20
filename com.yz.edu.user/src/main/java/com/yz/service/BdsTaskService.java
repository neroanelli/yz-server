package com.yz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yz.constants.TransferConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.dao.*;
import com.yz.model.BdLearnAnnex;
import com.yz.model.BdStudentBaseInfo;
import com.yz.model.StudentHistory;
import com.yz.model.WechatMsgVo;
import com.yz.model.student.BdLearnInfo;
import com.yz.model.student.BdStudentModify;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.Assert;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yz.edu.paging.common.PageHelper;
import com.yz.edu.paging.bean.PageInfo;
import com.yz.constants.GlobalConstants;
import com.yz.constants.StudentConstants;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.model.system.SysCity;
import com.yz.model.system.SysDistrict;
import com.yz.model.system.SysProvince;
import com.yz.util.StringUtil;

/**
 * 元智学堂-我的任务
 *
 * @author lx
 * @date 2017年8月17日 上午11:14:54
 */
@Service
@Transactional
public class BdsTaskService {

    private static final Logger log = LoggerFactory.getLogger(BdsTaskService.class);

    @Autowired
    private BdsTaskMapper bdsTaskMapper;

    @Autowired
    private BdsStudentSendService bdStudentSendService;

    @Autowired
    private UsAddressService addressService;

    @Autowired
    private BdsStudentService bdsStudentService;

    @Autowired
    private UsBaseInfoMapper usBaseInfoMapper;

    @Autowired
    private BdStudentOutService studentOuntService;

    @Autowired
    private SysProvinceCityDistrictMapper cityMapper;

    public Object myTasks(Header header, Body body) {
        int page = body.getInt(GlobalConstants.PAGE_NUM, 0);
        int pageSize = body.getInt(GlobalConstants.PAGE_SIZE, 15);
        PageHelper.startPage(page, pageSize);
        String taskStatus = body.getString("taskStatus");
        String learnId = body.getString("learnId");

        List<Map<String, String>> list = bdsTaskMapper.getMyTaskInfo(learnId, taskStatus);
        if (null != list && list.size() > 0) {
            for (Map<String, String> map : list) {
                if (map.get("taskType").equals("4")) { //考场确认任务
                    map.put("curTime", System.currentTimeMillis() + "");
                    String erId = bdsTaskMapper.getStudentReasonById(map.get("taskId"), learnId);
                    if (StringUtil.hasValue(erId)) {
                        map.put("ifTeacherOper", "Y");
                    } else {
                        map.put("ifTeacherOper", "N");
                    }
                } else if (map.get("taskType").equals("6")) { //毕业资料提交
                    String userName = bdsTaskMapper.getStudentGraduateAddressById(map.get("taskId"), learnId);
                    if (StringUtil.hasValue(userName)) {
                        map.put("ifApply", "Y");
                    } else {
                        map.put("ifApply", "N");
                    }
                } else if (map.get("taskType").equals("8")) { //英语学位
                    //获取学位英语的信息
                    Map<String, String> engilshInfo = bdsTaskMapper.getStuDegreeEnglishInfo(map.get("taskId"), learnId);
                    if (null != engilshInfo) {
                        map.put("isEnroll", engilshInfo.get("isEnroll"));
                        map.put("enrollNo", engilshInfo.get("enrollNo"));
                    }
                } else if (map.get("taskType").equals("13")) { //国开考试城市确认
                    map.put("curTime", System.currentTimeMillis() + "");
                    String reason = bdsTaskMapper.getUnconfirmedReason(learnId, map.get("taskId"));
                    if (StringUtil.hasValue(reason)) {
                        map.put("ifTeacherOper", "Y");
                    } else {
                        map.put("ifTeacherOper", "N");
                    }
                }
            }
        }
        PageInfo<Map<String, String>> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    public Object updateTaskStatus(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        bdsTaskMapper.updateTaskStatus(taskId, learnId);
        return null;
    }

    public Object affirmAddress(Header header, Body body) {

        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        String semester = body.getString("semester"); //学期
        bdsTaskMapper.updateTaskStatus(taskId, learnId);

        String saId = body.getString("saId");
        Map<String, String> addressMap = addressService.getAddressDetailById(saId);
        if (null != addressMap && addressMap.size() > 0) {
            //收货地址信息
            log.info("确认收货地址信息:" + learnId + "收件人:" + addressMap.get("saName") + "联系电话:" + addressMap.get("mobile")
                    + "地址:" + addressMap.get("address") + "省:" + addressMap.get("provinceCode") + "市:" + addressMap.get("cityCode") + "区:" + addressMap.get("districtCode"));
            Map<String, String> studentInfo = bdsTaskMapper.getStudentInfo(learnId);

            bdStudentSendService.updateStdBookReceive(learnId, addressMap.get("saName"),
                    addressMap.get("mobile"), addressMap.get("address"),
                    addressMap.get("provinceCode"), addressMap.get("cityCode"),
                    addressMap.get("districtCode"), addressMap.get("streetCode"),
                    addressMap.get("provinceName"), addressMap.get("cityName"),
                    addressMap.get("districtName"), addressMap.get("streetName"),
                    strConvertArray(semester, studentInfo.get("recruitType")));

        } else {
            throw new BusinessException("E200024");
        }

        return null;
    }

    //转换学期
    private String[] strConvertArray(String str, String recruitType) {
        String[] ss = null;
        if (recruitType.equals(StudentConstants.RECRUIT_TYPE_GK)) {
            ss = new String[]{str};
        } else {
            if (str.equals("1") || str.equals("2")) {
                ss = new String[]{"1", "2"};
            } else if (str.equals("3") || str.equals("4")) {
                ss = new String[]{"3", "4"};
            } else if (str.equals("5") || str.equals("6")) {
                ss = new String[]{"5", "6"};
            }
        }
        return ss;
    }

    public Object affirmExamInfo(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        String pyId = body.getString("pyId");
        String eyId = body.getString("eyId");
        String erId = bdsTaskMapper.getStudentReasonById(taskId, learnId);
        if (StringUtil.hasValue(erId)) {
            throw new BusinessException("E60032");
        }
        int ifPast = bdsTaskMapper.taskIfPast(taskId);
        if (ifPast < 1) {
            throw new BusinessException("E60033");
        }
        //获取当前年度的当前考场已确认的人数以及当前年度的当前考场总数
        int confirmedCount = bdsTaskMapper.getExamAffirmCount(eyId, pyId);
        int totalCount = NumberUtils.toInt(bdsTaskMapper.getExamSeats(pyId), 0);
        if (totalCount - confirmedCount > 0) {
            //如果还有座位,就进行确认的操作
            bdsTaskMapper.affirmExamInfo(taskId, eyId, learnId, pyId);
            bdsTaskMapper.updateTaskStatus(taskId, learnId);
        } else {
            throw new BusinessException("E60034");
        }
        return null;
    }

    public Object getStudentInfo(Header header, Body body) {
        String learnId = body.getString("learnId");
        String eyId = body.getString("eyId");
        String semester = null;
        Map<String, String> studentInfo = bdsTaskMapper.getStudentInfo(learnId);
        if (null != studentInfo) {
            String grade = studentInfo.get("grade");
            List<Map<String, String>> yearSubject = bdsTaskMapper.getExamYearSubject(eyId);
            if (null != yearSubject && yearSubject.size() > 0) {
                for (Map<String, String> map : yearSubject) {
                    if (StringUtil.hasValue(grade) && grade.equals(map.get("grade"))) {
                        semester = map.get("semester");
                        break;
                    }
                }
            }
            String testSubject = bdsTaskMapper.getStudentTestSubject(studentInfo.get("pfsnId"), grade, semester);
            studentInfo.put("testSubject", testSubject);
        }
        return studentInfo;
    }

    public Object getProvince(Header header, Body body) {
        Map<String, Object> map = new HashMap<>();
        String eyId = body.getString("eyId");
        map.put("province", bdsTaskMapper.getProvince(eyId));
        map.put("city", bdsTaskMapper.getCity(eyId));
        map.put("district", bdsTaskMapper.getDistrict(eyId));

        return map;
    }

    public Object getExamPlace(Header header, Body body) {
        String provinceCode = body.getString("provinceCode");
        String cityCode = body.getString("cityCode");
        String districtCode = body.getString("districtCode");
        String eyId = body.getString("eyId");
        List<Map<String, String>> resultMap = bdsTaskMapper.getExamPlace(provinceCode, cityCode, districtCode, eyId);
        if (null != resultMap && resultMap.size() > 0) {
            for (Map<String, String> map : resultMap) {
                SysProvince province = cityMapper.selectProvinceByPrimaryKey(map.get("provinceCode"));
                SysCity city = cityMapper.selectCityByPrimaryKey(map.get("cityCode"));
                SysDistrict district = cityMapper.selectDistrictByPrimaryKey(map.get("districtCode"));
                StringBuilder sb = new StringBuilder();
                if (null != province) {
                    sb.append(province.getProvinceName());
                }
                if (null != city) {
                    sb.append(city.getCityName());
                }
                if (null != district) {
                    sb.append(district.getDistrictName());
                }
                map.put("detailAddress", sb.toString() + map.get("address"));
            }
        }
        return resultMap;
    }

    public Object getPlaceYear(Header header, Body body) {
        String placeId = body.getString("placeId");
        String eyId = body.getString("eyId");
        return bdsTaskMapper.getPlaceYear(placeId, eyId);
    }

    public Object getExamAffirmResult(Header header, Body body) {
        String taskId = body.getString("taskId");
        String eyId = body.getString("eyId");
        String learnId = body.getString("learnId");
        Map<String, String> resultMap = bdsTaskMapper.getExamAffirmResult(taskId, eyId, learnId);
        if (null != resultMap) {
            SysProvince province = cityMapper.selectProvinceByPrimaryKey(resultMap.get("provinceCode"));
            SysCity city = cityMapper.selectCityByPrimaryKey(resultMap.get("cityCode"));
            SysDistrict district = cityMapper.selectDistrictByPrimaryKey(resultMap.get("districtCode"));
            StringBuilder sb = new StringBuilder();
            if (null != province) {
                sb.append(province.getProvinceName());
            }
            if (null != city) {
                sb.append(city.getCityName());
            }
            if (null != district) {
                sb.append(district.getDistrictName());
            }
            resultMap.put("detailAddress", sb.toString() + resultMap.get("address"));
            String semester = null;
            String grade = resultMap.get("grade");
            List<Map<String, String>> yearSubject = bdsTaskMapper.getExamYearSubject(eyId);
            if (null != yearSubject && yearSubject.size() > 0) {
                for (Map<String, String> map : yearSubject) {
                    if (StringUtil.hasValue(grade) && grade.equals(map.get("grade"))) {
                        semester = map.get("semester");
                        break;
                    }
                }
            }
            String testSubject = bdsTaskMapper.getStudentTestSubject(resultMap.get("pfsnId"), grade, semester);
            resultMap.put("testSubject", testSubject);
        }
        return resultMap;
    }

    public Object getStudentExamGk(Header header, Body body) {
        String learnId = body.getString("learnId");
        String eyId = body.getString("eyId");
        Map<String, Object> studentInfo = bdsTaskMapper.getStudentForGkExam(eyId, learnId);
        return studentInfo;
    }

    public Object updateStudentExamGkIsRead(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        String eyId = body.getString("eyId");

        bdsTaskMapper.updateTaskStatus(taskId, learnId);

        bdsTaskMapper.updateStudentExamGkIsRead(eyId, learnId);

        return null;
    }

    public Object getStudentGraduateTemplate(Header header, Body body) {
        String learnId = body.getString("learnId");
        String templateUrl = bdsTaskMapper.getStudentGraduateTemplate(learnId);
        return templateUrl;
    }

    public Object applyRegisterForm(Header header, Body body) {

        String learnId = body.getString("learnId");
        String taskId = body.getString("taskId");
        String provinceCode = body.getString("provinceCode");
        String cityCode = body.getString("cityCode");
        String districtCode = body.getString("districtCode");
        String userName = body.getString("userName");
        String mobile = body.getString("mobile");
        String address = body.getString("address");

        Map<String, String> map = new HashMap<>();
        map.put("learnId", learnId);
        map.put("taskId", taskId);
        map.put("provinceCode", provinceCode);
        map.put("cityCode", cityCode);
        map.put("districtCode", districtCode);
        map.put("userName", userName);
        map.put("mobile", mobile);
        map.put("address", address);

        bdsTaskMapper.updateStudentGraduateAddress(map);
        return null;
    }

    public Object stuLookXueXinNet(Header header, Body body) {
        String learnId = body.getString("learnId");
        String taskId = body.getString("taskId");

        bdsTaskMapper.stuLookXueXinNet(taskId, learnId);
        return null;
    }

    public Object stuSubmitXueXinInfo(Header header, Body body) {
        String learnId = body.getString("learnId");
        String taskId = body.getString("taskId");

        //判断是否查看学信息网信息
        int ifLook = bdsTaskMapper.stuIfLookXueXinInfo(taskId, learnId);
        if (ifLook < 1) {
            throw new BusinessException("E60038");
        }
        //修改任务状态为已完成
        bdsTaskMapper.updateTaskStatus(taskId, learnId);

        String isError = body.getString("isError");
        String feedback = body.getString("feedback");
        bdsTaskMapper.stuSubmitXueXinInfo(taskId, learnId, isError, feedback);
        return null;
    }

    public Object stuDegreeEnglishInfo(Header header, Body body) {

        String learnId = body.getString("learnId");
        String taskId = body.getString("taskId");
        String isEnroll = body.getString("isEnroll");
        if (StringUtil.hasValue(isEnroll)) {
            if (isEnroll.equals("2")) { //不报名
                bdsTaskMapper.stuDegreeEnglishEnroll(taskId, learnId, isEnroll, null);
            } else if (isEnroll.equals("1")) { //报名
                //验证学位考试分数是否合格
                String degreeScore = bdsTaskMapper.getDegreeScoreByLearnId(learnId);
                if (StringUtil.hasValue(degreeScore) && Integer.parseInt(degreeScore) >= 60) {
                    throw new BusinessException("E60040");
                }
                String enrollNo = body.getString("enrollNo"); //必填
                //报名
                bdsTaskMapper.stuDegreeEnglishEnroll(taskId, learnId, isEnroll, enrollNo);
            }
            //修改任务状态为已完成
            bdsTaskMapper.updateTaskStatus(taskId, learnId);
        }

        return null;
    }

    public Object getStuGraduatePaperTemplate(Header header, Body body) {
        String unvsId = body.getString("unvsId");
        String pfsnLevel = body.getString("pfsnLevel");

        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");

        String grade = body.getString("grade");

        List<Map<String, String>> templateUrl = bdsTaskMapper.getTemplateUrlByUnvsIdAndPfsnLevel(unvsId, pfsnLevel, grade);

        //更新论文状态为已查看
        bdsTaskMapper.updatePaperTaskViewTime(taskId, learnId);

        //修改任务状态为已完成
        bdsTaskMapper.updateTaskStatus(taskId, learnId);

        return templateUrl;
    }

    public Object updateNoticeTaskViewTime(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");

        bdsTaskMapper.updateNoticeTaskViewTime(taskId, learnId);

        return null;
    }

    public Object submitAffirmInfo(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");

        String isReceiveBook = body.getString("isReceiveBook");
        String isKnowTimetables = body.getString("isKnowTimetables");
        String isKnowCourseType = body.getString("isKnowCourseType");


        bdsTaskMapper.updateSubmitAffirmInfo(taskId, learnId, isReceiveBook, isKnowCourseType, isKnowTimetables);


        //修改任务状态为已完成
        bdsTaskMapper.updateTaskStatus(taskId, learnId);

        return null;

    }

    public Object getQingshuInfo(String learnId) {
        return bdsTaskMapper.getQingshuInfo(learnId);
    }

    public Object sumbitQingshuConfirmStatus(String learnId, String taskId, String confirmStatus) {
        bdsTaskMapper.updateQingshuConfirmStatus(learnId, taskId, confirmStatus);
        return null;
    }

    /**
     * 国开考场城市信息
     *
     * @return
     */
    public Object getExamCityGk() {
        return bdsTaskMapper.getExamCityGk();
    }

    /**
     * 国开城市确认信息
     *
     * @param header
     * @param body
     * @return
     */
    public Object affirmExamCityGK(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        String ecId = body.getString("ecId");
        String reason = bdsTaskMapper.getUnconfirmedReason(taskId, learnId);
        if (StringUtil.hasValue(reason)) {
            throw new BusinessException("E60032");
        }
        int ifPast = bdsTaskMapper.taskIfPast(taskId);
        if (ifPast < 1) {
            throw new BusinessException("E60033");
        }
        //修改任务状态为已完成
        bdsTaskMapper.updateTaskStatus(taskId, learnId);

        bdsTaskMapper.affirmExamCityGKInfo(taskId, learnId, ecId);

        return null;
    }

    /**
     * 查看国开考试城市选择确认结果
     *
     * @param header
     * @param body
     * @return
     */
    public Object lookGKCityAffirmResult(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");

        String reason = bdsTaskMapper.getUnconfirmedReason(taskId, learnId);
        if (StringUtil.hasValue(reason)) {
            return reason;
        }
        Map<String, String> result = bdsTaskMapper.getAffirmExamCityGKInfo(taskId, learnId);
        if (result != null && result.size() > 0) {
            if (result.get("isAffirm").equals("1")) {
                return result.get("ecName");
            } else {
                throw new BusinessException("E60042");
            }
        }
        return null;
    }

    /**
     * 获取毕业论文任务信息
     *
     * @param header
     * @param body
     * @return
     */
    public Object getStuGraduatePaperTaskInfo(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");

        Map<String, Object> paperTaskInfo = bdsTaskMapper.getStuGraduatePaperTaskInfo(taskId, learnId);
        return paperTaskInfo;
    }

    /**
     * 获取国开统考设置信息
     *
     * @param header
     * @param body
     * @return
     */
    public Object getGkUnifiedExamSet(Header header, Body body) {
        String eyId = body.getString("eyId");
        Map<String, String> result = bdsTaskMapper.getGkUnifiedExamSet(eyId);
        return result;
    }

    /**
     * 提交国开统考操作信息
     *
     * @param header
     * @param body
     * @return
     */
    public Object submitGkUnifiedExamInfo(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        String operType = body.getString("operType"); //操作类型 1：去缴费报名  2:我已知晓，下次报名
        if (StringUtil.isEmpty(operType)) {
            operType = "2";
        }
        //修改任务状态为已完成
        bdsTaskMapper.updateTaskStatus(taskId, learnId);
        bdsTaskMapper.updateGkUnifiedExamInfo(taskId, learnId, operType);
        return null;
    }

    /**
     * 获取毕业证发放选择领取信息
     *
     * @param header
     * @param body
     * @return
     */
    public Object getDiplomaSelectInfo(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        String unvsId = body.getString("unvsId");
        String pfsnLevel = body.getString("pfsnLevel");
        String grade = body.getString("grade");

        //更新状态为已查看
        bdsTaskMapper.updateDiplomaGiveTaskView(taskId, learnId);

        //获取温馨提示
        Map<String, String> warmTips = bdsTaskMapper.getDiplomaWarmTips(taskId, learnId);

        //暂无信息
        if (null == warmTips) {
            return null;
        }
        //获取毕业证发放点
        List<Map<String, String>> place = bdsTaskMapper.getDiplomaPlace(grade, unvsId, pfsnLevel, warmTips.get("diplomaId"));

        Map<String, Object> result = new HashMap<String, Object>(3);
        result.put("diplomaId", warmTips.get("diplomaId"));
        result.put("warmTips", warmTips.get("warmTips"));
        result.put("place", place);
        return result;
    }

    /**
     * 获取毕业证发放选择领取日期
     *
     * @param header
     * @param body
     * @return
     */
    public Object getDiplomaTaskDate(Header header, Body body) {
        Map<String, String> map = new HashMap<>(5);
        map.put("diplomaId", body.getString("diplomaId"));
        map.put("placeId", body.getString("placeId"));
        map.put("unvsId", body.getString("unvsId"));
        map.put("pfsnLevel", body.getString("pfsnLevel"));
        map.put("grade", body.getString("grade"));
        //获取日期
        List<String> result = bdsTaskMapper.getDiplomaTaskDate(map);
        return result;
    }

    /**
     * 获取毕业证发放选择领取时间段
     *
     * @param header
     * @param body
     * @return
     */
    public Object getDiplomaTaskTime(Header header, Body body) {
        Map<String, String> map = new HashMap<>(5);
        map.put("diplomaId", body.getString("diplomaId"));
        map.put("placeId", body.getString("placeId"));
        map.put("unvsId", body.getString("unvsId"));
        map.put("pfsnLevel", body.getString("pfsnLevel"));
        map.put("grade", body.getString("grade"));
        map.put("date", body.getString("date"));
        List<Map<String, String>> result = bdsTaskMapper.getDiplomaTaskTime(map);
        return result;
    }

    /**
     * 提交毕业证发放领取时间及地点
     *
     * @param header
     * @param body
     * @return
     */
    public Object submitDiplomaTask(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        String configId = body.getString("configId");
        String receiveAddres = body.getString("receiveAddres");
        //获取未确认原因
        String unconfirmed = bdsTaskMapper.getDiplomaUnconfirmed(taskId, learnId);
        if (null != unconfirmed && unconfirmed.length() != 0) {
            throw new BusinessException("E000125");
        }

        //获取配置容量
        Integer availableNumbers = bdsTaskMapper.getDiplomaAvailableNumbers(configId);
        if (availableNumbers == 0) {
            throw new BusinessException("E000124");
        } else {
            //更新容量
            availableNumbers = availableNumbers - 1;
            bdsTaskMapper.updateDiplomaAvailableNumbers(configId, availableNumbers);
        }

        //更新任务跟进
        bdsTaskMapper.updateDiplomaGiveTask(taskId, learnId, configId, receiveAddres);

        //修改任务状态为已完成
        bdsTaskMapper.updateTaskStatus(taskId, learnId);
        return null;
    }

    /**
     * 获取毕业证发放任务跟进信息
     *
     * @param header
     * @param body
     * @return
     */
    public Object getDiplomaGiveTask(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");

        Map<String, String> result = bdsTaskMapper.getDiplomaGiveTask(taskId, learnId);
        return result;
    }

    /**
     * 获取
     *
     * @param isView
     * @param taskId
     * @param learnId
     * @return
     */
    public Object getConfirmStuInfo(/*String isView,*/String taskId, String learnId) {
        //查看是否是去确认进来的
//        if ("0".equals(isView)) {
//            //判断信息确认是否任务重复
//            int i = bdsTaskMapper.selectConfirmFinishByLearnId(learnId);
//            if (i > 0) {
//                //任务已完成,将当前任务置为已完成
//                bdsTaskMapper.updateTaskStatus(taskId, learnId);
//                return "false";
//            }
//        }
        //获取学员信息
        HashMap<String, Object> result = bdsStudentService.getConfirmStuInfo(learnId);
        //获取任务的信息
        result.put("taskInfo",bdsTaskMapper.getTaskInfoOtherByTaskId(taskId));
        return result;
    }


    /**
     * 报考信息确认提交事件
     *
     * @param body
     * @return
     */
    public Object submitConfirmStuInfo(Header header, Body body) {
        String userId = header.getUserId();
        String userName = body.getString("realName");
        String learnId = body.getString("learnId");
        String stdId = body.getString("stdId");
        Assert.hasText(learnId, "学业信息不能为空！");
        Assert.hasText(stdId, "学生信息不能为空！");

        BdStudentModify bdStudentModify = new BdStudentModify();

        //判断是否更改了基本信息
        if ("1".equals(body.getString("isChangeBase"))) {
            //更改了信息
            BdStudentBaseInfo bds = new BdStudentBaseInfo();
            //更新学员信息
            bds.setLearnId(learnId);
            bds.setStdId(stdId);
            bds.setStdName(body.getString("stdName"));
            bds.setNation(body.getString("nation"));
            bds.setRprProvinceCode(body.getString("rprProvinceCode"));
            bds.setRprCityCode(body.getString("rprCityCode"));
            bds.setRprDistrictCode(body.getString("rprDistrictCode"));
            bds.setRprType(body.getString("rprType"));
            bds.setUpdateUserId(userId);
            bds.setUpdateUser(userName);
            bdsStudentService.updateConfirmStuInfo(bds, bdStudentModify);

        }
        //判断是否更改了学历信息
        if("1".equals(body.getString("isChangeHis"))){
            //更新学历信息
            StudentHistory history = new StudentHistory();
            history.setLearnId(learnId);
            history.setStdId(stdId);
            history.setEdcsType(body.getString("edcsType"));
            history.setUnvsName(body.getString("formerUn"));
            history.setGraduateTime(body.getString("graduateTime"));
            history.setProfession(body.getString("profession"));
            history.setDiploma(body.getString("diploma"));
            String ext1 = bdsStudentService.updateConfirmHistory(history);
            if (null != ext1) {
                bdStudentModify.setExt1(null == bdStudentModify.getExt1()? ext1 : bdStudentModify.getExt1() + ext1);
            }
        }

        //判断是否更新附件
        if("1".equals(body.getString("isChangeAnn"))){
            List<BdLearnAnnex> annexInfos = JsonUtil.str2List(body.getString("annexInfos"),BdLearnAnnex.class);
            String ext11 = bdsStudentService.updateAnnexInfos(annexInfos, userName,userId,stdId,learnId);
            if (null != ext11) {
                bdStudentModify.setExt1(null == bdStudentModify.getExt1()? ext11 : bdStudentModify.getExt1() + ext11);
            }
        }

        //学员更改了信息
        if("1".equals(body.getString("isChangeBase")) || "1".equals(body.getString("isChangeHis")) || "1".equals(body.getString("isChangeAnn"))){
            //添加记录
            bdStudentModify.setModifyType(TransferConstants.MODIFY_TYPE_CHANGE_CONFIRM_9);
            bdStudentModify.setStdId(stdId);
            bdStudentModify.setLearnId(learnId);
            bdStudentModify.setIsComplete("1");
            studentOuntService.addStudentModifyRecord(bdStudentModify, userName, userId);


            //根据学员学业ID获取招生老师openId
            String recruitOpenId = usBaseInfoMapper.getRecruitOpenIdByLearnId(learnId);
            if(StringUtil.hasValue(recruitOpenId)){
                //发送推送消息
                WechatMsgVo vo = new WechatMsgVo();

                vo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_TASK);
                vo.setTouser(recruitOpenId);
                vo.addData("first", "你的学员" + userName + "更新了报读信息");
                vo.addData("keyword1", body.getString("taskName"));
                vo.addData("keyword2", "报考信息变更");
                vo.addData("remark", "请登录学员系统查看并核对");
                vo.setIfUseTemplateUlr(false);
                RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(vo));
            }
            log.info("学员 stdID {" + stdId + "},在报考信息确认时更改了信息！");
        }

        //更新学员资料为已完善
        bdsStudentService.updateIsDateCompleted(learnId);
        //更新报考确认的信息确认字段
        bdsTaskMapper.updateConfirmInfo(learnId);
        //任务已完成,将当前任务置为已完成
        bdsTaskMapper.updateTaskStatus(body.getString("taskId"), learnId);

        return "true";
    }
    
    /**
     * 获取学员现场确认点信息
     * @param header
     * @param body
     * @return
     */
    public Object getSceneConfirmSelectInfo(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        String pfsnLevel = body.getString("pfsnLevel");

        //获取温馨提示
        Map<String, String> taskInfo = bdsTaskMapper.getTaskInfoById(taskId);
        //查看学员考区信息
        Map<String, String> testArea=bdsTaskMapper.selectTestAreaByLearnId(learnId);
        //暂无信息
        if (null == testArea) {
            return null;
        }
        //获取现场确认城市
        List<String> city = bdsTaskMapper.getSceneConfirmCity(testArea.get("taId"),pfsnLevel);
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("warmTips",taskInfo.get("warmPrompt"));
        result.put("testArea",testArea);
        result.put("city",city);
        //result.put("place",place);
        return result;
    }
    /**
     * 获取学员现场确认点列表
     * @param header
     * @param body
     * @return
     */
    public Object getSceneConfirmPlace(Header header, Body body) {
        Map<String,String> map = new HashMap<>(3);
        map.put("taId",body.getString("taId"));
        map.put("pfsnLevel",body.getString("pfsnLevel"));
        map.put("confirmCity",body.getString("confirmCity"));  
        List<String> result = bdsTaskMapper.getSceneConfirmPlace(map);
        return result;
    }
    /**
     * 获取学员现场确认日期
     * @param header
     * @param body
     * @return
     */
    public Object getSceneConfirmTaskDate(Header header, Body body) {
        Map<String,String> map = new HashMap<>(4);
        map.put("taId",body.getString("taId"));
        map.put("pfsnLevel",body.getString("pfsnLevel"));
        map.put("confirmCity",body.getString("confirmCity"));  
        map.put("confirmName",body.getString("confirmName"));
        //获取日期
        List<String> result = bdsTaskMapper.getSceneConfirmTaskDate(map);
        return result;
    }
    
    /**
     * 获取学员现场确认时间段
     * @param header
     * @param body
     * @return
     */
    public Object getSceneConfirmTaskTime(Header header, Body body) {
        Map<String,String> map = new HashMap<>(5);
        map.put("taId",body.getString("taId"));
        map.put("pfsnLevel",body.getString("pfsnLevel"));
        map.put("confirmCity",body.getString("confirmCity"));  
        map.put("confirmName",body.getString("confirmName"));
        map.put("date",body.getString("date"));
        List<Map<String, String>> result = bdsTaskMapper.getSceneConfirmTaskTime(map);
        return result;
    }
    
    /**
     * 提交学员现场确认点确认时间及地点
     * @param header
     * @param body
     * @return
     */
    public Object submitSceneConfirmTask(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        String confirmationId = body.getString("confirmationId");
        //获取配置容量
        Integer availableNumbers = bdsTaskMapper.getSceneConfirmAvailableNumbers(confirmationId);
        if (availableNumbers==null||availableNumbers == 0) {
            throw new BusinessException("E000124");
        }else {
            //更新容量
            availableNumbers = availableNumbers - 1;
            bdsTaskMapper.updateSceneConfirmAvailableNumbers(confirmationId, availableNumbers);
        }
        //更新任务跟进
        String resetId=IDGenerator.generatorId();
        bdsTaskMapper.updateSceneConfirmTask(taskId,learnId,confirmationId,resetId);

        //修改任务状态为已完成
        bdsTaskMapper.updateTaskStatus(taskId, learnId);
        return null;
    }
    
    /**
     * 获取学员现场确认点跟进信息
     * @param header
     * @param body
     * @return
     */
    public Object getSceneConfirmTask(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        //获取温馨提示
        Map<String, String> taskInfo = bdsTaskMapper.getTaskInfoById(taskId);
        Map<String,String> result = bdsTaskMapper.getSceneConfirmTask(learnId);
        result.put("warmTips",taskInfo.get("warmPrompt"));
        return result;
    }
    

}