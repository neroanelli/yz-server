package com.yz.service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yz.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.GsLotteryApi;
import com.yz.conf.YzSysConfig;
import com.yz.constants.GlobalConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.constants.UsConstants;
import com.yz.core.constants.UsRelationConstants;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.BdLearnAnnex;
import com.yz.model.BdStudentBaseInfo;
import com.yz.model.BdStudentOther;
import com.yz.model.SendSmsVo;
import com.yz.model.SessionInfo;
import com.yz.model.StudentHistory;
import com.yz.model.UsBaseInfo;
import com.yz.model.UserLotteryEvent;
import com.yz.model.student.BdCheckRecord;
import com.yz.model.student.BdLearnInfo;
import com.yz.model.student.BdStudentModify;
import com.yz.redis.RedisService;
import com.yz.session.AppSessionHolder;
import com.yz.task.YzTaskConstants;
import com.yz.trace.wrapper.TraceEventWrapper;
import com.yz.util.Assert;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
public class BdsStudentService {

	private static final Logger log = LoggerFactory.getLogger(BdsStudentService.class);

	@Autowired
	private StudentAllMapper stdAllMapper;

	@Autowired
	private UsCertService certService;
	
	@Autowired
	private YzSysConfig yzSysConfig;
	
	@Autowired
	private BdsStudentMapper bdsStudentMapper;
	
	@Autowired
	private UsBaseInfoMapper usBaseInfoMapper;
	
	@Autowired
	private BdStudentSendMapper studentSendMapper;
	
	@Autowired
	private BdStudentOutService studentOuntService;
	
	@Autowired
	private BdLearnAnnexMapper lAnnexMapper;

	@Autowired
	private SysProvinceCityDistrictMapper sysProvinceCityDistrictMapper;
	
	
	@Reference(version = "1.0")
	private GsLotteryApi lotteryApi;
	
	public void sendCoupon(String userId) {
		List<String> couponIds = stdAllMapper.selectAvailableRegistCouponIds();
		if (null != couponIds && couponIds.size() > 0) {
			stdAllMapper.insertStdCoupon(userId, couponIds);
		}
	}

