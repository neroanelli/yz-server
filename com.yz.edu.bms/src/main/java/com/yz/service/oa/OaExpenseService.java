package com.yz.service.oa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.yz.conf.YzSysConfig;
import com.yz.constants.GlobalConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.oa.OaSessionMapper;
import com.yz.dao.oa.PerformanceMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.SessionDpInfo;
import com.yz.model.common.IPageInfo;
import com.yz.model.oa.OaMonthExpense;
import com.yz.model.oa.OaMonthExpenseQuery;
import com.yz.report.ReportJdbcDao;
import com.yz.util.AmountUtil;
import com.yz.util.BigDecimalUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class OaExpenseService {

	@Autowired
	private YzSysConfig yzSysConfig;
	@Autowired
	private PerformanceMapper perMapper;

	@Autowired
	private OaSessionMapper sessionMapper;
	@Autowired
	private ReportJdbcDao reportJdbcDao;

	public IPageInfo<OaMonthExpense> queryMonthExpenseByPage(int start, int length, OaMonthExpenseQuery query) {
		PageHelper.offsetPage(start, length);
		List<OaMonthExpense> list = perMapper.selectMonthExpenseByPage(query);
		Integer year = Integer.valueOf(yzSysConfig.getRecruitYear());

		Integer startYear = 0;
		Integer endYear = 0;

		String startTime = null;
		String endTime = null;

		List<String> grades = new ArrayList<String>();
		if (null != year) {
			startYear = year - 2;
			endYear = year - 1;
			// startTime = startYear.toString() + "-11-01 00:00:00";
			startTime = startYear.toString() + "-11-01";
			// TODO 2019 年度特殊要求，9月 10月 11月统一算11月，以后的照常从11月开始算
			if (2019 == year) {
				// startTime = startYear.toString() + "-9-01 00:00:00";
				startTime = startYear.toString() + "-9-01";
			}
			// endTime = endYear.toString() + "-10-31 23:59:59";
			endTime = endYear.toString() + "-10-31";
			grades = perMapper.selectGradeByYear(year.toString());

		}
		if (null != list && list.size() > 0 && null != grades && grades.size() > 0) {

			for (OaMonthExpense ex : list) {
				ex.setYear(year.toString());

				BaseUser user = new BaseUser();
				user.setEmpId(ex.getEmpId());
				List<SessionDpInfo> dpList = getDepartmentList(ex.getEmpId());// 校监级别权限
				if (dpList != null && !dpList.isEmpty()) {
					user.setMyDpList(dpList);
					user.setUserLevel(GlobalConstants.USER_LEVEL_DEPARTMENT);
				} else {
					user.setUserLevel(GlobalConstants.USER_LEVEL_NORMARL);
				}

				BigDecimal reply = BigDecimal.ZERO;
				// 招生标准人数计算
				Map<String, Object> paramMap = Maps.newHashMap();
				List<String> stdStages = new ArrayList<>();
				stdStages.add("3");
				stdStages.add("4");
				stdStages.add("10");
				stdStages.add("5");
				stdStages.add("6");
				stdStages.add("7");
				stdStages.add("8");
				stdStages.add("12");
				paramMap.put("stdStages", stdStages);
				paramMap.put("grades", grades);
				paramMap.put("empId", user.getEmpId());
				if (user.getUserLevel().equals("3")) {
					ArrayList<String> dpId = new ArrayList<>();
					for (SessionDpInfo dpInfo : user.getMyDpList()) {
						dpId.add(dpInfo.getDpId());
					}
					paramMap.put("dpId", dpId);
					paramMap.put("hasDp", "1");
				}
				paramMap.put("hasStartTime", "1");
				paramMap.put("hasEndTime", "1");
				paramMap.put("startTime", startTime);
				paramMap.put("endTime", endTime);
				// 获取不包括筑梦的数据
				List<Map> map = (List<Map>) this.reportJdbcDao.getRepResultList("findAllRecruitStudents", paramMap);
				// 筑梦数据
				List<Map> map2 = (List<Map>) this.reportJdbcDao.getRepResultList("findAllRecruitStudentsForZhuMe",
						paramMap);
				String recruitCount = "0";
				String recruitCount2 = "0";
				if (map.get(0).get("total") != null && StringUtil.isNotBlank(map.get(0).get("total").toString())) {
					recruitCount = map.get(0).get("total").toString();
				}
				if (map2.get(0).get("total") != null && StringUtil.isNotBlank(map2.get(0).get("total").toString())) {
					recruitCount2 = map2.get(0).get("total").toString();
				}
				// 退学标准学员计算
				String outCount = "0";
				// 筑梦数据
				String outCount2 = "0";
				if (map.get(0).get("recruit") != null && StringUtil.isNotBlank(map.get(0).get("recruit").toString())) {
					outCount = map.get(0).get("recruit").toString();
				}
				if (map2.get(0).get("recruit") != null
						&& StringUtil.isNotBlank(map2.get(0).get("recruit").toString())) {
					outCount2 = map2.get(0).get("recruit").toString();
				}
				recruitCount = BigDecimalUtil.addHalfUp(recruitCount, recruitCount2);
				outCount = BigDecimalUtil.addHalfUp(outCount, outCount2);
				if (null != ex.getJtIds() && ex.getJtIds().size() > 0) {
					if (ex.getJtIds().contains("XJ")) {
						reply = BigDecimalUtil.multiply(BigDecimalUtil.substract(AmountUtil.str2Amount(recruitCount),
								AmountUtil.str2Amount(outCount)), AmountUtil.str2Amount("60"));
					} else {
						reply = BigDecimalUtil.multiply(BigDecimalUtil.substract(AmountUtil.str2Amount(recruitCount),
								AmountUtil.str2Amount(outCount)), AmountUtil.str2Amount("200"));
					}
					reply = BigDecimalUtil.multiply(reply, AmountUtil.str2Amount("0.75"));
				}

				BigDecimal balance = BigDecimal.ZERO;
				String rendered = perMapper.selectRenderd(ex.getEmpId(), year.toString());

				balance = BigDecimalUtil.substract(reply, AmountUtil.str2Amount(rendered));

				ex.setEstimate(reply.toString());
				ex.setRendered(rendered);
				ex.setBalance(balance.toString());

			}

		} else {
			list.clear();
		}
		return new IPageInfo<OaMonthExpense>((Page<OaMonthExpense>) list);
	}

	public Object queryExpenseInfo(int start, int length, OaMonthExpense ex) {
		PageHelper.offsetPage(start, length);
		List<Map<String, String>> list = perMapper.selectExpenseInfo(ex);

		return new IPageInfo<Map<String, String>>((Page<Map<String, String>>) list);
	}

	public void addExpense(OaMonthExpense ex) {
		String amount = ex.getAmount();
		if (BigDecimal.ZERO.compareTo(AmountUtil.str2Amount(amount)) >= 0) {
			throw new BusinessException("E000101"); // 报销金额必须大于0
		}

		String balance = getBalance(ex);

		if (AmountUtil.str2Amount(balance).compareTo(AmountUtil.str2Amount(amount)) < 0) {
			throw new BusinessException("E000102"); // 报销金额不能大于余额
		}

		String expenseYear = ex.getYear();

		Integer year = Integer.valueOf(yzSysConfig.getRecruitYear());
		ex.setYear(year.toString());
		ex.setExpenseYear(expenseYear);
		ex.setEiId(IDGenerator.generatorId());
		perMapper.insertExpense(ex);
	}

	public BaseUser getUser(String empId) {
		BaseUser user = new BaseUser();
		user = new BaseUser();
		user.setEmpId(empId);
		user.setJtList(sessionMapper.getJtList(empId));
		List<SessionDpInfo> dpList = getDepartmentList(empId);// 校监级别权限
		if (dpList != null && !dpList.isEmpty()) {
			user.setMyDpList(dpList);
			user.setUserLevel(GlobalConstants.USER_LEVEL_DEPARTMENT);
		} else {
			user.setUserLevel(GlobalConstants.USER_LEVEL_NORMARL);
		}

		return user;
	}

	public String getBalance(OaMonthExpense ex) {
		BaseUser user = null;
		if (StringUtil.hasValue(ex.getEmpId())) {
			user = getUser(ex.getEmpId());
		} else {
			user = SessionUtil.getUser();
		}
		boolean isXJ = false;
		if (null != user.getJtList() && user.getJtList().size() > 0) {
			if (user.getJtList().contains("XJ")) {
				isXJ = true;
			}
		}
		// 预计报销
		BigDecimal reply = BigDecimal.ZERO;
		Integer year = Integer.valueOf(yzSysConfig.getRecruitYear());
		List<String> grades = perMapper.selectGradeByYear(year.toString());
		String balance = "0";
		if (null != ex) {
			// 招生标准人数计算
			Map<String, Object> paramMap = Maps.newHashMap();
			List<String> stdStages = new ArrayList<>();
			stdStages.add("3");
			stdStages.add("4");
			stdStages.add("5");
			stdStages.add("6");
			stdStages.add("7");
			stdStages.add("8");
			stdStages.add("10");
			stdStages.add("12");
			if (null != year) {
				grades = perMapper.selectGradeByYear(year.toString());
			}
			paramMap.put("stdStages", stdStages);
			paramMap.put("grades", grades);
			paramMap.put("empId", user.getEmpId());
			paramMap.put("hasStartTime", "0");
			paramMap.put("hasEndTime", "0");
			if (user.getUserLevel().equals("3")) {
				List<String> dpId = new ArrayList<>();
				for (SessionDpInfo dpInfo : user.getMyDpList()) {
					dpId.add(dpInfo.getDpId());
				}
				paramMap.put("dpId", dpId);
				paramMap.put("hasDp", "1");
			}
			// 获取不包括筑梦的数据
			List<Map> map = (List<Map>) this.reportJdbcDao.getRepResultList("findAllRecruitStudents", paramMap);
			// 筑梦数据
			List<Map> map2 = (List<Map>) this.reportJdbcDao.getRepResultList("findAllRecruitStudentsForZhuMe",
					paramMap);
			String recruitCount = "0";
			String recruitCount2 = "0";
			if (map.get(0).get("total") != null && StringUtil.isNotBlank(map.get(0).get("total").toString())) {
				recruitCount = map.get(0).get("total").toString();
			}
			if (map2.get(0).get("total") != null && StringUtil.isNotBlank(map2.get(0).get("total").toString())) {
				recruitCount2 = map2.get(0).get("total").toString();
			}
			// 退学标准学员计算
			String outCount = "0";
			// 筑梦数据
			String outCount2 = "0";
			if (map.get(0).get("recruit") != null && StringUtil.isNotBlank(map.get(0).get("recruit").toString())) {
				outCount = map.get(0).get("recruit").toString();
			}
			if (map2.get(0).get("recruit") != null && StringUtil.isNotBlank(map2.get(0).get("recruit").toString())) {
				outCount2 = map2.get(0).get("recruit").toString();
			}
			recruitCount = BigDecimalUtil.addHalfUp(recruitCount, recruitCount2);
			outCount = BigDecimalUtil.addHalfUp(outCount, outCount2);
			BigDecimal sub = BigDecimalUtil.substract(AmountUtil.str2Amount(recruitCount),
					AmountUtil.str2Amount(outCount));
			if (isXJ) {
				reply = BigDecimalUtil.multiply(sub, AmountUtil.str2Amount("60"));
			} else {
				reply = BigDecimalUtil.multiply(sub, AmountUtil.str2Amount("200"));
			}
			reply = BigDecimalUtil.multiply(reply, AmountUtil.str2Amount("0.75"));

			/*
			 * List<String> arrayMonth = new ArrayList<String>();
			 * arrayMonth.add(month);
			 */

			String replyed = perMapper.selectReplayed(null, ex.getEmpId(), year.toString());

			balance = BigDecimalUtil.substract(reply.toString(), replyed);
		}
		return balance;
	}

	public String queryReplyedAmount(OaMonthExpense ex) {
		return perMapper.selectReplyedAmount(ex);
	}

	public Object recruitList(String sName) {
		return perMapper.getRecruitList(sName);
	}

	public String count(List<String> list) {
		String result = "0";
		if (null != list && list.size() > 0) {
			for (String s : list) {
				result = BigDecimalUtil.add(s, result);
			}
		}
		return result;
	}

	public List<SessionDpInfo> getDepartmentList(String empId) {
		List<SessionDpInfo> dpList = sessionMapper.getDepartmentList(empId);
		if (dpList != null && dpList.size() > 0) {
			List<SessionDpInfo> subDpList = sessionMapper.getSubDpList(dpList);
			if (subDpList != null) {
				for (SessionDpInfo dpInfo : subDpList) {
					dpList.add(dpInfo);
				}
			}
		}
		return dpList;
	}

}
