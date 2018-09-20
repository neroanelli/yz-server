package com.yz.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.constant.FixConstant;
import com.yz.dao.RegNetWorkExamFrmMapper;
import com.yz.dao.ZhiMiSupplementMapper;
import com.yz.edu.cmd.UserAccountCmd;
import com.yz.edu.domain.engine.DomainExecEngine;
import com.yz.redis.RedisService;
import com.yz.report.ReportJdbcDao;
import com.yz.util.JsonUtil;

@Service(value = "zmSupplementService")
@SuppressWarnings("unchecked")
public class ZmSupplementService {

	@Autowired
	private ZhiMiSupplementMapper zhiMiSupplementMapper;
	@Autowired
	private DomainExecEngine domainExecEngine;
	private Logger logger = LoggerFactory.getLogger(ZmSupplementService.class);

	@Autowired
	private RegNetWorkExamFrmMapper regNetWorkExamFrmMapper;

	@Autowired
	private ReportJdbcDao reportJdbcDao;

	/**
	 * 智米补送
	 */
	public void AddZhiMi() {
		logger.info("开始智米补送");
		List<HashMap<String, String>> map = zhiMiSupplementMapper.selectZhiCountForDateBetween("2018-08-17 00:00:00",
				"2018-08-22 00:00:00");
		if (map.size() > 0) {
			map.forEach(v -> {
				List<UserAccountCmd> cmdInfos = new ArrayList<>();
				UserAccountCmd cmd = new UserAccountCmd();
				cmd.setAccountType(v.get("acc_type"));
				cmd.setAmount(new BigDecimal(v.get("amount")));
				cmd.setMappingId(v.get("mapping_id"));
				cmd.setRemark("退回参与京东兑换活动扣除的智米");
				cmd.setUserId(v.get("user_id"));
				cmd.setType("1");
				cmd.setCanCheckAccount(false);
				cmdInfos.add(cmd);
				this.logger.info("AddZhiMi.UserAccountCmd:{}", JsonUtil.object2String(cmd));
				domainExecEngine.executeCmds(cmdInfos, null);
			});
		}
		logger.info("-----------智米补送结束--------");
	}

	/**
	 * 2018-07-11~2018-08-17京东兑换智米扣除
	 */
	public void exchangeDeductionZhiMi() {
		logger.info("开始智米扣除");
		List<HashMap<String, String>> map = zhiMiSupplementMapper.selectexchangeDeductionZM();
		if (map.size() > 0) {
			map.forEach(v -> {
				List<UserAccountCmd> cmdInfos = new ArrayList<>();
				UserAccountCmd cmd = new UserAccountCmd();
				cmd.setAccountType(v.get("acc_type"));
				cmd.setAmount(new BigDecimal(v.get("trans_amount")));
				cmd.setMappingId(v.get("order_no"));
				cmd.setRemark("参与<" + v.get("sales_name") + ">活动的兑换扣除");
				cmd.setUserId(v.get("user_id"));
				cmd.setType("2");
				cmd.setCanCheckAccount(false);
				cmdInfos.add(cmd);
				this.logger.info("AddZhiMi.UserAccountCmd:{}", JsonUtil.object2String(cmd));
				domainExecEngine.executeCmds(cmdInfos, null);
			});
		}
		logger.info("-----------智米补送结束--------");
	}

	/**
	 * 批量网报注册
	 */
	public void pushDataToRedis() {
		logger.info("-----------开始push数据--------");
		// Map<String, Object> paramMap = Maps.newHashMap();
		// List<Map> regListInfo = (List<Map>)
		// this.reportJdbcDao.getRepResultList("findRecruitStudents", paramMap);
		List<Map<String, String>> regListInfo = regNetWorkExamFrmMapper.getStudentNetWorkInfoBatch();
		for (Map<String, String> m : regListInfo) {
			RedisService.getRedisService().lpush(FixConstant.REDIS_NET_WORK_EXAM, JsonUtil.object2String(m));
			logger.info("REDIS_NET_WORK_EXAM:{}", JsonUtil.object2String(m));
		}
		logger.info("-----------push数据结束--------");
	}

	/**
	 * 获取预报名号
	 */
	public void pushDataToRedisForParam() {
		logger.info("-----------开始pushParam数据--------");
		// Map<String, Object> paramMap = Maps.newHashMap();
		// List<Map> regListInfo = (List<Map>)
		// this.reportJdbcDao.getRepResultList("findRecruitStudents", paramMap);
		List<Map<String, String>> regListInfo = regNetWorkExamFrmMapper.getStudentNetWorkInfoBatchForParam();
		for (Map<String, String> m : regListInfo) {
			RedisService.getRedisService().lpush(FixConstant.REDIS_NET_WORK_EXAM_APPLYNO, JsonUtil.object2String(m));
			logger.info("REDIS_NET_WORK_EXAM:{}", JsonUtil.object2String(m));
		}
		logger.info("-----------pushParam数据结束--------");
	}