	public Object getAuthCode(String idCard, String userId) {
		int valicodeLength = 6;
		BdStudentBaseInfo baseInfo = stdAllMapper.getStudentInfoByIdCard(idCard);
		log.info("-------------------------- 根据身份证号[" + idCard + "] -- userId[" + userId + "]");

		if (baseInfo == null) {
			throw new BusinessException("E60007");// 身份证填写错误，或者学员不存在
		}

		String mobile = baseInfo.getMobile();

		if (StringUtil.isEmpty(mobile)) {
			throw new BusinessException("E60006");/// 学员手机号为空，无法进行身份验证
		}

		if (StringUtil.hasValue(baseInfo.getUserId())) {
			log.info("身份证号为{}的学员已和用户{}建立了关联关系",idCard,baseInfo.getUserId());
			throw new BusinessException("E60004");// 该学员身份已被关联，无法重复关联
		}

		//SimpleCacheUtil simple = RemoteCacheUtilFactory.simple().setLiveTime(300);// 5分钟有效时间
		
		String valicode = StringUtil.randomNumber(valicodeLength);
		Map<String, String> content = new HashMap<String, String>();
		content.put("code", valicode);
		//boolean isSuccess = smsApi.sendMessage(content, GlobalConstants.TEMPLATE_VALI_CODE, mobile);

		SendSmsVo vo = new SendSmsVo();
		vo.setContent(content);
		vo.setMobiles(Collections.singletonList(mobile));
		vo.setTemplateId(GlobalConstants.TEMPLATE_VALI_CODE);
	    RedisService.getRedisService().set(idCard, valicode); 
	    RedisService.getRedisService().lpush(YzTaskConstants.YZ_SMS_SEND_TASK, JsonUtil.object2String(vo));
		return null;
	}

	
	/**
	 * 绑定身份证信息
	 * @param idCard
	 * @param authCode
	 * @param userId
	 * @return
	 */
	public Object authStudent(String idCard, String authCode, String userId) {
		String smsIsOn = yzSysConfig.getSmsSwitch();

		Object saveAuth = RedisService.getRedisService().get(idCard);
		RedisService.getRedisService().del(idCard);
		

		if (GlobalConstants.FALSE.equals(smsIsOn)) {
			saveAuth = "888888";
		}

		if (saveAuth == null) {
			throw new BusinessException("E60008");// 验证码超时，请重新获取
		}

		if (!saveAuth.equals(authCode)) {
			throw new BusinessException("E60009");// 验证码不正确
		}

		BdStudentBaseInfo baseInfo = stdAllMapper.getStudentInfoByIdCard(idCard);

		if (baseInfo == null) {
			throw new BusinessException("E60019");// 学员信息不存在
		}
		
		SessionInfo sessionInfo = AppSessionHolder.getSessionInfo(userId, AppSessionHolder.RPC_SESSION_OPERATOR);

		UsBaseInfo usBaseInfo = new UsBaseInfo();
		usBaseInfo.setUserId(userId);
		usBaseInfo.setStdId(baseInfo.getStdId());
		
		if (StringUtil.hasValue(baseInfo.getUserId())) {
			log.info("操作用户["+userId+"]要绑定的学员编号["+baseInfo.getStdId()+"]已和用户["+baseInfo.getUserId()+"]存在了关联关系");
			throw new BusinessException("E60011");
		}
		
		//更新绑定关系
		bdsStudentMapper.updateUserIdByStdId(userId,baseInfo.getStdId());
		
		usBaseInfoMapper.updateByPrimaryKeySelective(usBaseInfo);
		
		Map<String, String> recruitMap = stdAllMapper.getRecruitMap(baseInfo.getStdId());
		String stdId = baseInfo.getStdId();
		Assert.hasText(stdId, "学员ID不能为空");
		Assert.hasText(idCard, "身份证不能为空");
		Assert.hasText(userId, "用户ID不能为空");
		certService.createStdRelation(stdId, idCard, userId, recruitMap);
	
		sessionInfo.setStdId(stdId);
		sessionInfo.setRelation(UsRelationConstants.N_USER_TYPE_STUDENT);
		AppSessionHolder.setSessionInfo(userId, sessionInfo, AppSessionHolder.RPC_SESSION_OPERATOR);
		
		//赠送抽奖券
		UserLotteryEvent lotteryEvent = new UserLotteryEvent();
		lotteryEvent.setUserId(baseInfo.getUserId());
		lotteryEvent.setMobile(baseInfo.getMobile());
		lotteryEvent.setOperType(UsConstants.GIVE_WAY_BIND);
		RedisService.getRedisService().lpush(YzTaskConstants.YZ_USER_GIVE_LOTTERY_EVENT, JsonUtil.object2String( TraceEventWrapper.wrapper(lotteryEvent)));
		log.info("YZ_USER_GIVE_LOTTERY_EVENT_BIND:{}",JsonUtil.object2String(lotteryEvent));
		
		return null;
	}

	

	public List<Map<String, String>> getEnrollInfos(String stdId) {
		return bdsStudentMapper.getEnrollList(stdId);
	}

	public StudentHistory getHistoryInfo(String learnId) {
		return bdsStudentMapper.getHistoryInfo(learnId);
	}

	public Object completeEnroll(String learnId, String stdId, String sfzFront, String sfzBack, String education) {
		String newSfzFront = FileSrcUtil.createFileSrc(Type.STUDENT, stdId, sfzFront);
		String newSfzBack = FileSrcUtil.createFileSrc(Type.STUDENT, stdId, sfzBack);
		String newEducation = FileSrcUtil.createFileSrc(Type.STUDENT, stdId, education);

		FileUploadUtil.copyToDisplay(sfzFront, newSfzFront);
		FileUploadUtil.copyToDisplay(sfzBack, newSfzBack);
		FileUploadUtil.copyToDisplay(education, newEducation);

		bdsStudentMapper.updateAnnex(stdId, StudentConstants.ANNEX_TYPE_IDCARD_FRONT, newSfzFront);
		bdsStudentMapper.updateAnnex(stdId, StudentConstants.ANNEX_TYPE_IDCARD_BEHIND, newSfzBack);
		bdsStudentMapper.updateAnnex(stdId, StudentConstants.ANNEX_TYPE_EDUCATION, newEducation);

		bdsStudentMapper.updateAnnexStatus(stdId);

		bdsStudentMapper.testCompleted(learnId);

		return null;
	}

	public void updateHistory(StudentHistory history) {
		bdsStudentMapper.updateHistory(history);
	}
	
	

	public Map<String, String> getStudentByMobile(String mobile) {
		return bdsStudentMapper.getStudentByMobile(mobile);
	}

