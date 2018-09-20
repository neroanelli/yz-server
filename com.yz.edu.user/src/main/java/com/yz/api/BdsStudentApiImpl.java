package com.yz.api;

import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.model.student.BdStudentModify;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.BdLearnAnnex;
import com.yz.model.BdStudentBaseInfo;
import com.yz.model.StudentHistory;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BdsStudentService;
import com.yz.util.JsonUtil;

import java.util.List;
import java.util.Map;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdsStudentApiImpl implements BdsStudentApi {

	@Autowired
	private BdsStudentService studentService;

	@Override
	public Object getAuthCode(Header header, Body body) throws IRpcException {

		String idCard = body.getString("idCard");
		String userId = header.getUserId();
		return studentService.getAuthCode(idCard,userId);
	}

	@Override
	public Object authStudent(Header header, Body body) throws IRpcException {
		String userId = header.getUserId();
		String idCard = body.getString("idCard");
		String authCode = body.getString("authCode");

		return studentService.authStudent(idCard, authCode, userId);
	}

	@Override
	public Object getEnrolls(Header header, Body body) throws IRpcException {
		String stdId = header.getStdId();
		return studentService.getEnrollInfos(stdId);
	}

	@Override
	public Object getHistory(Header header, Body body) throws IRpcException {
		String learnId = body.getString("learnId");
		return studentService.getHistoryInfo(learnId);
	}

	@Override
	public Object completeEnroll(Header header, Body body) throws IRpcException {
		String sfzFront = body.getString("sfzFront");
		String sfzBack = body.getString("sfzBack");
		String education = body.getString("education");
		String learnId = body.getString("learnId");
		String stdId = header.getStdId();
		return studentService.completeEnroll(learnId, stdId, sfzFront, sfzBack, education);
	}

	@Override
	public Object updateHistory(Header header, Body body) throws IRpcException {
		String unvsName = body.getString("unvsName");
		String profession = body.getString("profession");
		String graduateTime = body.getString("graduateTime");
		String diploma = body.getString("diploma");
		String learnId = body.getString("learnId");
		String edcsType = body.getString("edcsType");

		StudentHistory history = new StudentHistory();

		history.setUnvsName(unvsName);
		history.setProfession(profession);
		history.setGraduateTime(graduateTime);
		history.setDiploma(diploma);
		history.setLearnId(learnId);
		history.setEdcsType(edcsType);

		studentService.updateHistory(history);

		return null;
	}

	@Override
	public void sendCoupon(String userId) throws IRpcException {
		studentService.sendCoupon(userId);
	}

	@Override
	public Object getStudentByMobile(String mobile) throws IRpcException {
		return studentService.getStudentByMobile(mobile);
	}

	@Override
	public Map<String, String> getStudentModify(Header header, Body body) throws IRpcException {
		String learnId = body.getString("learnId");
		return studentService.getStudentModify(learnId);
	}

	@Override
	public void addStudentModify(Header header, Body body) throws IRpcException {
		BdStudentModify bdStudentModify = new BdStudentModify();
		bdStudentModify.setLearnId(body.getString("learnId"));
		bdStudentModify.setStdId(header.getStdId());
		bdStudentModify.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_FIRST);
		bdStudentModify.setModifyType(TransferConstants.MODIFY_TYPE_ROLL_NEW);
		bdStudentModify.setCheckType(TransferConstants.CHECK_TYPE_SCHOOLROLL_MODIFY_NEW);
		bdStudentModify.setStdName(body.getString("stdName"));
		bdStudentModify.setSex(body.getString("sex"));
		bdStudentModify.setNation(body.getString("nation"));
		bdStudentModify.setIdCard(body.getString("idCard"));
		bdStudentModify.setUnvsId(body.getString("unvsId"));
		bdStudentModify.setPfsnId(body.getString("pfsnId"));
		bdStudentModify.setNewPfsnId(body.getString("newPfsnId"));
		bdStudentModify.setExt1(body.getString("ext1"));
		bdStudentModify.setCreateUser(body.getString("stdName"));
		bdStudentModify.setUpdateUser(body.getString("stdName"));
		bdStudentModify.setCreateUserId(header.getUserId());
		bdStudentModify.setUpdateUserId(header.getUserId());
		studentService.addStudentModify(bdStudentModify);
	}

	@Override
	public void sendLotteryCoupon(String userId, String couponId) throws IRpcException {
		studentService.sendLotteryCoupon(userId,couponId);
	}

	@Override
	public Object getNeedCompleteStuInfo(Header header, Body body) throws IRpcException {
		return studentService.selectNeedCompleteStuInfo(body.getString("learnId"));
	}
	
	@Override
	public Object updateCompleteStuInfo(Header header, Body body) throws IRpcException {
		String learnId = body.getString("learnId");
		String stdId = body.getString("stdId"); 
		String recruitType = body.getString("recruitType"); 
		String userId = header.getUserId();
		String realName = body.getString("realName");
		
		StudentHistory history = new StudentHistory();
		history.setUnvsName(body.getString("unvsName"));
		history.setProfession(body.getString("profession"));
		history.setGraduateTime(body.getString("graduateTime"));
		history.setDiploma(body.getString("diploma"));
		history.setLearnId(learnId);
		history.setStdId(stdId);
		history.setEdcsType(body.getString("edcsType"));
		history.setIsOpenUnvs(body.getString("isOpenUnvs"));
		history.setStudyType(body.getString("studyType"));
		history.setMaterialCode(body.getString("materialCode"));
		history.setMaterialType(body.getString("materialType"));
		if(recruitType.equals(StudentConstants.RECRUIT_TYPE_CJ)) {
			history.setIsOpenUnvs("0");
		}
		BdStudentBaseInfo stuBaseInfo=new BdStudentBaseInfo();
		stuBaseInfo.setLearnId(learnId);
		stuBaseInfo.setStdId(stdId);
		stuBaseInfo.setNation(body.getString("nation"));
		stuBaseInfo.setPoliticalStatus(body.getString("politicalStatus"));
		stuBaseInfo.setRprType(body.getString("rprType"));
		stuBaseInfo.setNowProvinceCode(body.getString("nowProvinceCode"));
		stuBaseInfo.setNowCityCode(body.getString("nowCityCode"));
		stuBaseInfo.setNowDistrictCode(body.getString("nowDistrictCode"));
		stuBaseInfo.setNowStreetCode(body.getString("nowStreetCode"));
		stuBaseInfo.setNowProvinceName(body.getString("nowProvinceName"));
		stuBaseInfo.setNowCityName(body.getString("nowCityName"));
		stuBaseInfo.setNowDistrictName(body.getString("nowDistrictName"));
		stuBaseInfo.setNowStreetName(body.getString("nowStreetName"));
		stuBaseInfo.setAddress(body.getString("address"));
		stuBaseInfo.setJobType(body.getString("jobType"));
		stuBaseInfo.setJobStatus(body.getString("jobStatus"));
		stuBaseInfo.setMaritalStatus(body.getString("maritalStatus"));
		stuBaseInfo.setRprProvinceCode(body.getString("rprProvinceCode"));
		stuBaseInfo.setRprCityCode(body.getString("rprCityCode"));
		stuBaseInfo.setRprDistrictCode(body.getString("rprDistrictCode"));
		stuBaseInfo.setRprAddressCode(body.getString("rprAddressCode"));
		List<BdLearnAnnex> annexInfos = JsonUtil.str2List(body.getString("annexInfos"),BdLearnAnnex.class);
		studentService.updateCompleteStuInfo(stuBaseInfo,history,annexInfos,recruitType,realName,userId);
		return null;
	}
	
	
	/*@Override
	public boolean checkLotteryCondition(String mobile) throws IRpcException {
		return studentService.checkLotteryCondition(mobile);
	}*/
}
