package com.yz.job.service;

import com.dangdang.ddframe.job.executor.AbstractElasticJobExecutor;
import com.yz.constants.EducationConstants;
import com.yz.constants.StudentConstants;
import com.yz.job.common.YzJobInfo;
import com.yz.job.common.YzTaskContext;
import com.yz.job.dao.BdsCourseMapper;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 上课提醒（极光推送）
 * @author jyt
 */
@Service
public class ClassRemindPushService {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JPushService jPushService;
	
	@Autowired
	private BdsCourseMapper courseMapper;
	
	/**
	 * 发送上课提醒通知（极光推送）
	 * @param startTimePre
	 * @param endTimePre
	 */
	public void sendClassRemindNotice(String remindTime,String startTimePre, String endTimePre){
		// 查找学科课程
		List<Map<String, String>> courseInfoXK = courseMapper.selectLiveCourse(EducationConstants.COURSE_TYPE_XK,startTimePre, endTimePre);
		sendCourseInfoMsg(remindTime,courseInfoXK, StudentConstants.STD_STAGE_STUDYING, EducationConstants.COURSE_TYPE_XK);
		// 辅导课程
		List<Map<String, String>> courseInfoFD = courseMapper.selectLiveCourse(EducationConstants.COURSE_TYPE_FD,startTimePre, endTimePre);
		sendCourseInfoMsg(remindTime,courseInfoFD, StudentConstants.STD_STAGE_HELPING, EducationConstants.COURSE_TYPE_FD);
	}
	/**
	 * {@link AbstractElasticJobExecutor}
	 * 推送消息
	 * @param courseInfo
	 * @param stdStage
	 * @param courseType
	 */
	private void sendCourseInfoMsg(String remindTime,List<Map<String, String>> courseInfo, String stdStage, String courseType) {
		if (null != courseInfo && courseInfo.size() > 0) {

			logger.info("class course info {}",JsonUtil.object2String(courseInfo));

			for (Map<String, String> map : courseInfo) {
				String courseId = map.get("courseId");
				String courseName = map.get("courseName");
				StringBuffer classTime = new StringBuffer();

				String date = map.get("cpDate");
				String startTime = map.get("startTime");
				String endTime = map.get("endTime");

				classTime.append(date).append(" ").append(startTime).append(" 至 ").append(endTime);

				/**
				 * 根据课程id与学员阶段查询userId
				 */
				List<Map<String, String>> userIds = new ArrayList<>();

				if (EducationConstants.COURSE_TYPE_FD.equals(courseType)) {
					userIds = courseMapper.selectUserIdByFDCourseId(courseId);
				} else if(EducationConstants.COURSE_TYPE_XK.equals(courseType)) {
					userIds = courseMapper.selectUserIdByXKCourseId(courseId, stdStage);
				}
				// 查询所有需发送的registration_id
				if (null != userIds && userIds.size() > 0) {
					List<String> registrationIds = userIds.stream().map(x->x.get("registration_id")).collect(Collectors.toList());
					registrationIds = registrationIds.stream().filter(x-> !StringUtil.isEmpty(x)).collect(Collectors.toList());
					jPushService.pushMsg(registrationIds,"两小时后有课程直播噢~");
					// 增加task日志
					YzJobInfo info = YzTaskContext.getTaskContext().getContext(YzJobInfo.class);
					YzTaskContext.getTaskContext().addEventDetail(courseName+"(jpush)", String.format(info.getLogFormat(),
							classTime.toString(), remindTime, courseName, registrationIds.size()));
				}
			}

		}
	}
}