	public Map<String, String> getStudentModify(String learnId) {
		Map<String, String> map = bdsStudentMapper.getStudentModify(learnId);
		if (map == null) {
			return bdsStudentMapper.getStudentInfo(learnId);
		}
		return map;
	}

	public void addStudentModify(BdStudentModify studentModify) {
		// 圆梦计划入围
		if (bdsStudentMapper.countYMStudent(studentModify.getLearnId()) > 0) {
			throw new BusinessException("E60037");
		}
		bdsStudentMapper.addStudentModify(studentModify);
		List<Map<String, String>> checkWeights = null;
		checkWeights = bdsStudentMapper.getCheckWeight(TransferConstants.CHECK_TYPE_SCHOOLROLL_MODIFY_NEW);
		for (Map<String, String> map : checkWeights) {
			// 初始化审核记录
			BdCheckRecord checkRecord = new BdCheckRecord();
			checkRecord.setMappingId(studentModify.getModifyId());
			checkRecord.setCheckOrder(map.get("checkOrder"));
			checkRecord.setCheckType(map.get("checkType"));
			checkRecord.setJtId(map.get("jtId"));
			bdsStudentMapper.addBdCheckRecord(checkRecord);
		}
	}

	public void sendLotteryCoupon(String userId, String couponId) {
		List<String> couponIds = new ArrayList<String>();
		couponIds.add(couponId);
		stdAllMapper.insertStdCoupon(userId, couponIds);
	}
	
	
	/**
	 * 根据learnId得到学员需完善资料信息
	 * @param learnId
	 * @return
	 */
	public Map<String, Object> selectNeedCompleteStuInfo(String learnId) {
		HashMap<String, Object> mapResult=bdsStudentMapper.selectNeedCompleteStuInfo(learnId);
		return mapResult;
	}
	
