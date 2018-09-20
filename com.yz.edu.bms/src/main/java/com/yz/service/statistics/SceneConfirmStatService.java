package com.yz.service.statistics;

import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.core.util.StudentStatUtil;
import com.yz.dao.oa.OaEmployeeMapper;
import com.yz.dao.statistics.SceneConfirmStatMapper;
import com.yz.dao.statistics.SendBookStatMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.SessionDpInfo;
import com.yz.model.common.IPageInfo;
import com.yz.model.oa.OaEmployeeJobInfo;
import com.yz.model.statistics.SceneConfirmStatInfo;
import com.yz.model.statistics.SceneConfirmStatQuery;
import com.yz.model.statistics.SendBookStatInfo;
import com.yz.model.statistics.SendBookStatQuery;
import com.yz.util.ExcelUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SceneConfirmStatService
{
	private static Logger log = LoggerFactory.getLogger(SceneConfirmStatService.class);

	@Autowired
	private SceneConfirmStatMapper sceneConfirmStatMapper;

	@Autowired
	private OaEmployeeMapper oaEmployeeMapper;

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IPageInfo sceneConfirmStatList(int start, int length,SceneConfirmStatQuery statQuery)
	{
		BaseUser user = getUser();
		PageHelper.offsetPage(start, length).setRmGroup(false);
		List<SceneConfirmStatInfo> confirmStatInfos = sceneConfirmStatMapper.sceneConfirmStatList(statQuery,user);
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		for(SceneConfirmStatInfo sceneConfirmStatInfo:confirmStatInfos){
			int count = Integer.parseInt(sceneConfirmStatInfo.getCount());
			sceneConfirmStatInfo.setExamInfoConfirmRate(numberFormat.format(Double.parseDouble(sceneConfirmStatInfo.getExamInfoConfirmRate())/count*100)+"%["+sceneConfirmStatInfo.getExamInfoConfirmRate()+"]");
			sceneConfirmStatInfo.setPlaceConfirmRate(numberFormat.format(Double.parseDouble(sceneConfirmStatInfo.getPlaceConfirmRate())/count*100)+"%["+sceneConfirmStatInfo.getPlaceConfirmRate()+"]");
			sceneConfirmStatInfo.setWebRegisterRate(numberFormat.format(Double.parseDouble(sceneConfirmStatInfo.getWebRegisterRate())/count*100)+"%["+sceneConfirmStatInfo.getWebRegisterRate()+"]");
			sceneConfirmStatInfo.setExamPayRate(numberFormat.format(Double.parseDouble(sceneConfirmStatInfo.getExamPayRate())/count*100)+"%["+sceneConfirmStatInfo.getExamPayRate()+"]");
			sceneConfirmStatInfo.setSignRate(numberFormat.format(Double.parseDouble(sceneConfirmStatInfo.getSignRate())/count*100)+"%["+sceneConfirmStatInfo.getSignRate()+"]");
			sceneConfirmStatInfo.setSceneConfirmRate(numberFormat.format(Double.parseDouble(sceneConfirmStatInfo.getSceneConfirmRate())/count*100)+"%["+sceneConfirmStatInfo.getSceneConfirmRate()+"]");
			sceneConfirmStatInfo.setHasExamNo(numberFormat.format(Double.parseDouble(sceneConfirmStatInfo.getHasExamNo())/count*100)+"%["+sceneConfirmStatInfo.getHasExamNo()+"]");
		}
		return new IPageInfo((Page) confirmStatInfos);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SceneConfirmStatInfo countSceneConfirmStat(int start, int length,SceneConfirmStatQuery statQuery)
	{
		BaseUser user = getUser();
		SceneConfirmStatInfo sceneConfirmStatInfo = sceneConfirmStatMapper.countSceneConfirmStat(statQuery,user);
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		int count = Integer.parseInt(sceneConfirmStatInfo.getCount());
		sceneConfirmStatInfo.setExamInfoConfirmRate(numberFormat.format(Double.parseDouble(sceneConfirmStatInfo.getExamInfoConfirmRate())/count*100)+"%["+sceneConfirmStatInfo.getExamInfoConfirmRate()+"]");
		sceneConfirmStatInfo.setPlaceConfirmRate(numberFormat.format(Double.parseDouble(sceneConfirmStatInfo.getPlaceConfirmRate())/count*100)+"%["+sceneConfirmStatInfo.getPlaceConfirmRate()+"]");
		sceneConfirmStatInfo.setWebRegisterRate(numberFormat.format(Double.parseDouble(sceneConfirmStatInfo.getWebRegisterRate())/count*100)+"%["+sceneConfirmStatInfo.getWebRegisterRate()+"]");
		sceneConfirmStatInfo.setExamPayRate(numberFormat.format(Double.parseDouble(sceneConfirmStatInfo.getExamPayRate())/count*100)+"%["+sceneConfirmStatInfo.getExamPayRate()+"]");
		sceneConfirmStatInfo.setSignRate(numberFormat.format(Double.parseDouble(sceneConfirmStatInfo.getSignRate())/count*100)+"%["+sceneConfirmStatInfo.getSignRate()+"]");
		sceneConfirmStatInfo.setSceneConfirmRate(numberFormat.format(Double.parseDouble(sceneConfirmStatInfo.getSceneConfirmRate())/count*100)+"%["+sceneConfirmStatInfo.getSceneConfirmRate()+"]");
		sceneConfirmStatInfo.setHasExamNo(numberFormat.format(Double.parseDouble(sceneConfirmStatInfo.getHasExamNo())/count*100)+"%["+sceneConfirmStatInfo.getHasExamNo()+"]");
		return sceneConfirmStatInfo;
	}

	private BaseUser getUser(){
		BaseUser user = SessionUtil.getUser();
		// 校长、副校长、副校长助理、营销管理中心助理
		if (user.getJtList().contains("XZ") || user.getJtList().contains("FXZ") || user.getJtList().contains("FXZZL") || user.getJtList().contains("YXGLZXZL") || user.getJtList().contains("XCQR_ALL")) {
			user.setUserLevel("1");
		}
		// 校监助理和校监一样权限
		if (user.getJtList().contains("XJZL")) {
			OaEmployeeJobInfo jobInfo = oaEmployeeMapper.getEmployeeJobInfo(user.getEmpId());
			List<SessionDpInfo> list =new ArrayList<SessionDpInfo>();
			SessionDpInfo sessionDpInfo = new SessionDpInfo();
			sessionDpInfo.setDpId(jobInfo.getDpId());
			list.add(sessionDpInfo);
			user.setMyDpList(list);
			user.setUserLevel("3");
		}
		return user;
	}

}
