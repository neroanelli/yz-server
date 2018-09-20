package com.yz.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.conf.YzSysConfig;
import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.BdOrderMapper;
import com.yz.dao.BdStudentOutMapper;
import com.yz.dao.BdsLearnMapper;
import com.yz.dao.StudentAllMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.BdOrderRefund;
import com.yz.model.BdStudentOut;
import com.yz.model.StudentOutListMap;
import com.yz.model.WechatMsgVo;
import com.yz.model.student.BdCheckRecord;
import com.yz.model.student.BdStudentModify;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdStudentOutService {
	private static final Logger log = LoggerFactory.getLogger(BdStudentOutService.class);

	@Autowired
	private YzSysConfig yzSysConfig;

	@Autowired
	private BdStudentOutMapper studentOutMapper;

	@Autowired
	private BdOrderMapper bdOrderMapper;

	@Autowired
	private StudentAllMapper allMapper;

	@Autowired
	private BdsLearnMapper bdsLearnMapper;

	public Object studentOutList(String learnId) {
		List<StudentOutListMap> map= studentOutMapper.studentOutList(learnId);
		map.forEach(v->{
			for(int i=0;i<v.getCheck().size();i++){
				if(v.getCheck().get(i).getCheckStatus().equals("3")){
					v.setStatus("已驳回");//审核失败
					v.setReasonRemark(v.getCheck().get(i).getReason());
				}
			}
			if(!StringUtil.hasValue(v.getStatus())||!v.getStatus().equals("已驳回")){
				v.setStatus(v.getIsComplete().equals("0")?"审核中":"审核通过");
			}
		});
		return map;
	}

	public void addStudentOut(String empId, String learnId, String userId, String realName, String tempUrl,
			String reason, String remark) {
		int count = studentOutMapper.selectOutCount(learnId);
		if (count > 0) {
			throw new BusinessException("E000117"); // 请勿重复提交申请
		}
		Map<String, String> learnInfo = bdsLearnMapper.selectLearnInfoByLearnId(learnId);
		BdStudentOut studentOut = new BdStudentOut();
		studentOut.setStdId(learnInfo.get("stdId"));
		studentOut.setReason(reason);
		studentOut.setRemark(remark);
		studentOut.setLearnId(learnId);
		studentOut.setCheckOrder("2");
		studentOut.setEmpId("");
		studentOut.setCreateUser(realName);
		studentOut.setStdStage(learnInfo.get("stdStage"));
		studentOut.setCreateUserId(userId);
		studentOut.setUpdateUser(realName);
		studentOut.setUpdateUserId(userId);

		// 学员状态是否为在读
		// 审批不需要通过教务
		studentOut.setCheckType(TransferConstants.CHECK_TYPE_OUT_BEFORE_STUDYING);

		studentOut.setOutId(IDGenerator.generatorId());
		studentOutMapper.insertSelective(studentOut);
		
		boolean flag = false;
		// 学员状态是否为在读
		if (StudentConstants.STD_STAGE_STUDYING.equals(learnInfo.get("stdStage"))) {
			flag = true;
		}
		
			// 发送微信推送给学员
			Map<String, String> map1 = studentOutMapper.selectUserInfoForLearnId(learnInfo.get("stdId"));
			// 如果存在bind_id，就发送推送
			if (map1 != null && map1.containsKey("bind_id") && StringUtil.hasValue(map1.get("bind_id"))) {
					WechatMsgVo vo = new WechatMsgVo();
					vo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_TASK);
					vo.setTouser(map1.get("bind_id"));
					vo.addData("first", "提交了退学申请！");
					vo.addData("keyword1", "退学申请提醒");
					vo.addData("keyword2", map1.get("real_name") + ",您的退学申请 3个工作日会有相关的老师跟进确认，请留意电话。退学申请流程15个工作日完成，咨询电话4008336013。");
					vo.addData("remark", "退学申请已受理！");
					RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(vo));
			}
				
				
				Map<String, String> studentInfo = studentOutMapper.findStudentInfoByLearnId(studentOut.getLearnId());
			
				if (studentInfo != null && studentInfo.size() > 0) {
					String pfsnName = studentInfo.get("bsapfsnName");  //专业名称
					String grade = studentInfo.get("grade"); 		   //年级
					String unvsName = studentInfo.get("bsaunvsName");  //院校名称
					String stdName = studentInfo.get("stdName");       //退学学员姓名
					String recruitName=  studentInfo.get("oetName");   //招生老师姓名
					String bindId = null;
					if (flag && "1".equals(studentInfo.get("oerStatus"))) {
						bindId = studentInfo.get("oerBindId"); // 班主任bind_id
					} else {
						//如果招生老师离职就发给校监
						if("2".equals(studentInfo.get("oetStatus"))){
							if(StringUtil.hasValue(studentInfo.get("oetEmpId"))){
								if(StringUtil.hasValue(studentOutMapper.findXjBandId(studentInfo.get("oetEmpId")))){
									bindId = studentOutMapper.findXjBandId(studentInfo.get("oetEmpId"));
								}
							}
						}else{
							bindId = studentInfo.get("oetBindId"); // 招生老师bind_id
						}
					}
					String pfsnLevel = studentInfo.get("pfsnLevel");
					if (pfsnLevel.indexOf("高中") > 0) {
						pfsnLevel = "[专科]";
					} else {
						pfsnLevel = "[本科]";
					}
					// 公众号提醒
					if (!StringUtil.isEmpty(bindId)&& !StringUtil.isEmpty(stdName)) {
						WechatMsgVo vo = new WechatMsgVo();
						vo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_TASK);
						vo.setTouser(bindId);
						vo.addData("first", "您有学员提交了退学申请,请在两个工作日跟进联系!");
						vo.addData("keyword1", "退学申请提醒");
						vo.addData("keyword2", "退学申请");
						//针对校监做区别
						StringBuffer remarkBuf = new StringBuffer();
						String remind = "学员姓名："+stdName;
						remarkBuf.append(remind);
						remarkBuf.append("\n报读信息："+unvsName+"["+pfsnName+"]专业" +grade+"级"+pfsnLevel);
						if("2".equals(studentInfo.get("oetStatus"))){
							remarkBuf.append("\n所属招生老师："+recruitName+"已离职");
						}
						vo.addData("remark", remarkBuf.toString());
						RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(vo));
					}
				}
		
		/*
		 * 根据审核类型生成审核流程结束
		 */
		studentOrderRefund(studentOut.getLearnId(), studentOut.getStdId(), studentOut.getCheckType(), empId, realName,
				userId);
		log.debug("--------------初始化订单退款成功---------------");
		/*
		 * 根据审核类型生成审核流程开始
		 */
		// 获取审核权重
		List<Map<String, String>> checkWeights = null;
		// if
		// (StudentConstants.STD_STAGE_STUDYING.equals(studentOut.getStdStage()))
		// {
		// 审批需要通过教务
		// checkWeights =
		// getCheckWeight(TransferConstants.CHECK_TYPE_OUT_AFTER);
		// } else {
		// 审批不需要通过教务
		checkWeights = getCheckWeight(TransferConstants.CHECK_TYPE_OUT_BEFORE_STUDYING);
		// }
		for (Map<String, String> map : checkWeights) {
			// 初始化审核记录
			BdCheckRecord checkRecord = new BdCheckRecord();
			checkRecord.setMappingId(studentOut.getOutId());
			checkRecord.setCheckOrder(map.get("checkOrder"));
			checkRecord.setCheckType(map.get("checkType"));
			checkRecord.setJtId(map.get("jtId"));
			checkRecord.setCrId(IDGenerator.generatorId());
			studentOutMapper.insertSelectiveRecord(checkRecord);
		}

		// 添加退学 学员变更记录
		BdStudentModify studentModify = new BdStudentModify();
		studentModify.setLearnId(studentOut.getLearnId());
		studentModify.setStdId(studentOut.getStdId());
		studentModify.setExt1("添加了退学学员申请!");
		studentModify.setIsComplete("1");
		studentModify.setModifyType(TransferConstants.MODIFY_TYPE_CHANGE_normalopt_5);
		addStudentModifyRecord(studentModify, realName, userId);
		if (StringUtil.hasValue(tempUrl)) {
			String bucket = yzSysConfig.getBucket();
			try {
				StringBuffer s = new StringBuffer(Type.OUT.get()).append("/");
				if (StringUtil.hasValue(studentOut.getOutId())) {
					s.append(studentOut.getOutId()).append("/");
				}
				s.append(tempUrl);
				String url = s.toString();
				FileUploadUtil.copyToDisplay(tempUrl, url);
				studentOutMapper.updateFileUrl(studentOut.getOutId(), url, tempUrl);
			} catch (Exception e) {
				log.error("--------------- 上传文件失败");
				throw new BusinessException("E000118"); // 文件上传失败；
			}
		}
	}

	/**
	 * 添加学员变更记录
	 * 
	 * @param studentModify
	 */
	public void addStudentModifyRecord(BdStudentModify studentModify, String userName, String userId) {
		studentModify.setCreateUser(userName);
		studentModify.setCreateUserId(userId);
		if (StringUtil.isEmpty(studentModify.getStdId())) {
			studentModify.setStdId(allMapper.selectStdIdByLearnId(studentModify.getLearnId()));
		}
		if (StringUtil.hasValue(studentModify.getNewScholarship())) {
			// TODO 临时加入优惠分组 后续可以从页面带过来
			String scholarship = studentModify.getScholarship();
			Map<String, String> dict = studentOutMapper.getDict("scholarship." + scholarship);
			studentModify.setSg(dict.get("ext1"));

			String newScholarship = studentModify.getNewScholarship();
			Map<String, String> newDict = studentOutMapper.getDict("scholarship." + newScholarship);
			studentModify.setNewSg(dict.get("ext1"));
		}
		studentModify.setModifyId(IDGenerator.generatorId());
		studentOutMapper.insertSelectiveBdStudentModify(studentModify);
	}

	/**
	 * 学员退学退费状态更改
	 * 
	 * @param learnId
	 * @param checkType
	 * @return
	 */
	public String studentOrderRefund(String learnId, String stdId, String checkType, String empId, String realName,
			String userId) {

		int count = bdOrderMapper.selectCountOrderRefunde(learnId, checkType);
		if (count > 0) {
			return null;
		}
		BdOrderRefund refund = new BdOrderRefund();
		refund.setEmpId(empId);
		refund.setLearnId(learnId);
		refund.setUpdateUser(realName);
		refund.setUpdateUserId(userId);
		refund.setCheckType(checkType);
		refund.setStdId(stdId);
		bdOrderMapper.refundingOrder(refund.getLearnId());
		refund.setRefundId(IDGenerator.generatorId());
		bdOrderMapper.insertRefundInfo(refund);

		return refund.getRefundId();
	}

	public List<Map<String, String>> getCheckWeight(String stdStage) {
		// TODO Auto-generated method stub
		return studentOutMapper.getCheckWeight(stdStage);
	}
}