	/**
	 * 完善学员资料
	 * @param baseInfo
	 * @param history
	 * @param recruitType
	 */
	@Transactional
	public void updateCompleteStuInfo(BdStudentBaseInfo baseInfo, StudentHistory history,List<BdLearnAnnex> annexInfos,String recruitType,String userName,String userId) {
		BdStudentBaseInfo oldbaseInfo =stdAllMapper.getStudentBaseInfoByLearnId(baseInfo.getLearnId());
		if(null!=baseInfo.getNowStreetName()&&baseInfo.getNowStreetName().indexOf("请选择")>=0) baseInfo.setNowStreetName("");
		String oldAddress= StringUtil.string2Unicode((oldbaseInfo.getNowProvinceName()==null?"":oldbaseInfo.getNowProvinceName())+(oldbaseInfo.getNowCityName()==null?"":oldbaseInfo.getNowCityName())+(oldbaseInfo.getNowDistrictName()==null?"":oldbaseInfo.getNowDistrictName())+(oldbaseInfo.getNowStreetName()==null?"":oldbaseInfo.getNowStreetName())+(oldbaseInfo.getAddress()==null?"":oldbaseInfo.getAddress()));
		String newAddress = StringUtil.string2Unicode(baseInfo.getNowProvinceName()+baseInfo.getNowCityName()+baseInfo.getNowDistrictName()+baseInfo.getNowStreetName()+baseInfo.getAddress());
		StringBuffer modifyText = new StringBuffer();
		//初始化必填项
		baseInfo.setStdId(oldbaseInfo.getStdId());
		baseInfo.setStdName(oldbaseInfo.getStdName());
		baseInfo.setIdType(oldbaseInfo.getIdType());
		baseInfo.setIdCard(oldbaseInfo.getIdCard());
		baseInfo.setSex(oldbaseInfo.getSex());
		baseInfo.setBirthday(oldbaseInfo.getBirthday());
		baseInfo.setMobile(oldbaseInfo.getMobile());
		if(!newAddress.equals(oldAddress)){
			baseInfo.setAddressEditTime("1"); //变动了地址
			updateBookSendAddress(baseInfo);
			modifyText.append("收教材地址：").append(StringUtil.unicode2String(oldAddress)).append(" ==> ").append(StringUtil.unicode2String(newAddress)).append("<br/>");
			
		}
		bdsStudentMapper.updateBaseInfo(baseInfo);
		
		if(StringUtil.hasValue(baseInfo.getMaritalStatus())) {
			BdStudentOther other=new BdStudentOther();
			other.setStdId(oldbaseInfo.getStdId());
			other.setMaritalStatus(baseInfo.getMaritalStatus());
			bdsStudentMapper.updateStudentOther(other);			
		}
		
		//更新学历信息
		StudentHistory historyResult = bdsStudentMapper.getHistoryInfo(baseInfo.getLearnId());
		if(historyResult!=null){
			bdsStudentMapper.updateHistory(history);
		}else{
			bdsStudentMapper.insertStudentHistory(history);
		}
		
		//上传附件
		if(annexInfos!=null&&annexInfos.size()>0) {
			annexInfos.stream().forEach(v -> {
				//如果附件没有变动则不更新
				BdLearnAnnex oldAnnex=lAnnexMapper.selectByPrimaryKey(v.getAnnexId());
				if(oldAnnex!=null) {
					String oldAnnexUrl=StringUtil.substringAfterLast(oldAnnex.getAnnexUrl(), "/");
					if(null!=oldAnnexUrl&&oldAnnexUrl.equals(v.getAnnexUrl())) {
						//更新完附件状态
						v.setAnnexUrl(oldAnnex.getAnnexUrl());
						if(oldAnnex.getAnnexStatus().equals(StudentConstants.ANNEX_STATUS_REJECT)) {
							v.setAnnexStatus(StudentConstants.ANNEX_STATUS_UNCHECK);
						}
						lAnnexMapper.updateByPrimaryKeySelective(v);
						return;
					}
				}

				if (StringUtil.hasValue(v.getAnnexUrl()) ) {
					v.setLearnId(baseInfo.getLearnId());	
					updateAnnexInfo(v,userName,userId,oldbaseInfo.getStdId());
					modifyText.append("附件:" + v.getAnnexName()+"更新了<br/>");
				}
				
			});
			//更新学员附件上传状态
			lAnnexMapper.updateLearnAnnexStatus(baseInfo.getLearnId(), StudentConstants.ANNEX_STATUS_UNCHECK);
			baseInfo.setMyAnnexStatus( StudentConstants.ANNEX_STATUS_UNCHECK);
		}

		checkDataCompleted(baseInfo, history, recruitType);
		checkTestCompleted(baseInfo, history);
		
		
		//如果教材地址有变更，填写变更记录
		BdStudentModify studentModify = new BdStudentModify();
		studentModify.setNation(baseInfo.getNation());
		studentModify.setNewNation(oldbaseInfo.getNation());
		studentModify.setLearnId(baseInfo.getLearnId());
		studentModify.setStdId(baseInfo.getStdId());
		studentModify.setExt1(modifyText.toString());
		studentModify.setIsComplete("1");
		studentModify.setModifyType(TransferConstants.MODIFY_TYPE_CHANGE_normalopt_5);
		if(StringUtil.hasValue(modifyText.toString())) {
			studentOuntService.addStudentModifyRecord(studentModify, userName, userId);
		}

	}
	
	/**
	 * 修改发书审核中、驳回状态的地址
	 * 
	 * @param baseInfo
	 */
	public void updateBookSendAddress(BdStudentBaseInfo baseInfo) {
		// 修改收货地址处理
		if (StringUtil.hasValue(baseInfo.getNowProvinceCode()) && StringUtil.hasValue(baseInfo.getNowCityCode())
				&& StringUtil.hasValue(baseInfo.getAddress())) {
			// 修改地址审核状态 驳回改为待审核
			studentSendMapper.updateSendAddressStatusByFD(baseInfo);
		}
	}
	
