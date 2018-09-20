package com.yz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.constants.FinanceConstants;
import com.yz.dao.BdsPaymentMapper;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.AtsAccount;
import com.yz.model.coupon.BdCoupon;
import com.yz.model.payment.BdPayableInfoResponse;
import com.yz.util.AmountToCNUtil;
import com.yz.util.AmountUtil;
import com.yz.util.Assert;
import com.yz.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class BdsPaymentService {

	@Autowired
	private BdsPaymentMapper payMapper;

	@Autowired
	private AccountService accountService;

	@Autowired
	private GwWechatService wechatService;
	
	/**
	 * 学员已缴详情
	 * 
	 * @param learnId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Object selectStudentSerial(String learnId, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize, false);
		List<HashMap<String, Object>> list = payMapper.selectPayDetailByLearnId(learnId);
		for (HashMap<String, Object> s : list) {
			String cnAmount = AmountToCNUtil.number2CNMontrayUnit(AmountUtil.str2Amount(s.get("amount").toString()));
			s.put("chnAmount", cnAmount);
		}
		return list;
	}

	/**
	 * 我的优惠券
	 * 
	 * @param learnId
	 * @return
	 */
	public Object selectCoupon(String userId) {
		return JSONArray.fromObject(payMapper.selectCouponByLearnId(userId));
	}

	/**
	 * 任务录取页---待缴费页面数据
	 * 
	 * @param learnId
	 * @param userId
	 * @param subOrderStatus
	 * @return
	 */
	public Object selectEnrollPayableInfoByLearnId(String learnId, String userId) {
		BdPayableInfoResponse response = payMapper.selectStuPayableInfoResultMap(learnId,
				FinanceConstants.ORDER_STATUS_UNPAID);
		// 确定缴费时间
		// 已入围
		if (response != null && response.getInclusionStatus() != null && response.getInclusionStatus().equals("2")) {
			response.setPayFeeTime("2017年12月*日至2017年12月*日");
		} else if (response != null && response.getInclusionStatus() != null
				&& response.getInclusionStatus().equals("3")) {// 非入围
			response.setPayFeeTime("2017年12月*日至2017年12月*日");
		} else {
			if (response.getPfsnLevel() != null && response.getPfsnLevel().equals("1")) {// 本科
				response.setPayFeeTime("2017年12月*日至2017年12月*日");
			} else {
				response.setPayFeeTime("2017年12月*日至2017年12月*日");
			}
		}
		return JSONObject.fromObject(response);
	}

	/**
	 * 缴费页面数据
	 * 
	 * @param learnId
	 * @param userId
	 * @param subOrderStatus
	 * @return
	 */
	public Object selectPayableInfoByLearnId(String learnId, String userId) {
		BdPayableInfoResponse response = payMapper.selectPayableInfoByLearnId(learnId,
				FinanceConstants.ORDER_STATUS_UNPAID);
		// 2017-10-25 有现金 变为滞留
		// Map<String, String> account = atsApi.getAccount(userId, null,
		// null,FinanceConstants.ACC_TYPE_DEMURRAGE);
		Assert.isTrue(StringUtil.hasValue(userId), "用户ID不能为空");

		AtsAccount acc = new AtsAccount();
		acc.setStdId(null);
		acc.setUserId(userId);
		acc.setEmpId(null);
		acc.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
		Map<String, String> account = accountService.getAccount(acc);

		String accAmount = "0.00";
		if (account != null) {
			accAmount = account.get("accAmount");
			if (StringUtil.isEmpty(accAmount)) {
				accAmount = "0.00";
			}
		}


		AtsAccount zmacc = new AtsAccount();
		zmacc.setStdId(null);
		zmacc.setUserId(userId);
		zmacc.setEmpId(null);
		zmacc.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		Map<String, String> zmAccount = accountService.getAccount(zmacc);

		String zmAmount = "0";
		if (zmAccount != null) {
			zmAmount = zmAccount.get("accAmount");
			if (StringUtil.isEmpty(zmAmount)) {
				zmAmount = "0";
			} else {
				double x = Double.parseDouble(zmAmount);
				zmAmount = String.valueOf((int) x);
			}
		}
		response.setZmAmount(zmAmount);
		response.setAccAmount(accAmount);
		return JSONObject.fromObject(response);
	}

	/**
	 * 查询当前可用优惠券
	 * 
	 * @param learnId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Object selectAbleCoupon(String learnId) {
		Map<String, String> std = payMapper.selectStdUserIdByLearnId(learnId);
		String userId = null;
		if (StringUtil.hasValue(std.get("user_id"))) {
			userId = std.get("user_id");
		}
		List<BdCoupon> s = payMapper.selectAbleCouponByLearnId(std.get("stdId"), userId, learnId);
		return s;
	}
	/**
	 * 用户
	 * @param stdId
	 * @return
	 */
	public Object selectWithdrawSerial(String stdId) {

		return payMapper.selectWithdrawSerial(stdId);
	}

	/**
	 * 获取微信jsapi签名
	 * 
	 * @param url
	 * @return
	 */
	public Map<String, String> jsapiSign(String url) {
		return wechatService.jsapiSign(url);
	}
}
