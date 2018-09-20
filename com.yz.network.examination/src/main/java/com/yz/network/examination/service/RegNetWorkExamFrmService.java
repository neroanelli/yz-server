package com.yz.network.examination.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.conf.YzSysConfig;
import com.yz.constants.GlobalConstants;
import com.yz.model.SendSmsVo;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.croe.exception.ExamException;
import com.yz.network.examination.croe.util.RequestUtil;
import com.yz.network.examination.croe.util.SessionUtil;
import com.yz.network.examination.dao.RegNetWorkExamFrmMapper;
import com.yz.network.examination.form.BindMobileNetWorkExamForm;
import com.yz.network.examination.form.GetBaseInfoNetWorkExamForm;
import com.yz.network.examination.form.GetSmsCodeNetWorkForm;
import com.yz.network.examination.form.LoginNetWorkExamForm;
import com.yz.network.examination.form.RegNetWorkExamForm;
import com.yz.network.examination.format.YzNetWorkFormater;
import com.yz.network.examination.handler.NetWorkExaminationHandler.NetWorkExamHandlerResult;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 网报注册
 * 
 * @author lx
 * @date 2018年8月23日 下午4:52:20
 */
@Service(value = "regNetWorkExamFrmService")
public class RegNetWorkExamFrmService {

	private static final Logger log = LoggerFactory.getLogger(RegNetWorkExamFrmService.class);
	@Autowired
	private RegNetWorkExamFrmMapper regNetWorkExamFrmMapper;

	@Autowired
	private YzSysConfig yzSysConfig;

	@Autowired
	private NetWorkExamStarter examstart;

	/**
	 * 根据学业获取学员网报信息
	 * 
	 * @param learnId
	 * @return
	 */
	public Map<String, String> getStudentNetWorkInfo(String learnId) {
		return regNetWorkExamFrmMapper.getStudentNetWorkInfo(learnId);
	}

	/**
	 * 根据学业获取展示的学员网报信息
	 * 
	 * @param learnId
	 * @return
	 */
	public Map<String, String> getShowInfoFromStudentInfo(String learnId) {
		return regNetWorkExamFrmMapper.getShowInfoFromStudentInfo(learnId);
	}

	/**
	 * 插入学员预报名信息
	 * 
	 * @param stuMap
	 */
	public void insertStudentSceneRegister(Map<String, String> stuMap) {
		regNetWorkExamFrmMapper.insertStudentSceneRegister(stuMap);
	}

	/**
	 * 更改有效网报信息
	 * 
	 * @param learnId
	 */
	public void updateStudentRegisterStatus(String learnId) {
		regNetWorkExamFrmMapper.updateStudentRegisterStatus(learnId);
	}

	/**
	 * 获取学员注册成功次数
	 * 
	 * @param learnId
	 * @return
	 */
	public int studentSceneRegisterNum(String learnId) {
		return regNetWorkExamFrmMapper.studentSceneRegisterNum(learnId);
	}

	/**
	 * 更新学员现场确认的状态
	 * 
	 * @param learnId
	 * @return
	 */
	public int updateStudentWebRegisterStatus(String learnId) {
		return regNetWorkExamFrmMapper.updateStudentWebRegisterStatus(learnId);
	}

	/**
	 * 更新学员网报状态
	 * 
	 * @param status
	 * @param learnId
	 */
	public void updateNetWorkStatus(String status, String learnId) {
		regNetWorkExamFrmMapper.updateNetWorkStatus(status, learnId);
	}

	public void updateMobileBindStatus(String status, String learnId) {
		regNetWorkExamFrmMapper.updateMobileBindStatus(status, learnId);
	}

