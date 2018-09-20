package com.yz.dao.oa;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.oa.EmpModifyListInfo;
import com.yz.model.oa.OaEmpLearnInfoPerfInfo;
import com.yz.model.oa.OaEmployeeLearnInfo;
import com.yz.model.oa.OaEmployeeModifyInfo;
import com.yz.model.oa.OaEmployeePerfQuery;

/**
 * 分配招生老师绩效
 * @author lx
 * @date 2017年11月8日 上午10:32:43
 */
public interface OaEmployeePerfMapper
{
	
	//所有的待分配学员信息
	public List<OaEmpLearnInfoPerfInfo> getOaEmpLearnInfoPerfInfo(OaEmployeePerfQuery perfQuery);
	
	//部分归属到原部门
	public void partChangeToOld(@Param("list") List<OaEmpLearnInfoPerfInfo> list,@Param("user") BaseUser user,@Param("modifyInfo") EmpModifyListInfo modifyInfo);
	
	//部分归属到新部门
	public void partChangeToNew(@Param("list") List<OaEmpLearnInfoPerfInfo> list,@Param("user") BaseUser user,@Param("modifyInfo") EmpModifyListInfo modifyInfo);
	//全部归属到原部门
	public void allChangeToOld(@Param("modifyInfo") EmpModifyListInfo modifyInfo,@Param("user") BaseUser user);
	
	//全部归属到原部门
	public void allChangeToNew(@Param("modifyInfo") EmpModifyListInfo modifyInfo,@Param("user") BaseUser user);
	
	//学员学业对应的用户信息(部分)
	public List<String> getUserIdByLearnId(@Param("list") List<OaEmpLearnInfoPerfInfo> list);
	
	//改变招生关系(部分(归属到新))
	public void updateBdLearnRules(@Param("list") List<OaEmpLearnInfoPerfInfo> list,@Param("modifyInfo") EmpModifyListInfo modifyInfo);
	
	//改变招生关系(全部(归属到新))
	public void updateAllBdLearnRules(@Param("modifyInfo") EmpModifyListInfo modifyInfo);
	
	///学员学业对应的用户信息(全部)
	public List<String> getAllUserIdByLearnId(@Param("empId") String empId,@Param("dpId") String dpId);
	
	//改变招生关系(部分(归属到老))
	public void updateBdLearnRulesForOld(@Param("list") List<OaEmpLearnInfoPerfInfo> list,@Param("modifyInfo") EmpModifyListInfo modifyInfo);
	
	//改变招生关系(全部(归属到老))
	public void updateAllBdLearnRulesForOld(@Param("modifyInfo") EmpModifyListInfo modifyInfo);
	
	//改变招生关系(生效时间小于当前时间)
	public void updateBdLearnRulesForChange(@Param("list") List<OaEmployeeLearnInfo> list,@Param("modifyInfo") OaEmployeeModifyInfo modifyInfo);
}