	/**
	 * 找回预报名号
	 */
	public void pushDataToRedisForfindParam() {
		logger.info("-----------开始pushParam数据--------");
		// Map<String, Object> paramMap = Maps.newHashMap();
		// List<Map> regListInfo = (List<Map>)
		// this.reportJdbcDao.getRepResultList("findRecruitStudents", paramMap);
		List<Map<String, String>> regListInfo = regNetWorkExamFrmMapper.getStudentNetWorkInfoBatchForFindParam();
		for (Map<String, String> m : regListInfo) {
			RedisService.getRedisService().lpush(FixConstant.REDIS_NET_WORK_EXAM_FIND_APPLYNO,
					JsonUtil.object2String(m));
			logger.info("REDIS_NET_WORK_EXAM:{}", JsonUtil.object2String(m));
		}
		logger.info("-----------pushParam数据结束--------");
	}

	/**
	 * 获取学历验证结果
	 */
	public void pushEducationCheck() {
		logger.info("-----------开始pushEducationCheck数据--------");
		// Map<String, Object> paramMap = Maps.newHashMap();
		// List<Map> regListInfo = (List<Map>)
		// this.reportJdbcDao.getRepResultList("findRecruitStudents", paramMap);
		List<Map<String, String>> regListInfo = regNetWorkExamFrmMapper.getStudentEducationCheck();
		for (Map<String, String> m : regListInfo) {
			RedisService.getRedisService().lpush(FixConstant.REDIS_NET_WORK_EXAM_EDUCATIONCHECK,
					JsonUtil.object2String(m));
			logger.info("REDIS_NET_WORK_EXAM_EDUCATIONCHECK:{}", JsonUtil.object2String(m));
		}
		logger.info("-----------pushEducationCheck数据结束--------");
	}

	/**
	 * 获取学历
	 */
	public void pushEducationGet() {
		logger.info("-----------开始pushEducationGet数据--------");
		// Map<String, Object> paramMap = Maps.newHashMap();
		// List<Map> regListInfo = (List<Map>)
		// this.reportJdbcDao.getRepResultList("findRecruitStudents", paramMap);
		List<Map<String, String>> regListInfo = regNetWorkExamFrmMapper.getStudentEducationGet();
		for (Map<String, String> m : regListInfo) {
			RedisService.getRedisService().lpush(FixConstant.REDIS_NET_WORK_EXAM_EDUCATIONGET,
					JsonUtil.object2String(m));
			logger.info("REDIS_NET_WORK_EXAM_EDUCATIONGET:{}", JsonUtil.object2String(m));
		}
		logger.info("-----------pushEducationGet数据结束--------");
	}

	/**
	 * 首页获取学历
	 */
	public void pushEducationForLook() {
		logger.info("-----------开始pushEducationForLook数据--------");
		List<Map<String, String>> regListInfo = regNetWorkExamFrmMapper.getStudentEducationForLook();
		for (Map<String, String> m : regListInfo) {
			RedisService.getRedisService().lpush(FixConstant.REDIS_NET_WORK_EXAM_EDUCATIONLOOK,
					JsonUtil.object2String(m));
			logger.info("REDIS_NET_WORK_EXAM_EDUCATIONGET:{}", JsonUtil.object2String(m));
		}
		logger.info("-----------pushEducationForLook数据结束--------");
	}

	/**
	 * 修改毕业证书编号和户口归属地
	 */
	public void pushEducationForHuKou() {
		logger.info("-----------开始pushEducationForHuKou数据--------");
		List<Map<String, String>> regListInfo = regNetWorkExamFrmMapper.getStudentEducationForUpdate();
		for (Map<String, String> m : regListInfo) {
			RedisService.getRedisService().lpush(FixConstant.REDIS_NET_WORK_EXAM_EDUCATIONUPDATE,
					JsonUtil.object2String(m));
			logger.info("REDIS_NET_WORK_EXAM_EDUCATIONUPDATE:{}", JsonUtil.object2String(m));
		}
		logger.info("-----------pushEducationForHuKou数据结束--------");
	}
	
	/**
	 * 批量push学员确认
	 */
	public void pushDataToSceneConfirm() {
		logger.info("-----------开始push数据--------");
//		Map<String, Object> paramMap = Maps.newHashMap();
//		List<Map> regListInfo = (List<Map>) this.reportJdbcDao.getRepResultList("findRecruitStudents", paramMap);
		List<Map<String, String>> regListInfo = regNetWorkExamFrmMapper.getSceneConfirmCollectByAlreadyReserved();
		for(Map<String,String> m:regListInfo){
			RedisService.getRedisService().lpush(FixConstant.REDIS_ALREADYRESERVD_DATA_QUEUE, JsonUtil.object2String(m));
			logger.info("REDIS_ALREADYRESERVD_DATA_QUEUE:{}",JsonUtil.object2String(m));
		}
		logger.info("-----------push数据结束--------");
	}
}
