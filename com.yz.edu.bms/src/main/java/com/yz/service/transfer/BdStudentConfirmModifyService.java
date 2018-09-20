package com.yz.service.transfer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.AtsAccountApi;
import com.yz.constants.GlobalConstants;
import com.yz.constants.TransferConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.security.service.ISecurityService;
import com.yz.dao.oa.OaSessionMapper;
import com.yz.dao.sceneMng.BdPrintingForecastMapper;
import com.yz.dao.transfer.BdStudentConfirmModifyMapper;
import com.yz.dao.transfer.BdStudentModifyMapper;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.SessionDpInfo;
import com.yz.model.system.SysDict;
import com.yz.model.transfer.BdCheckRecord;
import com.yz.model.transfer.BdStudentModify;
import com.yz.model.transfer.StudentModifyMap;
import com.yz.service.system.SysDictService;
import com.yz.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BdStudentConfirmModifyService {

	private static final Logger log = LoggerFactory.getLogger(BdStudentConfirmModifyService.class);

	@Autowired
	private BdStudentConfirmModifyMapper bdStudentConfirmModifyMapper;

	@Reference(version = "1.0")
	private AtsAccountApi atdApi;
	@Autowired
	private BdStudentChangeService changeService;
	@Autowired
	private SysDictService sysDictService;
	
	@Autowired
	private BdStudentOutService studentOutService;
	@Autowired
	private BdCheckRecordService checkRecordService;
	@Autowired
	private BdStudentModifyMapper studentModifyMapper;
	
	@Autowired
	private BdPrintingForecastMapper bdPrintingForecastMapper;
    @Autowired
    private OaSessionMapper sessionMapper;

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

    /**
     * 申请的人：招生老师（只能是申请/查看自己的学员）；校监/校监助理（申请/查看自己的学员以及离职招生老师的学员）；
     //现场工作人员（类似“现场确认-惠州”数据权限，可申请/查看惠州的学员）；现场确认总负责人（“现场确认-总负责人”，可申请/查看全部的学员）
     * @param user
     * @return
     */
    private BaseUser setUserLevel(BaseUser user){
        List<String> array = bdPrintingForecastMapper.findConfirmDictValue("现场确认");
        boolean is = false;
        if("8".equals(user.getUserLevel())){
            //根据校监助理部门id查询校部门负责人
            String empId = sessionMapper.getEmpIdByDpId(user.getDepartmentId());
            List<SessionDpInfo> dpList = getDepartmentList(empId);// 校监级别权限
            if (dpList != null && !dpList.isEmpty()) {
                user.setMyDpList(dpList);
                user.setUserLevel(GlobalConstants.USER_LEVEL_DEPARTMENT);
            }
        }

        if (user.getJtList().contains("XCQR_ALL")) {//现场确认工作人员
            user.setUserLevel("1");
        }else {
            List<String> array1 = new ArrayList<String>();
            if(user.getJtList().size()>0){
                for(String jtCode : user.getJtList()){

                    if(array.contains(jtCode)){
                        array1.add(jtCode);
                        is = true;
                    }
                }
            }else{
                user.setJtList(null);
            }

            if(array1.size()>0){
                user.setJtList(array1);
            }else{
                user.setJtList(null);
            }

        }

        if (is){
            user.setUserLevel("100");
        }
        return user;
    }
	

	public List<Map<String, String>> findConfirmModify(StudentModifyMap studentModifyMap, int start, int length) {
		BaseUser user = SessionUtil.getUser();
		//设置权限
	    user = setUserLevel(user);
        PageHelper.offsetPage(start, length);
		return bdStudentConfirmModifyMapper.findConfirmModify(studentModifyMap, user);
	}
	
	public List<Map<String, String>> findStudentInfo(String sName, int page, int rows) {
		BaseUser user = SessionUtil.getUser();
        //设置权限
        user = setUserLevel(user);

        PageHelper.startPage(page, rows);
		return bdStudentConfirmModifyMapper.findStudentInfo(sName, user);
	}
	
	public Map<String, String> findStudentEnrollInfo(String learnId) {
		// TODO Auto-generated method stub
		return bdStudentConfirmModifyMapper.findStudentEnrollInfo(learnId);
	}
	
	public void deleteStudentModify(String[] idArray) {
		// TODO Auto-generated method stub
		bdStudentConfirmModifyMapper.deleteStudentModify(idArray);
	}
	
	public BdStudentModify findStudentModifyById(String modifyId) {
		// TODO Auto-generated method stub
		return bdStudentConfirmModifyMapper.findStudentModifyById(modifyId);
	}
	
	public void insertConfirmModify(BdStudentModify studentModify) {
		checkParam(studentModify);
		checkRepeat(studentModify);

		// 检查流水金额是否大于订单金额
		changeService.checkStudentSerial(studentModify.getLearnId());

		if (StringUtil.hasValue(studentModify.getNewScholarship())) {

			// TODO 临时加入优惠分组 后续可以从页面带过来
			String scholarship = studentModify.getScholarship();
			SysDict dict = sysDictService.getDict("scholarship." + scholarship);
			String sg = dict.getExt1();// 优惠分组
			studentModify.setSg(sg);

			String newScholarship = studentModify.getNewScholarship();
			SysDict newDict = sysDictService.getDict("scholarship." + newScholarship);
			String newSg = newDict.getExt1();// 优惠分组
			studentModify.setNewSg(newSg);
		}
		studentModify.setModifyId(IDGenerator.generatorId());
		//基础信息修改
		studentModifyMapper.insertSelective(studentModify);
		
		//添加变动信息
		BdStudentModify bdInfo = bdStudentConfirmModifyMapper.findStudentModifyById(studentModify.getModifyId());
		if(StringUtil.hasValue(bdInfo.getGraduateTime())){
			bdInfo.setGraduateTime(bdInfo.getGraduateTime().substring(0,10));
		}
		if(StringUtil.hasValue(bdInfo.getNewGraduateTime())){
			bdInfo.setNewGraduateTime(bdInfo.getNewGraduateTime().substring(0,10));
		}
		String ext1 = modifyExt(bdInfo);
		bdStudentConfirmModifyMapper.updateExt1(studentModify.getModifyId(),ext1);
		
		// 添加审核记录
		// 获取审核权重
		List<Map<String, String>> checkWeights = null;
		checkWeights = studentOutService.getCheckWeight(TransferConstants.CHECK_TYPE_CONFIRM);
		for (Map<String, String> map : checkWeights) {
			// 初始化审核记录
			BdCheckRecord checkRecord = new BdCheckRecord();
			checkRecord.setMappingId(studentModify.getModifyId());
			checkRecord.setCheckOrder(map.get("checkOrder"));
			checkRecord.setCheckType(map.get("checkType"));
			checkRecord.setJtId(map.get("jtId"));
			checkRecord.setCheckStatus("1");
			checkRecordService.addBdCheckRecord(checkRecord);
		}
	}
	
	/**
	   * 代码整改,获取修改的内容
	   * @param type     修改的类型
	   * @param oldNex   旧内容
	   * @param newNex  新内容
	   * @return
	   */
	private StringBuilder getModifyExt(String type, String oldNex ,String newNex){
	    StringBuilder result = new StringBuilder();
	    result.append(type);
	    result.append("[");
        result.append(StringUtil.hasValue(oldNex) ? oldNex : "");
        result.append("]");
        result.append(" ==> ");
        result.append("[");
        result.append(newNex);
        result.append("]");
	    result.append("<br/>");
	    return result;
	  }
	  
	  private String modifyExt(BdStudentModify studentModify){
		  StringBuilder text = new StringBuilder();
		  if(StringUtil.hasValue(studentModify.getNewStdName()) && !studentModify.getNewStdName().equals(studentModify.getStdName())){
				text.append(getModifyExt("姓名：",studentModify.getStdName(),studentModify.getNewStdName()));
			}
		  if(StringUtil.hasValue(studentModify.getNewIdCard()) && !studentModify.getNewIdCard().equals(studentModify.getIdCard())){
				text.append(getModifyExt("身份证：",studentModify.getIdCard(),studentModify.getNewIdCard()));
			}
		  if(StringUtil.hasValue(studentModify.getNewSex()) && !studentModify.getNewSex().equals(studentModify.getSex())){
				 SysDict dict = sysDictService.getDict("sex." + studentModify.getSex());
				  SysDict newDict = sysDictService.getDict("sex." + studentModify.getNewSex());
				  String Value = dict == null ? "" : dict.getDictName();
				  String newValue = newDict == null ? "" : newDict.getDictName();
				  text.append(getModifyExt("性别：",Value,newValue));
			}
		  if(StringUtil.hasValue(studentModify.getNewNation()) && !studentModify.getNewNation().equals(studentModify.getNation())){
			  SysDict dict = sysDictService.getDict("nation." + studentModify.getNation());
			  SysDict newDict = sysDictService.getDict("nation." + studentModify.getNewNation());
			  String Value = dict == null ? "" : dict.getDictName();
			  String newValue = newDict == null ? "" : newDict.getDictName();
			  text.append(getModifyExt("民族：",Value,newValue));
			}
		  if(StringUtil.hasValue(studentModify.getNunvsName()) && !studentModify.getNunvsName().equals(studentModify.getUnvsName())){
			  text.append(getModifyExt("院校：",studentModify.getUnvsName(),studentModify.getNunvsName()));
		  }
		  if(StringUtil.hasValue(studentModify.getNpfsnName()) && !studentModify.getNpfsnName().equals(studentModify.getPfsnName())){
			  text.append(getModifyExt("专业：",studentModify.getPfsnName(),studentModify.getNpfsnName()));
		  }
          if(StringUtil.hasValue(studentModify.getNpfsnLevel()) && !studentModify.getPfsnLevel().equals(studentModify.getNpfsnLevel())){
              SysDict dict = sysDictService.getDict("pfsnLevel." + studentModify.getPfsnLevel());
              SysDict newDict = sysDictService.getDict("pfsnLevel." + studentModify.getNpfsnLevel());
              String Value = dict == null ? "" : dict.getDictName();
              String newValue = newDict == null ? "" : newDict.getDictName();
		      text.append(getModifyExt("层次：",Value,newValue));
          }
          if(StringUtil.hasValue(studentModify.getNtaName()) && !studentModify.getNtaName().equals(studentModify.getTaName())){
  			text.append(getModifyExt("考区：",studentModify.getTaName(),studentModify.getNtaName()));
  		}
		  if(StringUtil.hasValue(studentModify.getNewScholarship()) && !studentModify.getNewScholarship().equals(studentModify.getScholarship())){
			  SysDict dict = sysDictService.getDict("scholarship." + studentModify.getScholarship());
			  SysDict newDict = sysDictService.getDict("scholarship." + studentModify.getNewScholarship());
			  String Value = dict == null ? "" : dict.getDictName();
			  String newValue = newDict == null ? "" : newDict.getDictName();
			  text.append(getModifyExt("优惠类型：",Value,newValue));
		  }
			
		 if(StringUtil.hasValue(studentModify.getNewRprAddress()) && !studentModify.getNewRprAddress().equals(studentModify.getRprAddress())){
				text.append(getModifyExt("户口所在地：",studentModify.getRprAddress(),studentModify.getNewRprAddress()));
		}
		if(StringUtil.hasValue(studentModify.getNewGraduateUnvsName()) && !studentModify.getNewGraduateUnvsName().equals(studentModify.getGraduateUnvsName())){
			text.append(getModifyExt("原毕业院校：",studentModify.getGraduateUnvsName(),studentModify.getNewGraduateUnvsName()));
		}
		if(StringUtil.hasValue(studentModify.getNewGraduateTime()) && !studentModify.getNewGraduateTime().equals(studentModify.getGraduateTime())){
			text.append(getModifyExt("原毕业时间：",studentModify.getGraduateTime(),studentModify.getNewGraduateTime()));
		}
		if(StringUtil.hasValue(studentModify.getNewGraduateEdcsType()) && !studentModify.getNewGraduateEdcsType().equals(studentModify.getGraduateEdcsType())){
			 SysDict dict = sysDictService.getDict("edcsType." + studentModify.getGraduateEdcsType());
			 String Value = dict == null ? "" : dict.getDictName();
			 SysDict newDictType = sysDictService.getDict("edcsType." + studentModify.getNewGraduateEdcsType());
			String newValueType = newDictType == null ? "" : newDictType.getDictName();
			text.append(getModifyExt("原学历类型：",Value,newValueType));
		}
		if(StringUtil.hasValue(studentModify.getNewGraduateProfession()) && !studentModify.getNewGraduateProfession().equals(studentModify.getGraduateProfession())){
			text.append(getModifyExt("原毕业专业：",studentModify.getGraduateProfession(),studentModify.getNewGraduateProfession()));
		}
		if(StringUtil.hasValue(studentModify.getNewGraduateDiploma()) && !studentModify.getNewGraduateDiploma().equals(studentModify.getGraduateDiploma())){
			text.append(getModifyExt("原毕业证编号：",studentModify.getGraduateDiploma(),studentModify.getNewGraduateDiploma()));
		}
		return text.toString();
	  }
	
	private void checkParam(BdStudentModify studentModify) {
		boolean blankFlag = false;
		if (StringUtil.hasValue(studentModify.getNewStdName())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewIdCard())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewSex())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewNation())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewUnvsId())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewPfsnId())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewScholarship())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewTaId())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewGraduateUnvsName())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewGraduateTime())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewGraduateEdcsType())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewGraduateProfession())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewGraduateDiploma())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewRprAddress())) {
			blankFlag = true;
		}
		if (!blankFlag) {
			throw new BusinessException("E000078"); // 提交内容不能为空
		}
	}
	
	private void checkRepeat(BdStudentModify studentModify) {

		boolean repeatFlag = true;

		if (StringUtil.hasValue(studentModify.getNewStdName())
				&& studentModify.getStdName().equals(studentModify.getNewStdName())) {
			throw new BusinessException("E000079", new String[] { "姓名" }); // 未做任何修改
		}
		if (StringUtil.hasValue(studentModify.getNewIdCard())
				&& studentModify.getIdCard().equals(studentModify.getNewIdCard())) {
			throw new BusinessException("E000079", new String[] { "身份证" }); // 未做任何修改
		}
		if (StringUtil.hasValue(studentModify.getNewSex())
				&& studentModify.getSex().equals(studentModify.getNewSex())) {
			throw new BusinessException("E000079", new String[] { "性别" }); // 未做任何修改
		}
		if (StringUtil.hasValue(studentModify.getNewNation())
				&& studentModify.getNation().equals(studentModify.getNewNation())) {
			throw new BusinessException("E000079", new String[] { "名族" }); // 未做任何修改
		}
		if (StringUtil.hasValue(studentModify.getNewGraduateDiploma())
				&& studentModify.getGraduateDiploma().equals(studentModify.getNewGraduateDiploma())) {
			throw new BusinessException("E000079", new String[] { "原毕业证编号" }); // 未做任何修改
		}
		if (StringUtil.hasValue(studentModify.getNewGraduateUnvsName())
				&& studentModify.getGraduateUnvsName().equals(studentModify.getNewGraduateUnvsName())) {
			throw new BusinessException("E000079", new String[] { "原毕业院校" }); // 未做任何修改
		}
		if (StringUtil.hasValue(studentModify.getNewGraduateTime())
				&& studentModify.getGraduateTime().equals(studentModify.getNewGraduateTime())) {
			throw new BusinessException("E000079", new String[] { "原毕业时间" }); // 未做任何修改
		}
		if (StringUtil.hasValue(studentModify.getNewGraduateEdcsType())
				&& studentModify.getGraduateEdcsType().equals(studentModify.getNewGraduateEdcsType())) {
			throw new BusinessException("E000079", new String[] { "原毕业学历类型" }); // 未做任何修改
		}
		if (StringUtil.hasValue(studentModify.getNewGraduateProfession())
				&& studentModify.getGraduateProfession().equals(studentModify.getNewGraduateProfession())) {
			throw new BusinessException("E000079", new String[] { "原毕业专业" }); // 未做任何修改
		}
		if (StringUtil.hasValue(studentModify.getNewRprAddress())
				&& studentModify.getRprAddress().equals(studentModify.getNewRprAddress())) {
			throw new BusinessException("E000079", new String[] { "户口所在地" }); // 未做任何修改
		}
		if (StringUtil.hasValue(studentModify.getNewUnvsId())) {

			if (!studentModify.getUnvsId().equals(studentModify.getNewUnvsId())) {
				repeatFlag = false;
			}
			if (StringUtil.hasValue(studentModify.getNewPfsnId())
					&& !studentModify.getPfsnId().equals(studentModify.getNewPfsnId())) {
				repeatFlag = false;
			}
			if (StringUtil.hasValue(studentModify.getNewScholarship())
					&& !studentModify.getScholarship().equals(studentModify.getNewScholarship())) {
				repeatFlag = false;
			}
			if (StringUtil.hasValue(studentModify.getNewTaId())
					&& !studentModify.getTaId().equals(studentModify.getNewTaId())) {
				repeatFlag = false;
			}

			if (repeatFlag) {
				throw new BusinessException("E000079", new String[] { "报读信息" }); // 未做任何修改
			}
		}

	}

	public Integer ifModifyingByLearnId(String learnId){
        return bdStudentConfirmModifyMapper.ifModifyingByLearnId(learnId);
    }

    public  String getCityCodeByTaId(String taId){
        return bdStudentConfirmModifyMapper.getCityCodeByTaId(taId);
    }

}