	/**
	 * 判断学员信息是否已完善
	 * 
	 * @param baseInfo
	 * @param history
	 */
	private void checkDataCompleted(BdStudentBaseInfo baseInfo, StudentHistory history, String recruitType) {
		if (baseInfo == null || history == null)
			return;

		String learnId = history.getLearnId();
		String isDataCompleted = GlobalConstants.FALSE;

		if (StudentDataCheckUtil.isComplete(baseInfo, recruitType)
				&& StudentDataCheckUtil.isComplete(history, recruitType)) {
			isDataCompleted = GlobalConstants.TRUE;
		}

		BdLearnInfo learnInfo = new BdLearnInfo();
		learnInfo.setIsDataCompleted(isDataCompleted);
		learnInfo.setLearnId(learnId);
		bdsStudentMapper.updateIsDataCompleted(learnInfo);
	}
	
	
	/**
	 * 判断学员信息是否完善
	 * 
	 * @param baseInfo
	 * @param history
	 */
	public void checkTestCompleted(BdStudentBaseInfo baseInfo, StudentHistory history) {
		if (baseInfo == null || history == null)
			return;
		String isTestCompleted = GlobalConstants.FALSE;
		if (StudentDataCheckUtil.isTestCompleted(history)
				&& (StudentConstants.ANNEX_STATUS_UNCHECK.equals(baseInfo.getMyAnnexStatus())
						|| StudentConstants.ANNEX_STATUS_ALLOW.equals(baseInfo.getMyAnnexStatus()))) {
			isTestCompleted = GlobalConstants.TRUE;
		}

		BdLearnInfo learnInfo = new BdLearnInfo();
		learnInfo.setIsTestCompleted(isTestCompleted);
		learnInfo.setLearnId(history.getLearnId());
		bdsStudentMapper.updateIsDataCompleted(learnInfo);
	}
	
	
	/**
	 * 更新附件信息
	 * 
	 * @param annexInfo
	 */
	@Transactional
	public String updateAnnexInfo(BdLearnAnnex annexInfo,String userName,String userId,String stdId) {
		String annexUrl = annexInfo.getAnnexUrl();
		String annexUrlNew="";
		try {
			//默认图片的处理
			annexUrlNew = FileSrcUtil.createFileSrc(Type.STUDENT, annexInfo.getLearnId(), annexUrl);
			if(StudentConstants.ANNEX_DIPLOMA_DEFAULT_1.equals(annexUrl)){

				FileUploadUtil.copyToDisplayDefault(annexUrl, annexUrlNew);

			}else {

				FileUploadUtil.copyToDisplay(annexUrl, annexUrlNew);
			}
			annexInfo.setAnnexUrl(annexUrlNew);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(StringUtil.isEmpty(annexInfo.getIsRequire())) {
			annexInfo.setIsRequire(GlobalConstants.TRUE);
		}
		if (GlobalConstants.TRUE.equals(annexInfo.getIsRequire())) {
			annexInfo.setAnnexStatus(StudentConstants.ANNEX_STATUS_UNCHECK);
		} else {
			annexInfo.setAnnexStatus(StudentConstants.ANNEX_STATUS_ALLOW);
		}
		annexInfo.setReason("");
		annexInfo.setUploadTime(new Date());
		annexInfo.setUploadUser(userName);
		annexInfo.setUploadUserId(userId);
		if(StringUtil.hasValue(annexInfo.getAnnexId()) && !"null".equals(annexInfo.getAnnexId())){
			lAnnexMapper.updateByPrimaryKeySelective(annexInfo);
		}else{
			annexInfo.setAnnexId(IDGenerator.generatorId());
			lAnnexMapper.insertSelective(annexInfo);
		}
		return annexUrlNew;
	}


	public HashMap<String, Object> getConfirmStuInfo(String learnId) {
		return bdsStudentMapper.getConfirmStuInfo(learnId);
	}

	/**
	 * 更新学员信息返回更新记录
	 * @param bds
	 * @return
	 */
	public void updateConfirmStuInfo(BdStudentBaseInfo baseInfo, BdStudentModify bdStudentModify) {
		BdStudentBaseInfo oldbaseInfo =stdAllMapper.getStudentBaseInfoByLearnId(baseInfo.getLearnId());
		if(null == oldbaseInfo){
			throw new BusinessException("E60004");
		}
		//记录更新的数据
		boolean isUpdate = false;
		if(StringUtil.hasValue(baseInfo.getStdName())){
			bdStudentModify.setStdName(oldbaseInfo.getStdName());
			bdStudentModify.setNewStdName(baseInfo.getStdName());
			isUpdate = true;
		}
		if(StringUtil.hasValue(baseInfo.getNation())){
			bdStudentModify.setNation(oldbaseInfo.getNation());
			bdStudentModify.setNewNation(baseInfo.getNation());
			isUpdate = true;
		}
		StringBuilder ext1 = new StringBuilder();
		//判断户口所在地是否更改
		if(StringUtil.hasValue(baseInfo.getRprProvinceCode())){
			ext1.append("户口所在地：");
			if (StringUtil.hasValue(oldbaseInfo.getRprProvinceCode())) {
				ext1.append(sysProvinceCityDistrictMapper.selectPCDByPCDCode(oldbaseInfo.getRprProvinceCode(),oldbaseInfo.getRprCityCode(),oldbaseInfo.getRprDistrictCode()));
			}else {
				ext1.append("无");
			}
			ext1.append(" ==> ");
			ext1.append(sysProvinceCityDistrictMapper.selectPCDByPCDCode(baseInfo.getRprProvinceCode(),baseInfo.getRprCityCode(),baseInfo.getRprDistrictCode()));
			ext1.append("<br/>");
			isUpdate = true;
		}
		if(StringUtil.hasValue(baseInfo.getRprType())){

			ext1.append(getModifyExt("户口类型：",oldbaseInfo.getRprType(),baseInfo.getRprType(),true,"rprType"));
			isUpdate = true;
		}
		if(isUpdate){
			//更新学员信息
			bdStudentModify.setExt1(ext1.toString());
			bdsStudentMapper.updateBaseInfo(baseInfo);
		}

	}


	public String updateConfirmHistory(StudentHistory history) {
		StringBuilder ext1 = new StringBuilder();

		//更新学历信息
		StudentHistory oldHistory = bdsStudentMapper.getHistoryInfo(history.getLearnId());
		boolean isUpdate = false;
		if(StringUtil.hasValue(history.getEdcsType())){
			ext1.append(getModifyExt("原学历类型：",null != oldHistory ? oldHistory.getEdcsType() : null,history.getEdcsType(),true,"edcsType"));
			isUpdate = true;
		}
		if(StringUtil.hasValue(history.getUnvsName())){
			ext1.append(getModifyExt("原毕业院校：",null != oldHistory ? oldHistory.getUnvsName() : null,history.getUnvsName(),false,null));
			isUpdate = true;
		}
		if(StringUtil.hasValue(history.getGraduateTime())){
			ext1.append(getModifyExt("原毕业时间：",null != oldHistory ? oldHistory.getGraduateTime() : null,history.getGraduateTime(),false,null));
			isUpdate = true;
		}
		if(StringUtil.hasValue(history.getProfession())){
			ext1.append(getModifyExt("原毕业专业：",null != oldHistory ? oldHistory.getProfession() : null,history.getProfession(),false,null));
			isUpdate = true;
		}
		if(StringUtil.hasValue(history.getDiploma())){
			ext1.append(getModifyExt("原毕业证编号：",null != oldHistory ? oldHistory.getDiploma() : null,history.getDiploma(),false,null));
			isUpdate = true;
		}
		if(oldHistory!=null){
			//记录更新
			if(isUpdate){
				bdsStudentMapper.updateHistory(history);
			}
		}else{

			bdsStudentMapper.insertStudentHistory(history);
		}

		if(!isUpdate){
			return null;
		}

		return ext1.toString();
	}

	/**
	 * 代码整改,获取修改的内容
	 * @param type     修改的类型
	 * @param oldNex   旧内容
	 * @param newNex	新内容
	 * @param isDict	是否是字典
	 * @return
	 */
	public StringBuilder getModifyExt(String type, String oldNex ,String newNex, boolean isDict, String dictType){
		StringBuilder result = new StringBuilder();
		result.append(type);
		if(isDict){
			result.append(StringUtil.hasValue(oldNex) ? bdsStudentMapper.getSysDictByPidAndV(dictType,oldNex) : "无");
			result.append(" ==> ");
			result.append( bdsStudentMapper.getSysDictByPidAndV(dictType,newNex));
		}else {
			result.append(StringUtil.hasValue(oldNex) ? oldNex : "无");
			result.append(" ==> ");
			result.append(newNex);
		}
		result.append("<br/>");
		return result;
	}


	public String updateAnnexInfos(List<BdLearnAnnex> annexInfos, String userName, String userId, String stdId,String learnId) {
		StringBuilder result = new StringBuilder();
		annexInfos.stream().forEach(ann ->{
			ann.setLearnId(learnId);
			updateAnnexInfo(ann, userName,userId,stdId);
			result.append("附件:" + ann.getAnnexName()+"更新了<br/>");
		});
		//更新学员附件上传状态
		lAnnexMapper.updateLearnAnnexStatus(learnId, StudentConstants.ANNEX_STATUS_UNCHECK);
		return result.toString();
	}

	public void updateIsDateCompleted(String learnId) {
		BdLearnInfo learnInfo = new BdLearnInfo();
		learnInfo.setIsDataCompleted(GlobalConstants.TRUE);
		learnInfo.setIsTestCompleted(GlobalConstants.TRUE);
		learnInfo.setLearnId(learnId);
		bdsStudentMapper.updateIsDataCompleted(learnInfo);
	}
}
