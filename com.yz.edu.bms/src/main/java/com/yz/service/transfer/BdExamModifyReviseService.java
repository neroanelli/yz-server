package com.yz.service.transfer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.AtsAccountApi;
import com.yz.constants.*;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.educational.BdStudentSendMapper;
import com.yz.dao.finance.BdCouponMapper;
import com.yz.dao.finance.BdOrderMapper;
import com.yz.dao.recruit.StudentAllMapper;
import com.yz.dao.transfer.BdExamModifyReviseMapper;
import com.yz.dao.transfer.BdStudentChangeMapper;
import com.yz.dao.transfer.BdStudentModifyMapper;
import com.yz.dao.us.UsFollowMapper;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.WechatMsgVo;
import com.yz.model.admin.BaseUser;
import com.yz.model.communi.Body;
import com.yz.model.finance.stdfee.BdPayInfo;
import com.yz.model.finance.stdfee.BdPayableInfoResponse;
import com.yz.model.finance.stdfee.BdStdPayInfoResponse;
import com.yz.model.recruit.BdFeeInfo;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.recruit.BdStudentBaseInfo;
import com.yz.model.system.SysDict;
import com.yz.model.transfer.BdCheckRecord;
import com.yz.model.transfer.BdStudentModify;
import com.yz.model.transfer.StudentModifyMap;
import com.yz.redis.RedisService;
import com.yz.service.educational.BdStudentSendService;
import com.yz.service.finance.BdStdPayFeeService;
import com.yz.service.finance.BdTuitionService;
import com.yz.service.recruit.StudentAllService;
import com.yz.service.recruit.StudentRecruitService;
import com.yz.service.system.SysDictService;
import com.yz.task.YzTaskConstants;
import com.yz.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BdExamModifyReviseService {

	private static final Logger log = LoggerFactory.getLogger(BdExamModifyReviseService.class);

	@Autowired
	private SysDictService sysDictService;

	@Autowired
	private BdStudentOutService studentOutService;
	@Autowired
	private BdCheckRecordService checkRecordService;
	@Autowired
	private BdStudentModifyMapper studentModifyMapper;

	@Reference(version = "1.0")
	private AtsAccountApi atdApi;

	@Autowired
	private BdStudentSendService sendService;

	@Autowired
	private StudentRecruitService recruitService;

	@Autowired
	private StudentAllService studentAllService;

	@Autowired
	private BdTuitionService tuitionService;

	@Autowired
	private BdStdPayFeeService payService;

	@Autowired
	private StudentAllMapper allMapper;

	@Autowired
	private UsFollowMapper usMapper;

	@Autowired
	private BdStudentSendMapper studentSendMapper;

	@Autowired
	private StudentAllMapper stdAllMapper;

	@Autowired
	private BdCouponMapper couponMapper;

	@Autowired
	private BdStudentChangeMapper studentChangeMapper;

	@Autowired
	private BdOrderMapper orderMapper;

	@Autowired
	private BdExamModifyReviseMapper bdExamModifyReviseMapper;

	/**
	 * 查询事件
	 * @param studentModifyMap
	 * @return
	 */
	public List<Map<String,String>> findStudentAuditModify(StudentModifyMap studentModifyMap) {
		//职称权限设置。
		BaseUser user = SessionUtil.getUser();
		if(null !=  user.getJtList()){
			if(user.getJtList().contains("XCQR_ALL")){
				user.setUserLevel("1");
			}
		}
		return bdExamModifyReviseMapper.findStudentAuditModify(studentModifyMap,user);
	}

	/**
	 * 修改通过或驳回
	 * @param id
	 * @param modifyId
	 * @param checkStatus
	 * @param reason
	 */
	public void passModify(String modifyId, String checkStatus, String reason, String remark) {

		// 判断是否已修改
		int count = studentModifyMapper.selectModifyFinishCount(modifyId);

		if (count > 0) {
			return;
		}

		BdStudentModify bdStudentModify = findStudentModifyById(modifyId);
			bdStudentModify.setRemark(remark);

		// 设置转账对象
		Body body = new Body();

        BaseUser user = SessionUtil.getUser();

        //更新审核记录
		BdCheckRecord checkRecord = new BdCheckRecord();
		checkRecord.setUpdateUser(user.getRealName());
		checkRecord.setUpdateUserId(user.getUserId());
		checkRecord.setMappingId(bdStudentModify.getModifyId());
		checkRecord.setCheckOrder(bdStudentModify.getCheckOrder());
		checkRecord.setCheckStatus(checkStatus);
		bdStudentModify.setIsComplete("1");

		// 是否修改辅导书
		boolean flag = false;

		// 审核是否通过
		if (CheckConstants.EXAM_CHECK_3.equals(checkStatus)) {
//			bdStudentModify.setExt1("审核状态:审核通过");
			if (StringUtil.hasValue(bdStudentModify.getNewPfsnId())
					|| StringUtil.hasValue(bdStudentModify.getNewScholarship())
					|| StringUtil.hasValue(bdStudentModify.getNewTaId())
					|| StringUtil.hasValue(bdStudentModify.getNewUnvsId())) {

				if (!StringUtil.hasValue(bdStudentModify.getNewPfsnId())) {
					bdStudentModify.setNewPfsnId(bdStudentModify.getPfsnId());
				}
				if (!StringUtil.hasValue(bdStudentModify.getNewScholarship())) {
					bdStudentModify.setNewScholarship(bdStudentModify.getScholarship());
				}

				if (!StringUtil.hasValue(bdStudentModify.getNewTaId())) {
					bdStudentModify.setNewTaId(bdStudentModify.getTaId());
				}

				if (!StringUtil.hasValue(bdStudentModify.getNewUnvsId())) {
					bdStudentModify.setNewUnvsId(bdStudentModify.getUnvsId());
				}

				// 如修改专业，则进入辅导书操作
				if (!bdStudentModify.getNewPfsnId().equals(bdStudentModify.getPfsnId())) {
					flag = true;
				}

				// 修改后收费标准ID
				String feeId = studentModifyMapper.selectFeeStandard(bdStudentModify.getNewPfsnId(),
						bdStudentModify.getNewTaId(), bdStudentModify.getNewScholarship());

				if (!StringUtil.hasValue(feeId)) {
					String[] name = new String[] { bdStudentModify.getStdName() };
					throw new BusinessException("E000077", name); // 无收费标准
				}

				// 当前收费标准ID
				String nowFeeId = studentModifyMapper.selectNowFeeId(bdStudentModify.getLearnId());
				if (!StringUtil.hasValue(nowFeeId)) {
					throw new BusinessException("E000089"); // 现收费标准不存在
				}
				// 如果收费标准变更

				String offerId = studentModifyMapper.selectOfferId(bdStudentModify.getNewPfsnId(),
						bdStudentModify.getNewTaId(), bdStudentModify.getNewScholarship());

				// 已缴金额
				BigDecimal paidAmount = BigDecimal.ZERO;

				String amount = studentModifyMapper.selectPaidAmountByLearnId(bdStudentModify.getLearnId());
				if (StringUtil.hasValue(amount)) {
					paidAmount = AmountUtil.str2Amount(amount);
				}
				// 废弃订单，重新初始化
				studentModifyMapper.updateStdStageByLearnId(bdStudentModify.getLearnId());//修改学业阶段为意向学员，
				BdStudentBaseInfo in=stdAllMapper.getStudentBaseInfo(bdStudentModify.getStdId());
				BdLearnInfo learnInfo = new BdLearnInfo();
				learnInfo.setUserId(in.getUserId());
				learnInfo.setCreateUser(user.getRealName());
				learnInfo.setCreateUserId(user.getUserId());
				learnInfo.setFeeId(feeId);
				learnInfo.setMobile(bdStudentModify.getMobile());
				learnInfo.setStdName(bdStudentModify.getStdName());
				learnInfo.setStdId(bdStudentModify.getStdId());
				learnInfo.setUpdateUser(user.getRealName());
				learnInfo.setUpdateUserId(user.getUserId());
				learnInfo.setPfsnId(bdStudentModify.getNewPfsnId());
				learnInfo.setUnvsId(bdStudentModify.getNewUnvsId());
				learnInfo.setTaId(bdStudentModify.getTaId());
				learnInfo.setOfferId(offerId);
				learnInfo.setRecruitType(bdStudentModify.getRecruitType());
				learnInfo.setStdStage(StudentConstants.STD_STAGE_PURPOSE);
				learnInfo.setLearnId(bdStudentModify.getLearnId());

				// 费用是否有变化
				boolean isNotChange = modifyInitOrder(learnInfo);

				// 学员阶段改回来
				studentModifyMapper.updateStdStageBack(bdStudentModify.getLearnId(), bdStudentModify.getStdStage());

				// 如果订单发生了变化
				if (!isNotChange) {

					if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
						// 设置转账对象
						body.put("accType", FinanceConstants.ACC_TYPE_DEMURRAGE);
						body.put("stdId", bdStudentModify.getStdId());
						body.put("amount", paidAmount);
						body.put("action", FinanceConstants.ACC_ACTION_IN);
						body.put("excDesc", "网报信息修改，"+bdStudentModify.getGrade()+"年级已缴费用退至滞留账户");
						body.put("mappingId", modifyId);
						atdApi.trans(body);
					}

					if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
						autoPayTheFees(paidAmount, bdStudentModify);
					}
				}

				bdStudentModify.setFeeId(feeId);
				bdStudentModify.setOfferId(offerId);
			}
			//身份证改完处理生日
			if(StringUtil.hasValue(bdStudentModify.getNewIdCard())){
				studentAllService.updateStudentBirthday(bdStudentModify);
			}


			// 通过则修改数据bd_student_enroll bd_learn_info bd_student_info bd_student_history
			updateBdStudentEnroll(bdStudentModify);// 根据学业id
			updateBdLearnInfo(bdStudentModify);// 根据学业id
			updateBdStudentInfo(bdStudentModify);// 根据学员id
			updateupdateBdStudentHistory(bdStudentModify);//更新原学历信息。
			// 筑梦计划 报读就送1200优惠券 如果由筑梦转报到筑梦，不再赠送优惠券
			if ("25".equals(bdStudentModify.getNewScholarship()) && !("25").equals(bdStudentModify.getScholarship())) {
				couponMapper.sendCoupon(bdStudentModify.getStdId(), bdStudentModify.getNewScholarship());
			}
			// 如果由筑梦转报到其他优惠类型，把之前赠送的优惠券收回
			if (StringUtil.hasValue(bdStudentModify.getScholarship()) && "25".equals(bdStudentModify.getScholarship()) && !"25".equals(bdStudentModify.getNewScholarship())) {
				couponMapper.delCoupon(bdStudentModify.getStdId(), bdStudentModify.getScholarship());
			}
			if (flag) {
				String tutorAmount = studentChangeMapper.selectTutorSerialAmount(bdStudentModify.getLearnId(), "Y0");
				// 找出变更专业后不一致的考试科目
				List<String> unSameList = studentModifyMapper.selectUnSameTestSubjectByPfsnId(bdStudentModify.getPfsnId(), bdStudentModify.getNewPfsnId());
				// 考试科目不一直，重新发放辅导书
				if (unSameList.size() > 0 && StringUtil.isNotBlank(tutorAmount)) {

					// 删除未发记录
					List<String> sendIds = studentSendMapper.selectFdSendIds(bdStudentModify.getLearnId());
					for (String sendId : sendIds) {
						studentSendMapper.deleteBookSend(sendId);
						studentSendMapper.deleteTextBookSend(sendId);
					}

					// 重置辅导书发放
					sendService.sendBook(bdStudentModify.getLearnId(), null, true,"1");
				}
			}
			sendWacth(bdStudentModify.getLearnId(),
					"异动修改成功",
					"您的学员"+bdStudentModify.getStdName()+"，网报后的异动申请已修改成功",
					"可前往学员系统查看具体的修改内容，谢谢~"
			);
		} else {
			checkRecord.setReason(reason);
//			bdStudentModify.setExt1("审核状态:审核被驳回，驳回原因：" + reason);
			sendWacth(bdStudentModify.getLearnId(),
					"异动修改驳回",
					"您的学员"+bdStudentModify.getStdName()+"，网报后的异动申请被驳回，驳回原因如下：\n" + reason,
					"请知悉，谢谢~"
					);
		}
		updateStudentModify(bdStudentModify);

		checkRecordService.updateBdCheckRecord(checkRecord);

	}


	//发送微信推送
	public void sendWacth(String learnId,String keyword1,String keyword2,String remark){

		//根据学员学业ID获取招生老师openId
		String recruitOpenId = bdExamModifyReviseMapper.getRecruitOpenIdByLearnId(learnId);
		if(StringUtil.hasValue(recruitOpenId)){
			//发送推送消息
			WechatMsgVo vo = new WechatMsgVo();

			vo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_TASK);
			vo.setTouser(recruitOpenId);
			vo.addData("first", "网报后异动修改通知");
			vo.addData("keyword1", keyword1);
			vo.addData("keyword2", keyword2);
			vo.addData("remark", remark);
			vo.setIfUseTemplateUlr(false);
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(vo));
		}

	}


	public BdStudentModify findStudentModifyById(String modifyId) {
		// TODO Auto-generated method stub
		return studentModifyMapper.findStudentModifyById(modifyId);
	}

	private boolean modifyInitOrder(BdLearnInfo learnInfo) {

		boolean flag = checkInitOrder(learnInfo);

		// 检查是否需要刷新订单
		if (!flag) {
			studentModifyMapper.destroyOrderByLearnId(learnInfo.getLearnId());
			recruitService.initStudentOrder(learnInfo);

		}

		return flag;
	}

	// 成教异动校验费用科目
	private final static String[] CJ_CHECK_ITEM_CODE = new String[] { "Y0" };

	private boolean checkInitOrder(BdLearnInfo learnInfo) {
		// 成教 对比Y0辅导费用
		if (StudentConstants.RECRUIT_TYPE_CJ.equals(learnInfo.getRecruitType())) {
			return checkCondition(learnInfo, CJ_CHECK_ITEM_CODE);
		}

		return false;
	}

	private boolean checkCondition(BdLearnInfo learnInfo, String[] itemCodes) {

		for (String itemCode : itemCodes) {

			BdFeeInfo amountInfo = orderMapper.getFeeInfo(learnInfo.getFeeId(), learnInfo.getOfferId(), itemCode);
			if (null == amountInfo) {
				return false;
			}

			if (!StringUtil.hasValue(amountInfo.getAmount())) {
				return false;
			}

			// 现在应缴额
			String nowAmount = orderMapper.selectItemCodePayable(learnInfo.getLearnId(), itemCode);

			String feeAmount = null;

			// 收费标准应缴额
			if (!StringUtil.hasValue(amountInfo.getDiscount())) {
				feeAmount = amountInfo.getAmount();
			} else {
				feeAmount = BigDecimalUtil.substract(amountInfo.getAmount(), amountInfo.getDiscount());
				if (AmountUtil.str2Amount(feeAmount).compareTo(BigDecimal.ZERO) < 0) {
					feeAmount = "0.00";
				}
			}

			// 如果费用不一直 返回false
			if (AmountUtil.str2Amount(nowAmount).compareTo(AmountUtil.str2Amount(feeAmount)) != 0) {
				return false;
			}

		}

		return true;

	}

	/**
	 * 自动缴费
	 *
	 * @param paidAmount
	 * @param bdStudentModify
	 * @param delayAmount
	 */
	public void autoPayTheFees(BigDecimal paidAmount, BdStudentModify bdStudentModify) {

		BdPayInfo payInfo = new BdPayInfo();
		BigDecimal payableAmount = paidAmount;
		List<String> itemCodes = new ArrayList<String>();
		List<String> years = new ArrayList<String>();

		BdPayableInfoResponse response = payService.selectPayableInfoByLearnId(bdStudentModify.getLearnId(),
				FinanceConstants.ORDER_STATUS_UNPAID);
		BigDecimal tutorPayable = BigDecimal.ZERO;

		if (StudentConstants.RECRUIT_TYPE_CJ.equals(bdStudentModify.getRecruitType())
				&& paidAmount.compareTo(BigDecimal.ZERO) > 0 && null != response.getTutorPayInfos()
				&& response.getTutorPayInfos().size() > 0) { // 成教、有缴费、有辅导费的进入

			for (BdStdPayInfoResponse res : response.getTutorPayInfos()) {
				tutorPayable = BigDecimalUtil.add(tutorPayable, AmountUtil.str2Amount(res.getPayable()));
			}
			if (paidAmount.compareTo(tutorPayable) >= 0) { // 如果大于现有YO费用
				// 已缴总额减去Y0
				paidAmount = BigDecimalUtil.substract(paidAmount, tutorPayable);
				for (BdStdPayInfoResponse res : response.getTutorPayInfos()) { // 循环遍历加入支付科目
					itemCodes.add(res.getItemCode());
				}

			} else {
				paidAmount = BigDecimal.ZERO;
			}

		}

		if (null != itemCodes && itemCodes.size() > 0) {
			payInfo.setItemCodes(itemCodes.toArray(new String[itemCodes.size()]));
			payInfo.setLearnId(bdStudentModify.getLearnId());
			payInfo.setZmDeduction("0.00");
			payInfo.setPaidAmount(payableAmount.toString());
			payInfo.setPaymentType(FinanceConstants.PAYMENT_TYPE_DELAY);
			payInfo.setAccDeduction("0.00");
			if (null != years && years.size() > 0) {
				payInfo.setYears(years.toArray(new String[years.size()]));
			}
			payInfo.setGiveZhimi(false);
			tuitionService.payTuition(payInfo, false, true);
		} else {

			try {
				// 发送微信推送，重新缴费
				String openId = usMapper.selectOpenIdByStdId(bdStudentModify.getStdId());
				String title = bdStudentModify.getStdName() + " 您好，您有一个重要通知，内容如下：";
				String msgName = "信息变更成功通知";
				String code = "YZ";
				String content = "由于您变更了院校专业信息，请在远智学堂进行重新缴费，原缴纳金额已退至您的可用账户，可在缴费时使用，谢谢。";

				WechatMsgVo msgVo = new WechatMsgVo();
				msgVo.setTouser(openId);
				msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_STUDENT_INFORM);
				msgVo.addData("title", title);
				msgVo.addData("msgName", msgName);
				msgVo.addData("code", code);
				msgVo.addData("content", content);

				RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
				// wechatApi.sendStudentInform(openId, title, code, content,
				// msgName, null);
			} catch (Exception e) {
				log.error("网报信息异动缴费推送异常：" + JsonUtil.object2String(e));
			}
		}
	}

	public void updateBdStudentEnroll(BdStudentModify bdStudentModify) {
		// TODO Auto-generated method stub
		studentModifyMapper.updateBdStudentEnroll(bdStudentModify);
	}

	public void updateBdLearnInfo(BdStudentModify bdStudentModify) {
		// TODO Auto-generated method stub
		studentModifyMapper.updateBdLearnInfo(bdStudentModify);
	}

	public void updateBdStudentInfo(BdStudentModify bdStudentModify) {
		// TODO Auto-generated method stub
		studentModifyMapper.updateBdStudentInfo(bdStudentModify);
	}

	public void updateStudentModify(BdStudentModify studentModify) {
		// TODO Auto-generated method stub
		studentModifyMapper.updateByPrimaryKeySelective(studentModify);
	}

	public HashMap<String,String> findStudentInfo(String learnId) {

		return bdExamModifyReviseMapper.findStudentInfo(learnId);

	}

	private void updateupdateBdStudentHistory(BdStudentModify bdStudentModify) {
		bdExamModifyReviseMapper.updateupdateBdStudentHistory(bdStudentModify);
	}

}
