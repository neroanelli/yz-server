package com.yz.service.finance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.AtsAccountApi;
import com.yz.api.BdsPaymentApi;
import com.yz.api.UsInfoApi;
import com.yz.constants.FeeConstants;
import com.yz.constants.FinanceConstants;
import com.yz.constants.StudentConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.enroll.BdStdRegisterMapper;
import com.yz.dao.finance.AtsAccountMapper;
import com.yz.dao.finance.BdFeeItemMapper;
import com.yz.dao.finance.BdStdPayFeeMapper;
import com.yz.dao.transfer.BdStudentModifyMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.communi.Body;
import com.yz.model.finance.AtsAccount;
import com.yz.model.finance.BdOrder;
import com.yz.model.finance.BdSubOrder;
import com.yz.model.finance.coupon.BdCoupon;
import com.yz.model.finance.feeitem.BdFeeItem;
import com.yz.model.finance.stdfee.BdPayInfo;
import com.yz.model.finance.stdfee.BdPayableInfoResponse;
import com.yz.model.finance.stdfee.BdPayableQuery;
import com.yz.model.finance.stdfee.BdQRCodePayableInfoResponse;
import com.yz.model.finance.stdfee.BdStdPayInfoResponse;
import com.yz.model.finance.stdfee.BdStudentSerial;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.transfer.BdStudentModify;
import com.yz.service.educational.BdStudentSendService;
import com.yz.service.recruit.StudentRecruitService;
import com.yz.service.transfer.BdStudentModifyService;
import com.yz.util.AmountUtil;
import com.yz.util.BigDecimalUtil;
import com.yz.util.StringUtil;

/**
 * 学员缴费 Description:
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年6月13日.
 *
 */
@Service
@Transactional
public class BdStdPayFeeService {

	private static Logger log = LoggerFactory.getLogger(BdStdPayFeeService.class);

	@Autowired
	private BdStdPayFeeMapper payMapper;

	@Autowired
	private BdFeeItemMapper itemMapper;

	@Reference(version = "1.0")
	private AtsAccountApi accountApi;

	@Autowired
	private AtsAccountMapper accountMapper;

	@Autowired
	private BdStudentSendService sendService;

	@Reference(version = "1.0")
	private UsInfoApi usApi;

	@Reference(version = "1.0")
	private BdsPaymentApi bdsPaymentApi;

	@Autowired
	private BdStudentModifyMapper studentModifyMapper;

	@Autowired
	private StudentRecruitService recruitService;

	@Reference(version = "1.0")
	private AtsAccountApi atdApi;

	@Autowired
	private BdStudentModifyService modifyService;

	@Autowired
	private BdTuitionService tuitionService;

	@Autowired
	private BdStdPayFeeService payService;

	@Autowired
	private BdStdRegisterMapper regMapper;

