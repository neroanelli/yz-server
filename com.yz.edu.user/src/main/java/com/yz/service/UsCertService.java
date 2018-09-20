package com.yz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.constants.UsConstants;
import com.yz.core.constants.UsRelationConstants;
import com.yz.dao.UsBaseInfoMapper;
import com.yz.dao.UsCertificateMapper;
import com.yz.dao.UsFollowLogMapper;
import com.yz.dao.UsFollowMapper;
import com.yz.exception.BusinessException;
import com.yz.model.SessionInfo;
import com.yz.model.UsBaseInfo;
import com.yz.model.UsCertificate;
import com.yz.model.UsFollow;
import com.yz.model.UsFollowLog;
import com.yz.model.communi.Body;
import com.yz.session.AppSessionHolder;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class UsCertService {

	private static final Logger logger = LoggerFactory.getLogger(UsCertService.class);

	@Autowired
	private UsCertificateMapper certMapper;

	@Autowired
	private AccountService accountService;

	@Autowired
	private UsBaseInfoMapper baseMapper;

	@Autowired
	private UsFollowLogMapper followLogMapper;

	@Autowired
	private UsFollowMapper followMapper;
	

	/**
	 * 绑定身份证
	 * 
	 * @param userId
	 *            用户ID
	 * @param name
	 *            用户姓名
	 * @param certNo
	 *            证件号
	 * @param certType
	 *            证件类型
	 * @param isEnroll
	 *            是否报名后绑定身份证
	 * @return
	 */
	public Object bindCert(String userId, String name, String certNo, String certType, boolean isEnroll) {
		// TODO 绑定后赠送智米
		int count = certMapper.countBy(userId, certType);

		UsCertificate cert = new UsCertificate();
		cert.setCertNo(certNo);
		cert.setCertType(certType);
		cert.setUserId(userId);
		cert.setName(name);

		if (!isEnroll) {
			if (count > 0) {
				throw new BusinessException("E30012");// 已绑定身份证 ，请勿重复绑定
			}
			certMapper.insertSelective(cert);
		} else {
			if (count > 0) {
				certMapper.updateByPrimaryKeySelective(cert);
			} else {
				certMapper.insertSelective(cert);
			}
		}

		if (StringUtil.hasValue(name)) {
			UsBaseInfo baseInfo = new UsBaseInfo();
			baseInfo.setRealName(name);
			baseInfo.setUserId(userId);
			// 更新用户姓名
			baseMapper.updateByPrimaryKeySelective(baseInfo);
		}

		return null;
	}

	public Map<String, String> getBindStatus(String userId, String certType) {
		UsCertificate cert = new UsCertificate();
		cert.setCertType(certType);
		cert.setUserId(userId);

		int count = certMapper.count(cert);
		boolean result = count > 0;
		Map<String, String> rm = new HashMap<String, String>();

		if (result) {
			rm.put("isBind", GlobalConstants.TRUE);
			cert = certMapper.getCertBy(userId, certType);
			rm.put("certId", cert.getCertId());
			rm.put("certNo", cert.getCertNo());
			rm.put("certType", cert.getCertType());
		} else {
			rm.put("isBind", GlobalConstants.FALSE);
		}

		return rm;
	}

	public Object isBindCert(String userId, String certType) {
		int count = certMapper.countBy(userId, certType);
		return count > 0;
	}

	@SuppressWarnings("unchecked")
	public void createStdRelation(String stdId, String idCard, String userId, Map<String, String> recruitMap) {
		// 建立跟进关系
		if (recruitMap != null && recruitMap.size() > 0) {
			logger.info("------------------------用户[" + userId + "] 绑定学员[" + stdId + ":" + idCard + "]身份，建立跟进关系"
					+ JsonUtil.object2String(recruitMap));

			UsFollow follow = followMapper.selectByPrimaryKey(userId);

			String recruit = recruitMap.get("recruit");
			String dpId = recruitMap.get("dpId");
			String campusId = recruitMap.get("campusId");
			String cm = recruitMap.get("campusManager");

			if (follow == null) {
				follow = new UsFollow();
				follow.setUserId(userId);
				follow.setCampusId(campusId);
				follow.setCampusManager(cm);
				follow.setDpId(dpId);
				follow.setEmpId(recruit);

				followMapper.insertSelective(follow);

			} else {
				follow.setUserId(userId);
				follow.setCampusId(campusId);
				follow.setCampusManager(cm);
				follow.setDpId(dpId);
				follow.setEmpId(recruit);

				followMapper.updateByPrimaryKeySelective(follow);
			}
		} else {
			logger.info("------------------------用户[" + userId + "]无招生老师跟进");
		}

		UsCertificate cert = certMapper.getCertBy(userId, UsConstants.CERT_TYPE_SFZ);

		if (cert == null) {
			cert = new UsCertificate();
			cert.setCertNo(idCard);
			cert.setUserId(userId);
			cert.setCertType(UsConstants.CERT_TYPE_SFZ);

			certMapper.insertSelective(cert);
			Body body = new Body();
			body.put("userId", userId);
			body.put("ruleCode", "bind.sfz");
			body.put("excDesc", "用户绑定身份证信息");
			body.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
			body.put("mappingId", cert.getCertId());
			body.put("ruleType", FinanceConstants.AWARD_RT_NORMAL);
			body.put("triggerUserId", userId);
			body.put("stdId", stdId);

			accountService.copyAndAward(body);
		} else {
			cert.setCertNo(idCard);
			cert.setCertType(UsConstants.CERT_TYPE_SFZ);

			certMapper.updateByPrimaryKeySelective(cert);
			accountService.copyZhimi(userId, stdId);
		}

		SessionInfo session = AppSessionHolder.getSessionInfo(userId, AppSessionHolder.RPC_SESSION_OPERATOR);

		if (session != null) {
			session.setStdId(stdId);
			session.addiUserType(UsConstants.I_USER_TYPE_STUDENT);
			session.setRelation(UsRelationConstants.N_USER_TYPE_STUDENT);
			AppSessionHolder.setSessionInfo(userId, session, AppSessionHolder.RPC_SESSION_OPERATOR);
		}

	}

	/**
	 * 缴费后刷新跟进关系
	 * @param body
	 * @return
	 */
	public void refreshFollow(Body body) {
		if (body == null) {
			logger.error("------------------------- 跟进关系为空啊！！！！！！！sob！！！！！！！！ ");
		}
		String userId = body.getString("userId");
		String mobile = body.getString("mobile");
		// 如果没有身份关系 根据手机号查询
		if (StringUtil.isEmpty(userId)) {
			List<Map<String, String>> userInfoList = baseMapper.getUserListInfoByMobile(mobile);
			if(null != userInfoList && userInfoList.size() ==1){
				userId = userInfoList.get(0).get("userId");
			}
			logger.info("通过手机号{}未找到或者找到多条用户信息",mobile);
		} 
		if(StringUtil.isNotBlank(userId)){
			String empId = body.getString("empId");
			String dpId = body.getString("dpId");
			String campusId = body.getString("campusId");

			UsFollow follow = followMapper.selectByPrimaryKey(userId);

			boolean hasFollow = false;
			if (StringUtil.hasValue(empId)) {
				hasFollow = true;
			}

			UsFollowLog log = new UsFollowLog();

			log.setUserId(userId);
			log.setDrType(UsConstants.DR_TYPE_FEE);
			log.setCampusId(campusId);
			log.setDpId(dpId);
			log.setEmpId(empId);
			log.setDrId(empId);

			if (follow == null) {
				if (hasFollow) {
					follow = new UsFollow();
					follow.setEmpId(empId);
					follow.setDpId(dpId);
					follow.setCampusId(campusId);
					follow.setUserId(userId);

					log.setRemark("学员缴费后建立跟进关系");
					followMapper.insertSelective(follow);
				}
			} else {
				if (hasFollow) {
					log.setOldCampusId(follow.getCampusId());
					log.setOldDpId(follow.getDpId());
					log.setOldEmpId(follow.getEmpId());
					log.setRemark("学员缴费后刷新跟进关系");

					follow.setEmpId(empId);
					follow.setDpId(dpId);
					follow.setCampusId(campusId);
					followMapper.updateByPrimaryKeySelective(follow);
				} else {
					log.setRemark("学员缴费后 因学员没有招生老师，则不做任何变更");
				}
			}

			// 插入跟进人变更记录
			followLogMapper.insertSelective(log);
		}else{
			logger.error("------------------------- 用户[" + userId + "]不存在");
		}

	}

	public void clearFollow(String empId) {
		List<Map<String, String>> list = followMapper.selectList(empId);
		logger.debug("------------------------------- empId :[ " + empId + "]先记录一下， 免的那群鬼又TM反悔 GGG : "
				+ JsonUtil.object2String(list));
		followMapper.clearFollow(empId);
	}

}
