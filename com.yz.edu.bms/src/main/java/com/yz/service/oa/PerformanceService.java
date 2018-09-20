package com.yz.service.oa;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.SessionDpInfo;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.report.ReportJdbcDao;
import com.yz.util.AmountUtil;
import com.yz.util.BigDecimalUtil;
import com.yz.util.StringUtil;

@Transactional
@Service
public class PerformanceService {

	@Autowired
	private YzSysConfig yzSysConfig;

	@Autowired
	private PerformanceMapper perMapper;

	@Autowired
	private OaSessionMapper sessionMapper;

	@Autowired
	private ReportJdbcDao reportJdbcDao;

	private BaseUser getUser(String empId) {
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

	public Object getMyPerformance(String empId) {

		Map<String, Object> result = new HashMap<String, Object>();

		List<HashMap<String, String>> performances = new LinkedList<HashMap<String, String>>();

		BaseUser user = null;
		if (StringUtil.hasValue(empId)) {
			user = getUser(empId);
		} else {
			user = SessionUtil.getUser();
		}
		boolean isXJ = false;

		if (null != user.getJtList() && user.getJtList().size() > 0) {
			if (user.getJtList().contains("XJ")) {
				isXJ = true;
			}
		}

		Date d2 = new Date();
		Date d1 = null;
		try {
			d1 = new SimpleDateFormat("yyyy-MM").parse("2017-11");// 定义起始日期
			// d2 = new SimpleDateFormat("yyyy-MM").parse("2018-10");// 定义起始日期
		} catch (ParseException e) {
			return null;
		} // 定义结束日期

		Calendar dd = Calendar.getInstance();// 定义日期实例

		dd.setTime(d1);// 设置日期起始时间

		// 剩余可报销金额
		BigDecimal allReply = BigDecimal.ZERO;
		List<String> stdStages = new ArrayList<>();
		stdStages.add("3");
		stdStages.add("4");
		stdStages.add("10");
		stdStages.add("5");
		stdStages.add("6");
		stdStages.add("7");
		stdStages.add("8");
		stdStages.add("12");
		Integer year = Integer.valueOf(yzSysConfig.getRecruitYear());
		List<String> grades = new ArrayList<String>();
		if (null != year) {
			grades = perMapper.selectGradeByYear(year.toString());
		}
		Map<String, Object> paramMap = Maps.newHashMap();
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
		// 获取不包括筑梦的数据
		List<Map> map = (List<Map>) this.reportJdbcDao.getRepResultList("findRecruitStudents", paramMap);
		// 筑梦数据
		List<Map> map2 = (List<Map>) this.reportJdbcDao.getRepResultList("findRecruitStudentsForZhuMeng", paramMap);
		while (dd.getTime().before(d2)) {// 判断是否到结束日期
			HashMap<String, String> per = new HashMap<String, String>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			String rMonth = dd.get(Calendar.MONTH) + 1 + "";
			String rYear = dd.get(Calendar.YEAR) + "";
			String month = sdf.format(dd.getTime());
			// 预计报销
			BigDecimal reply = BigDecimal.ZERO;
			// 已报销
			String replyed = "0.00";
			String recruitCount = "0";
			String recruitCount2 = "0";
			String outCount = "0";
			String outCount2 = "0";
			for (Map m : map) {
				if (m.containsKey("expenseFormat") && m.get("expenseFormat").equals(month)) {
					recruitCount = m.get("total").toString();
					outCount = m.get("recruit").toString();
				}
			}
			for (Map m : map2) {
				if (m.containsKey("expenseFormat") && m.get("expenseFormat").equals(month)) {
					recruitCount2 = m.get("total").toString();
					outCount2 = m.get("recruit").toString();
				}
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

			replyed = perMapper.selectReplayedByRecruit(rMonth, user.getEmpId(), rYear);

			per.put("month", month);
			per.put("recruitCount", StringUtil.hasValue(recruitCount) ? recruitCount : "0");
			per.put("outCount", StringUtil.hasValue(outCount) ? outCount : "0");
			per.put("reply", reply.toString());
			per.put("replyed", replyed.toString());

			// 计算总报销额
			allReply = BigDecimalUtil.add(allReply, BigDecimalUtil.substract(reply, AmountUtil.str2Amount(replyed)));

			dd.add(Calendar.MONTH, 1);// 进行当前日期月份加1

			performances.add(per);
		}

		result.put("performances", performances);
		result.put("allReply", allReply.toString());

		return result;
	}

	public Object getStudents(String month, int start, int length, String empId) {

		Integer year = Integer.valueOf(yzSysConfig.getRecruitYear());

		List<String> grades = new ArrayList<String>();
		if (null != year) {
			grades = perMapper.selectGradeByYear(year.toString());
		}

		String[] months = null;
		if ("2017-11".equals(month)) {
			months = new String[] { "2017-11", "2017-10", "2017-09" };
		} else {
			months = new String[] { month };
		}
		BaseUser user = null;
		if (StringUtil.hasValue(empId)) {
			user = getUser(empId);
		} else {
			user = SessionUtil.getUser();
		}
		PageHelper.offsetPage(start, length);
		List<Map<String, String>> stds = perMapper.selectStudentByMonth(months, user, grades);
		for (Map<String, String> map : stds) {
			map.put("deduct", BigDecimalUtil.multiply(map.get("audit"), map.get("recruitAmount")));
		}
		return new IPageInfo<Map<String, String>>((Page<Map<String, String>>) stds);
	}

	private String count(List<String> list) {
		String result = "0";
		if (null != list && list.size() > 0) {
			for (String s : list) {
				result = BigDecimalUtil.add(s, result);
			}
		}
		return result;
	}

	private List<SessionDpInfo> getDepartmentList(String empId) {
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

	public Object selectUnderEmpId(SelectQueryInfo queryInfo) {
		BaseUser user = SessionUtil.getUser();
		queryInfo.setsId(user.getEmpId());
		PageHelper.startPage(queryInfo.getPage(), queryInfo.getRows());
		return new IPageInfo((Page) perMapper.selectUnderEmpId(queryInfo));
	}

}