	/**
	 * 学员缴费信息分页查询
	 * 
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object selectStdFeePayPage(int start, int length, BdPayableQuery query) {
		PageHelper.offsetPage(start, length);
		List<BdPayableInfoResponse> stdFee = payMapper.selectStdPayFeeByPage(query);

		if (stdFee != null) {
			for (BdPayableInfoResponse response : stdFee) {
				AtsAccount account = new AtsAccount();
				account.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
				account.setStdId(response.getStdId());

				account = accountMapper.getAccount(account);

				String accAmount = "0.00";
				if (account != null) {
					accAmount = account.getAccAmount();
					if (StringUtil.isEmpty(accAmount)) {
						accAmount = "0.00";
					}
				}
				response.setAccAmount(accAmount);

				// 根据条件获取当前配置的收费标准
				String nowFeeId = studentModifyMapper.selectFeeStandard(response.getPfsnId(), response.getTaId(),
						response.getScholarship());
				response.setNowFeeId(nowFeeId);

				String feeName = payMapper.selectFeeNameByLearnId(response.getLearnId());
				response.setFeeName(feeName);

				String offerName = payMapper.selectOfferNameByLearnId(response.getLearnId());
				response.setOfferName(offerName);

			}

		}

		return new IPageInfo((Page) stdFee);
	}

	public Map<String, String> selectPayableInfoByStdId(String stdId) {
		Map<String, String> stdInfo = payMapper.selectStdInfoByStdId(stdId);

		AtsAccount account = new AtsAccount();
		account.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
		account.setStdId(stdInfo.get("stdId"));

		account = accountMapper.getAccount(account);

		String accAmount = "0.00";
		if (account != null) {
			accAmount = account.getAccAmount();
			if (StringUtil.isEmpty(accAmount)) {
				accAmount = "0.00";
			}
		}

		AtsAccount zmAccount = new AtsAccount();
		zmAccount.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		zmAccount.setStdId(stdInfo.get("stdId"));
		zmAccount = accountMapper.getAccount(zmAccount);
		String zmAmount = "0";
		if (zmAccount != null) {
			zmAmount = zmAccount.getAccAmount();
			if (StringUtil.isEmpty(zmAmount)) {
				zmAmount = "0";
			} else {
				double x = Double.parseDouble(zmAmount);
				zmAmount = String.valueOf((int) x);
			}
		}
		stdInfo.put("accAmount", accAmount);
		stdInfo.put("zmAmount", zmAmount);
		return stdInfo;
	}

	public BdPayableInfoResponse selectPayableInfoByLearnId(String learnId, String subOrderStatus) {
		BdPayableInfoResponse response = payMapper.selectPayableInfoByLearnId(learnId, subOrderStatus);

		AtsAccount account = new AtsAccount();
		account.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
		account.setStdId(response.getStdId());

		account = accountMapper.getAccount(account);

		String accAmount = "0.00";
		if (account != null) {
			accAmount = account.getAccAmount();
			if (StringUtil.isEmpty(accAmount)) {
				accAmount = "0.00";
			}
		}

		AtsAccount zmAccount = new AtsAccount();
		zmAccount.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		zmAccount.setStdId(response.getStdId());
		zmAccount = accountMapper.getAccount(zmAccount);
		String zmAmount = "0";
		if (zmAccount != null) {
			zmAmount = zmAccount.getAccAmount();
			if (StringUtil.isEmpty(zmAmount)) {
				zmAmount = "0";
			} else {
				double x = Double.parseDouble(zmAmount);
				zmAmount = String.valueOf((int) x);
			}
		}

		response.setAccAmount(accAmount);
		response.setZmAmount(zmAmount);
		return response;
	}

	/**
	 * 查询对应状态的缴费科目信息
	 * 
	 * @param learnId
	 * @param subOrderStatus
	 * @return
	 */
	public Object selectPayableInfo(String learnId, String subOrderStatus) {
		return payMapper.selectPayableInfo(learnId, subOrderStatus);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object payDetail(int start, int length, String learnId) {
		PageHelper.offsetPage(start, length);
		List<BdStudentSerial> serials = payMapper.selectPayDetailByLearnId(learnId);
		return new IPageInfo((Page) serials);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object stdPayDetail(int start, int length, String stdId) {
		PageHelper.offsetPage(start, length);
		List<BdStudentSerial> serials = payMapper.selectPayDetailByStdId(stdId);
		return new IPageInfo((Page) serials);
	}

	public String getUserId(String stdId) {
		String userId = payMapper.getUserId(stdId);
		return userId;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object findStudentInfo(int rows, int page, String sName) {
		PageHelper.startPage(page, rows);
		List<HashMap<String, String>> list = payMapper.findStudentInfo(sName);
		return new IPageInfo((Page) list);
	}

	public void addPayableItem(String itemCode, String amount, String learnId) {
		BdOrder order = payMapper.selectOrder(learnId);
		Map<String, String> map = payMapper.selectStdInfoByStdId(order.getStdId());
		BdSubOrder subOrder = new BdSubOrder();
		subOrder.setUserId(map.get("userId"));
		subOrder.setSubLearnId(learnId);
		subOrder.setItemCode(itemCode);
		subOrder.setFeeAmount(amount);
		subOrder.setMobile(order.getMobile());
		subOrder.setOrderNo(order.getOrderNo());
		subOrder.setPayable(amount);
		subOrder.setStdId(order.getStdId());
		subOrder.setStdName(order.getStdName());
		subOrder.setSubOrderStatus(FinanceConstants.ORDER_STATUS_UNPAID);
		subOrder.setSubOrderNo(IDGenerator.generatorId());
		payMapper.addPayableItem(subOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object findItemCodeHaveNot(int rows, int page, String sName, String learnId) {
		PageHelper.startPage(page, rows);

		List<BdStdPayInfoResponse> list = payMapper.findItemCodeHaveNot(sName, learnId);

		return new IPageInfo((Page) list);
	}

	/**
	 * 审核检查
	 * 
	 * @param learnId
	 * @param itemCodes
	 * @param updateUser
	 * @param updateUserId
	 * @param years
	 */
	public void checkPayChange(String learnId, String[] itemCodes, String updateUser, String updateUserId,
			String[] years) {

		String recruitType = payMapper.selectRecruitType(learnId);

		String stdStage = payMapper.selectStdStageByLearnId(learnId);

		if (null != years && years.length > 0) {
			for (String year : years) {
				if (FinanceConstants.FEE_ITEM_YEAR_FIRST.equals(year)) {
					if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
						if (StudentConstants.STD_STAGE_ENROLLED.equals(stdStage)) {
							payMapper.updateStdStage(learnId, StudentConstants.STD_STAGE_REGISTER);
							List<Map<String, String>> list = regMapper.selectTmpAddRecord(learnId, updateUser,
									updateUserId);
							for (Map<String, String> map : list) {
								map.put("register_id", IDGenerator.generatorId());
							}
							regMapper.insertFirstRegist(list);
						}
					} else if (StudentConstants.RECRUIT_TYPE_GK.equals(recruitType)) {
						if (StudentConstants.STD_STAGE_PURPOSE.equals(stdStage)) {
							payMapper.updateStdStage(learnId, StudentConstants.STD_STAGE_HELPING);
							// 修改学业辅导费审核时间
							payMapper.updateTutionTime(learnId);
						}
					}
				}

				// 发书
				for (String itemCode : itemCodes) {
					BdFeeItem item = itemMapper.selectItemInfoById(itemCode);
					// 0625版本修改
					if (FeeConstants.FEE_ITEM_TYPE_BOOK.equals(item.getItemType())) {
						if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) { // 成教
							sendService.sendBook(learnId, year, false, "1");
						} else {
							// 国开,如果学员状态为:已录取/注册学员/在读学员 则直接生成
							if (StudentConstants.STD_STAGE_ENROLLED.equals(stdStage)
									|| StudentConstants.STD_STAGE_REGISTER.equals(stdStage)
									|| StudentConstants.STD_STAGE_STUDYING.equals(stdStage)) {
								sendService.sendBook(learnId, year, false, "1");
							} else { // 三审变为已录取的时候生成
								sendService.sendBook(learnId, year, false, "0");
							}
						}

					}
				}
			}
		}

		if (StudentConstants.STD_STAGE_PURPOSE.equals(stdStage)
				&& StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
			for (String itemCode : itemCodes) {
				BdFeeItem item = itemMapper.selectItemInfoById(itemCode);
				if (FeeConstants.FEE_ITEM_TYPE_COACH.equals(item.getItemType())) {
					payMapper.updateStdStage(learnId, StudentConstants.STD_STAGE_HELPING);
					// sendService.sendBook(learnId, null, true);
					// 修改学业辅导费审核时间
					payMapper.updateTutionTime(learnId);
					break;
				}
			}
		}
	}

	/**
	 * 学业ID查询拥有优惠券
	 * 
	 * @param learnId
	 * @return
	 */
	public Object selectCoupon(String learnId) {
		Map<String, String> std = payMapper.selectStdUserIdByLearnId(learnId);
		String userId = null;
		if (StringUtil.hasValue(std.get("userId"))) {
			userId = std.get("userId");
		}
		List<BdCoupon> list = payMapper.selectAbleCouponByLearnId(std.get("stdId"), userId, learnId);
		return list;
	}

	public int selectCouponCount(String learnId) {
		Map<String, String> std = payMapper.selectStdUserIdByLearnId(learnId);
		String userId = null;
		if (StringUtil.hasValue(std.get("userId"))) {
			userId = std.get("userId");
		}
		return payMapper.selectAbleCouponByLearnId(std.get("stdId"), userId, learnId).size();
	}

	public Object selectCouponAmount(String couponId) {

		return payMapper.selectCouponAmount(couponId);
	}

	public List<BdQRCodePayableInfoResponse> selectStdPayFeeByCondition(String condition, String empId) {
		return payMapper.selectStdPayFeeByCondition(condition, empId);
	}

	public BdPayableInfoResponse selectPayableInfoByLearnIdForQRCode(String learnId, String subOrderStatus) {
		BdPayableInfoResponse response = payMapper.selectPayableInfoByLearnId(learnId, subOrderStatus);
		Map<String, String> account = accountApi.getAccount(null, response.getStdId(), null,
				FinanceConstants.ACC_TYPE_DEMURRAGE);

		String accAmount = "0.00";
		if (account != null) {
			accAmount = account.get("accAmount");
			if (StringUtil.isEmpty(accAmount)) {
				accAmount = "0.00";
			}
		}
		response.setAccAmount(accAmount);
		return response;
	}

	@SuppressWarnings("unchecked")
	public Object scanQRCodePay(BdPayInfo payInfo) {
		Body body = new Body();
		body.put("learnId", payInfo.getLearnId());
		body.put("coupons", payInfo.getCouponsStr());
		body.put("itemCodes", payInfo.getItemCodes());
		body.put("paymentType", payInfo.getPaymentType());
		body.put("years", payInfo.getYears());
		body.put("zmDeduction", payInfo.getZmDeduction());
		body.put("accDeduction", payInfo.getAccDeduction());
		body.put("tradeType", payInfo.getTradeType());
		body.put("empId", payInfo.getEmpId());
		return bdsPaymentApi.stdPayTuitionByQRCode(body);
	}

	public Object zmDetail(int start, int length, String stdId, String type) {
		String userId = payMapper.selectUserId(stdId);
		PageHelper.offsetPage(start, length);
		List<Map<String, String>> list = accountMapper.getStudentAccountSerial(type, userId, stdId);
		return new IPageInfo<Map<String, String>>((Page<Map<String, String>>) list);
	}

	public Object allSerials(int start, int length, String stdId) {
		String userId = payMapper.selectUserId(stdId);
		PageHelper.offsetPage(start, length);
		List<Map<String, String>> list = accountMapper.getStudentAllAccountSerial(userId, stdId);
		return new IPageInfo<Map<String, String>>((Page<Map<String, String>>) list);
	}

	public Object studentCoupon(String learnId) {
		Map<String, String> std = payMapper.selectStdUserIdByLearnId(learnId);
		String userId = null;
		if (StringUtil.hasValue(std.get("userId"))) {
			userId = std.get("userId");
		}
		List<BdCoupon> s = payMapper.selectAbleCouponByLearnId(std.get("stdId"), userId, learnId);
		// Object o = JSONArray.fromObject(s);
		return s;
	}

	/**
	 * 批量刷新
	 * 
	 * @param query
	 */
	public void batchAfreshStdOrder(BdPayableQuery query) {
		List<String> learnIds = payMapper.getStdLearndIdsByCond(query);
		if (null != learnIds && learnIds.size() > 0) {
			for (String learnId : learnIds) {
				log.debug("---------------=" + learnId);
				afreshStudentOrder(learnId, "B");
			}
		}
	}

	/**
	 * 手动刷新学员订单
	 */
	public void afreshStudentOrder(String learnId, String operType) {
		BdLearnInfo afreshLeanrInfo = payMapper.selectLearnInfoByLearnId(learnId);
		if (null != afreshLeanrInfo) {
			// 获取当前最新的收费标准
			String nowFeeId = studentModifyMapper.selectFeeStandard(afreshLeanrInfo.getPfsnId(),
					afreshLeanrInfo.getTaId(), afreshLeanrInfo.getScholarship());

			if (!StringUtil.hasValue(nowFeeId)) {
				throw new BusinessException("E000077"); // 无收费标准
			}
			log.debug("学业:" + learnId + "对应的新的收费标准为:" + nowFeeId);
			// 老收费标准ID
			String oldFeeId = studentModifyMapper.selectNowFeeId(learnId);
			if (!StringUtil.hasValue(nowFeeId)) {
				throw new BusinessException("E000089"); // 老收费标准
			}
			log.debug("学业:" + learnId + "对应的旧的收费标准为:" + oldFeeId);

			if (operType.equals("S")) { // 单个刷新操作
				if (nowFeeId.equals(oldFeeId)) {
					throw new BusinessException("E000113"); // 如果二者相同则表示,没有配置新的,或者新老一样,无需刷新
				}
				executeAfresh(afreshLeanrInfo, nowFeeId);
			} else if (operType.equals("B")) {
				if (!nowFeeId.equals(oldFeeId)) { // 批量操作,容错没有新收费标准的
					executeAfresh(afreshLeanrInfo, nowFeeId);
				}
			}
		}
	}

	@SuppressWarnings("unused")
	public void executeAfresh(BdLearnInfo afreshLeanrInfo, String nowFeeId) {
		// 设置转账对象
		Body body = new Body();
		// 如果有优惠政策
		String learnId = afreshLeanrInfo.getLearnId();
		String offerId = studentModifyMapper.selectOfferId(afreshLeanrInfo.getPfsnId(), afreshLeanrInfo.getTaId(),
				afreshLeanrInfo.getScholarship());
		log.debug("学业:" + learnId + "对应优惠政策:" + offerId);

		// 已缴金额
		BigDecimal paidAmount = BigDecimal.ZERO;

		BigDecimal delayAmount = BigDecimal.ZERO;

		String amount = studentModifyMapper.selectPaidAmountByLearnId(afreshLeanrInfo.getLearnId());
		if (StringUtil.hasValue(amount)) {
			paidAmount = AmountUtil.str2Amount(amount);
		}
		log.debug("学业:" + learnId + "在刷新新的收费标准前总缴费:" + paidAmount);

		studentModifyMapper.updateStdOrderStatusByLearnId(learnId);

		BdLearnInfo learnInfo = new BdLearnInfo();
		BaseUser user = SessionUtil.getUser();
		learnInfo.setUpdateUser(user.getRealName());
		learnInfo.setUpdateUserId(user.getUserId());

		learnInfo.setFeeId(nowFeeId);
		learnInfo.setOfferId(offerId);
		learnInfo.setLearnId(learnId);
		learnInfo.setUserId(afreshLeanrInfo.getUserId());
		learnInfo.setUnvsId(afreshLeanrInfo.getUnvsId());
		learnInfo.setPfsnId(afreshLeanrInfo.getPfsnId());
		learnInfo.setTaId(afreshLeanrInfo.getTaId());
		learnInfo.setRecruitType(afreshLeanrInfo.getRecruitType());
		learnInfo.setStdId(afreshLeanrInfo.getStdId());
		learnInfo.setStdStage("1"); // 全部按照意向学员刷新订单
		recruitService.initStudentOrder(learnInfo);

		BdStudentModify bdStudentModify = new BdStudentModify();
		bdStudentModify.setLearnId(learnId);
		bdStudentModify.setRecruitType(afreshLeanrInfo.getRecruitType());
		bdStudentModify.setFeeId(nowFeeId);
		bdStudentModify.setOfferId(offerId);
		bdStudentModify.setStdId(afreshLeanrInfo.getStdId());

		if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
			delayAmount = autoPayTheFees(paidAmount, bdStudentModify, delayAmount);
		}
		log.info("学业:" + learnId + "在自动交费后还剩滞留金:" + delayAmount);
		modifyService.updateBdLearnInfo(bdStudentModify);// 根据学业id修改学业信息
	}

	/**
	 * 手动刷新学员订单-自动缴费
	 * 
	 * @param paidAmount
	 * @param bdStudentModify
	 * @param delayAmount
	 * @return
	 */
	private BigDecimal autoPayTheFees(BigDecimal paidAmount, BdStudentModify bdStudentModify, BigDecimal delayAmount) {

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
				delayAmount = payableAmount;
				paidAmount = BigDecimal.ZERO;
			}

		} else if (StudentConstants.RECRUIT_TYPE_GK.equals(bdStudentModify.getRecruitType())
				&& paidAmount.compareTo(BigDecimal.ZERO) > 0) { // 国开 比对所有科目
			BigDecimal firstPayable = BigDecimal.ZERO;
			// 第一年
			if (null != response.getFirstPayInfos() && response.getFirstPayInfos().size() > 0) {

				for (BdStdPayInfoResponse res : response.getFirstPayInfos()) {
					firstPayable = BigDecimalUtil.add(firstPayable, AmountUtil.str2Amount(res.getPayable()));
				}

				if (paidAmount.compareTo(firstPayable) >= 0) { // 已缴金额大于应缴金额，自动缴费，插入流水
					paidAmount = BigDecimalUtil.substract(paidAmount, firstPayable);
					for (BdStdPayInfoResponse res : response.getFirstPayInfos()) { // 循环遍历加入支付科目
						itemCodes.add(res.getItemCode());
					}
					years.add(FinanceConstants.FEE_ITEM_YEAR_FIRST);
					delayAmount = paidAmount;
				} else {
					delayAmount = paidAmount;
					paidAmount = BigDecimal.ZERO;
				}
			}
			// 第二年
			if (null != response.getSecondPayInfos() && response.getSecondPayInfos().size() > 0) {
				if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal secondPayable = BigDecimal.ZERO;
					for (BdStdPayInfoResponse res : response.getSecondPayInfos()) {
						secondPayable = BigDecimalUtil.add(secondPayable, AmountUtil.str2Amount(res.getPayable()));
					}

					if (paidAmount.compareTo(secondPayable) >= 0) {
						paidAmount = BigDecimalUtil.substract(paidAmount, secondPayable);
						for (BdStdPayInfoResponse res : response.getSecondPayInfos()) { // 循环遍历加入支付科目
							itemCodes.add(res.getItemCode());
						}
						years.add(FinanceConstants.FEE_ITEM_YEAR_SECOND);
						delayAmount = paidAmount;
					} else {
						delayAmount = paidAmount;
						paidAmount = BigDecimal.ZERO;
					}
				}
			}
			// 第三年
			if (null != response.getThirdPayInfos() && response.getThirdPayInfos().size() > 0) {
				if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal thirdPayable = BigDecimal.ZERO;
					for (BdStdPayInfoResponse res : response.getThirdPayInfos()) {
						thirdPayable = BigDecimalUtil.add(thirdPayable, AmountUtil.str2Amount(res.getPayable()));
					}

					if (paidAmount.compareTo(thirdPayable) >= 0) {
						paidAmount = BigDecimalUtil.substract(paidAmount, thirdPayable);
						for (BdStdPayInfoResponse res : response.getThirdPayInfos()) { // 循环遍历加入支付科目
							itemCodes.add(res.getItemCode());
						}
						years.add(FinanceConstants.FEE_ITEM_YEAR_THIRD);
						delayAmount = paidAmount;
					} else {
						delayAmount = paidAmount;
						paidAmount = BigDecimal.ZERO;
					}
				}
			}
			// TODO 第四年
			// 其他
			if (null != response.getOtherPayInfos() && response.getOtherPayInfos().size() > 0) {
				if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal otherPayable = BigDecimal.ZERO;
					for (BdStdPayInfoResponse res : response.getOtherPayInfos()) {
						otherPayable = BigDecimalUtil.add(otherPayable, AmountUtil.str2Amount(res.getPayable()));
					}
					if (paidAmount.compareTo(otherPayable) >= 0) {
						paidAmount = BigDecimalUtil.substract(paidAmount, otherPayable);
						for (BdStdPayInfoResponse res : response.getOtherPayInfos()) { // 循环遍历加入支付科目
							itemCodes.add(res.getItemCode());
						}
					} else {
						delayAmount = paidAmount;
						paidAmount = BigDecimal.ZERO;
					}
				}
			}
		}
		if (null != itemCodes && itemCodes.size() > 0) {
			payInfo.setItemCodes(itemCodes.toArray(new String[itemCodes.size()]));
			payInfo.setLearnId(bdStudentModify.getLearnId());
			payInfo.setZmDeduction("0.00");
			payInfo.setPaidAmount(BigDecimalUtil.substract(payableAmount, delayAmount).toString());
			payInfo.setPaymentType(FinanceConstants.PAYMENT_TYPE_DELAY);
			payInfo.setAccDeduction("0.00");
			if (null != years && years.size() > 0) {
				payInfo.setYears(years.toArray(new String[years.size()]));
			}
			tuitionService.selfPayTuition(payInfo);
		}
		return delayAmount;
	}

	public Map<String, String> selectStdInfoByStdId(String stdId) {
		return payMapper.selectStdInfoByStdId(stdId);
	}

	/**
	 * 获取缴费总额
	 * 
	 * @param stdId
	 * @return
	 */
	public String getPaidSum(String stdId) {
		return payMapper.selectPaidSumByStdId(stdId);
	}

	/**
	 * 获取提现总额
	 * 
	 * @param stdId
	 * @return
	 */
	public String getWithdrawSum(String stdId) {
		return payMapper.selectWithdrawByStdId(stdId);
	}

	/**
	 * 获取余额
	 * 
	 * @param stdId
	 * @param accType
	 * @return
	 */
	public Object getAccount(String stdId, String accType) {
		AtsAccount account = new AtsAccount();
		account.setAccType(accType);
		account.setStdId(stdId);

		account = accountMapper.getAccount(account);

		String accAmount = "0.00";
		if (account != null) {
			accAmount = account.getAccAmount();
			if (StringUtil.isEmpty(accAmount)) {
				accAmount = "0.00";
			}
		}

		return accAmount;
	}
}