	/**
	 * 发送短信验证码
	 * 
	 * @param mobile
	 * @param authCode
	 * @return
	 */
	public Object valicode(String mobile) {
		int valicodeLength = 6;// 短信验证码长度
		String valicode = StringUtil.randomNumber(valicodeLength);
		Map<String, String> content = new HashMap<String, String>();
		content.put("code", valicode);
		log.debug("手机号:{},注册获取的验证码为:{}", mobile, valicode);
		String smsIsOn = yzSysConfig.getSmsSwitch();

		if (GlobalConstants.TRUE.equals(smsIsOn)) {
			SendSmsVo vo = new SendSmsVo();
			vo.setContent(content);
			vo.setMobiles(mobile);
			vo.setTemplateId(GlobalConstants.TEMPLATE_VALI_CODE);
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_SMS_SEND_TASK, JsonUtil.object2String(vo));
			RedisService.getRedisService().setex(mobile, valicode, 300);
		} else {
			RedisService.getRedisService().setex(mobile, "888888", 300);
		}
		return null;
	}

	public Object checkLoginCode(String mobile, String validCode) {
		String smsIsOn = yzSysConfig.getSmsSwitch();
		Object saveValicode = null;
		if (GlobalConstants.TRUE.equals(smsIsOn)) {
			saveValicode = RedisService.getRedisService().get(mobile);
		} else {
			saveValicode = "888888";
		}
		if (saveValicode == null) {
			throw new ExamException("NETE000032", "短信验证码超时，请重新获取！");
		}
		if (!saveValicode.equals(validCode)) {
			throw new ExamException("NETE000032", "短信验证码错误，请重新输入！");
		}

		return "SUCCESS";
	}

	/**
	 * 登陆
	 * 
	 * @return
	 */
	public Object loginNetWork(String idCard, String mobile) {

		Map<String, String> netInfo = regNetWorkExamFrmMapper.getStudentNetWorkInfoByIdCard(idCard);
		if (netInfo == null) {
			throw new ExamException("NETE000034", "学员还未进行网报注册，请联系招生老师！");
		}
		Map<String, String> regInfo = regNetWorkExamFrmMapper.getStudentNetWorkInfoByIdCardAndMobile(idCard, mobile);
		if (regInfo == null) {
			throw new ExamException("NETE000034", "身份证或手机号不匹配，请进入公众号远智学堂自行修改手机号或联系招生老师！");
		}
		if (String.valueOf(regInfo.get("status")).equals("0")) {
			throw new ExamException("NETE000034", "学员还未进行网报注册，请联系招生老师！");
		}
		if (String.valueOf(regInfo.get("status")).equals("5")) {
			throw new ExamException("NETE000034", "该学员已经报名确认成功，完成所有操作！");
		}
		SessionUtil.setKey(regInfo.get("learn_id"));// 存入用户唯一登录标识
		RedisService.getRedisService().del(regInfo.get("learn_id")); // 将cookie放入Redis中存储
		return regInfo.get("learn_id");
	}

	/**
	 * 得到网报信息
	 * 
	 * @return
	 */
	public Map<String, String> getNetWorkInfo(String learnId) {
		// 判断是否登陆
		String sessionId = RequestUtil.getSessionId();
		String key = SessionUtil.getKey(learnId);
		if (key == null) {
			throw new ExamException("NETE000034", "登录超时或尚未登录");// 登录超时或尚未登录
		}
		if (!sessionId.equals(key)) {
			throw new ExamException("NETE000035", "异地登录，被挤下线");// 异地登录，被挤下线
		}
		Map<String, String> regInfo = regNetWorkExamFrmMapper.getShowInfoFromStudentInfo(learnId);
		if (regInfo == null) {
			throw new ExamException("NETE000034", "身份证或手机号不匹配，请进入公众号远智学堂自行修改手机号或联系招生老师！");
		}
		Map<String, String> unvsInfo = regNetWorkExamFrmMapper.getUnvsInfoByLearnId(learnId);
		regInfo.put("unvs_name", unvsInfo.get("unvs_name"));
		regInfo.put("pfsn_name", unvsInfo.get("pfsn_name"));
		regInfo.put("pfsn_level", unvsInfo.get("pfsn_level"));
		SessionUtil.freshTime(sessionId, learnId);// 更新session生存时间
		return regInfo;
	}

	/**
	 * 发送网报验证码
	 * 
	 * @return
	 */
	public Object sendNetWorkCode(String learnId, String mobile) {
		GetSmsCodeNetWorkForm smsCodeForm = new GetSmsCodeNetWorkForm(learnId);
		smsCodeForm.setNeedLogin(true);
		smsCodeForm.addParam("lxsj", mobile);
		smsCodeForm.addParam("yzbj", "1");
		examstart.start(smsCodeForm);
		NetWorkExamHandlerResult handresult = smsCodeForm.getValue();
		return handresult.getResult();
	}

	/**
	 * 绑定手机号
	 * 
	 * @param mobile
	 * @param valicode
	 */
	public Object bindMoble(String learnId, String mobile, String valicode) {
		BindMobileNetWorkExamForm bindForm = new BindMobileNetWorkExamForm(learnId);
		bindForm.addParam("lxsj", mobile);
		bindForm.addParam("sjyzm", valicode);
		bindForm.addParam("yzbj", "1");

		examstart.start(bindForm);
		NetWorkExamHandlerResult handresult = bindForm.getValue();
		SessionUtil.freshTime(RequestUtil.getSessionId(), learnId);// 更新session生存时间
		return handresult.getResult();
	}

	/**
	 * 更新学员确认状态
	 * 
	 * @param examPayStatus
	 * @param picCollectStatus
	 * @param mobileBindStatus
	 * @param learnId
	 */
	public void updateSceneConfirmStatus(int examPayStatus, int picCollectStatus, int mobileBindStatus,
			String learnId) {
		regNetWorkExamFrmMapper.updateSceneConfirmStatus(examPayStatus, picCollectStatus, mobileBindStatus, learnId);
	}

	/**
	 * 更新预报名表打印内容
	 * 
	 * @param printHtml
	 * @param learnId
	 * @return
	 */
	public void updateScenePrintHtml(String printHtml, int printStatus, String learnId) {
		regNetWorkExamFrmMapper.updateScenePrintHtml(printHtml, printStatus, learnId);
	}

	/**
	 * 单个学员网报注册
	 */
	public void submitRegNetWorkInfo(String learnId) {
		RegNetWorkExamForm regForm = new RegNetWorkExamForm(learnId);
		Map<String, String> workInfo = getStudentNetWorkInfo(learnId);
		regForm.addParam(workInfo);
		regForm.addParam("pwd",
				NetWorkExamConstants.NETTO_PWD_PREFIX + YzNetWorkFormater.pwd.formater(workInfo.get("pwd"), null));
		if (workInfo.get("zsbpc1bkyx1").equals("11540")) { // 广金特殊处理
			regForm.addParam("txdz", "036" + workInfo.get("txdz") + NetWorkExamConstants.NETTO_ADDRESS_SUFFIX);
		} else {
			regForm.addParam("txdz", workInfo.get("txdz") + NetWorkExamConstants.NETTO_ADDRESS_SUFFIX);
		}
		examstart.start(regForm);
	}

	/**
	 * 模拟登录
	 * 
	 * @param learnId
	 */
	public void loginNetWebByLearnId(String learnId) {
		Map<String, String> regInfo = regNetWorkExamFrmMapper.getStudentSceneRegisterByLearnId(learnId);
		if (regInfo == null) {
			throw new ExamException("NETE000034", "该学员还没网报注册成功，请先网报注册！");
		}
		LoginNetWorkExamForm loginNetWorkExamForm = new LoginNetWorkExamForm(learnId);
		loginNetWorkExamForm.addParam("dlfs", "1");
		loginNetWorkExamForm.addParam("id", regInfo.get("username"));
		loginNetWorkExamForm.addParam("pwd", regInfo.get("password"));
		loginNetWorkExamForm.addValidCode();
		examstart.start(loginNetWorkExamForm);
	}

	/**
	 * 根据learnId得到预报名号
	 * 
	 * @param learnId
	 * @return
	 */
	public Map<String, String> getStudentSceneRegisterByLearnId(String learnId) {
		Map<String, String> regInfo = regNetWorkExamFrmMapper.getStudentSceneRegisterByLearnId(learnId);
		return regInfo;
	}

	/**
	 * 根据learnId得到预报名号
	 * 
	 * @param learnId
	 * @return
	 */
	public String getNetReportDataStutus(String learnId) {
		String status = regNetWorkExamFrmMapper.getNetReportDataStutus(learnId);
		return status;
	}

	/**
	 * 更改注册成功状态
	 * 
	 * @param learnId
	 * @param status
	 */
	public void updateReportStatus(String status, String learnId) {
		regNetWorkExamFrmMapper.updateReportStatus(status, learnId);
	}

	/**
	 * 是否网报成功
	 * 
	 * @param learnId
	 */
	public void updateRegIfSuccess(String learnId) {
		regNetWorkExamFrmMapper.updateRegIfSuccess(learnId);
	}

	/**
	 * 是否更新成功
	 * 
	 * @param learnId
	 */
	public void insertSuccess(String learnId, String userName, String status, String remark) {
		regNetWorkExamFrmMapper.insertSuccess(learnId, userName, status, remark);
	}

	/**
	 * 绑定手机号
	 * 
	 * @param mobile
	 * @param valicode
	 */
	public Object getBaseInfoStatus(String learnId) {
		GetBaseInfoNetWorkExamForm baseInfoNetWorkExamForm = new GetBaseInfoNetWorkExamForm(learnId);
		baseInfoNetWorkExamForm.setNeedLogin(true);
		examstart.start(baseInfoNetWorkExamForm);
		NetWorkExamHandlerResult handresult = baseInfoNetWorkExamForm.getValue();
		return handresult.getResult();
	}

	/**
	 * 更新图片采集状态
	 * 
	 * @param learnId
	 * @param status
	 */
	public void updatePicCollectStatus(int status, String learnId) {
		regNetWorkExamFrmMapper.updatePicCollectStatus(status, learnId);
	}

	/**
	 * 获取学业的预报名号
	 * 
	 * @param learnId
	 * @return
	 */
	public List<String> getStudentApplyNoByLeanrId(String learnId) {
		return regNetWorkExamFrmMapper.getStudentApplyNoByLeanrId(learnId);
	}
	
	/**
	 * 更新学员现场确认状态和考生号
	 * @param learnId
	 * @param examNo
	 * @return
	 */
	public int updateStudentSceneConfirmStatusAndExamNo(String learnId,String examNo) {
		return regNetWorkExamFrmMapper.updateStudentSceneConfirmStatusAndExamNo(learnId,examNo);
		
	}
}
