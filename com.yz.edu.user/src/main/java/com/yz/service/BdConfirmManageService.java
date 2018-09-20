package com.yz.service;

import com.yz.constants.TransferConstants;
import com.yz.generator.IDGenerator;
import com.yz.model.student.BdStudentModify;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.dao.BdConfirmManageMapper;
import com.yz.model.BdConfirmManage;
import com.yz.model.BdConfirmStudentInfo;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class BdConfirmManageService {
	
	@Autowired
	private BdConfirmManageMapper bdConfirmManageMapper;
    @Autowired
    private BdStudentOutService studentOuntService;
	
	public Object getConfirmManageList(String taId,String confirmCity,String confirmAddressLevel) {
		BdConfirmManage bdConfirmManage =new BdConfirmManage();
		bdConfirmManage.setTaId(taId);
		bdConfirmManage.setConfirmCity(confirmCity);
        bdConfirmManage.setConfirmAddressLevel(confirmAddressLevel);
		//首先查询出确认点名称
		List<Map<String,String>> list = bdConfirmManageMapper.getConfirmName(bdConfirmManage);
		List<HashMap<String,Object>> confirm = new ArrayList<>();
		//根据名称查询对应的确认点具体信息
		for(Map<String,String> item:list){
			if(item.get("confirmName")!=null){
                bdConfirmManage.setConfirmName(item.get("confirmName"));
                List<BdConfirmManage> confirmInfo = bdConfirmManageMapper.getConfirmManageList(bdConfirmManage);
				HashMap hashMap = new HashMap();
				hashMap.put("confirmName",item.get("confirmName"));
				//判断查询条件是否有确认日期
				hashMap.put("confirmInfo",confirmInfo);
				confirm.add(hashMap);
			}
		}

		return confirm;
	}
	
	public Object getConfirmCity(){
		return bdConfirmManageMapper.getConfirmCity();
	}
	
	public Object getTaName(String confirmCity){
		BdConfirmManage bdConfirmManage =new BdConfirmManage();
		bdConfirmManage.setConfirmCity(confirmCity);
		return bdConfirmManageMapper.getTaName(bdConfirmManage);
	}
	
	public Object getConfirmLevel(String taId,String confirmCity){
        BdConfirmManage bdConfirmManage =new BdConfirmManage();
        bdConfirmManage.setConfirmCity(confirmCity);
        bdConfirmManage.setTaId(taId);
		return bdConfirmManageMapper.getConfirmLevel(bdConfirmManage);
	}

	public Object getConfirmInfo(String searchInfo,String userId){
		//根据userid查询用户所属城市编码
        String city_code = "";
		if(StringUtil.hasValue(userId)){
			HashMap hashMap = bdConfirmManageMapper.getCityCodeByUserId(userId);
			if(hashMap!=null && !hashMap.isEmpty()){
			    city_code = (String) hashMap.get("city_code");
            }
		}
		//根据用户所在城市编码和查询条件搜索数据
		return bdConfirmManageMapper.getConfirmInfo(searchInfo,city_code);
	}

    /**
     * 签到确认
     * @param bds
     */
	public void confirmSign(BdConfirmStudentInfo bds){
	    //首先获取考生号
        BdConfirmStudentInfo hashMap = bdConfirmManageMapper.existExamNo(bds.getConfirmId());
        if(!StringUtil.hasValue(hashMap.getExamNo())){
            //学员没有考生号，现在输入考生号 添加
            if(StringUtil.hasValue(bds.getExamNo())) {
                bdConfirmManageMapper.insertExamNo(hashMap.getLearnId(), hashMap.getStdId(), bds.getExamNo());
                //添加学员信息变更记录
                addModifyRecord(hashMap.getLearnId(), hashMap.getStdId(), "添加考生号：" + bds.getExamNo(),bds.getUpdateUserId(),bds.getUpdateUser());
            }
        }else{
            //学员有考生号与现在输入的考生号不一致 修改
            if(bds.getExamNo()==null || !bds.getExamNo().equals(hashMap.getExamNo())){
                //添加学员信息变更记录
                addModifyRecord(hashMap.getLearnId(), hashMap.getStdId(),"将考生号'"+hashMap.getExamNo()+"'变更为'"+bds.getExamNo()+"'",bds.getUpdateUserId(),bds.getUpdateUser());
                bdConfirmManageMapper.updateExamNo(hashMap.getLearnId(),bds.getExamNo());
            }
        }
        String signTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
        if(bds.getSignStatus()==1 && hashMap.getSignStatus()==0){
            bds.setSignTime(signTime);
        }
        bds.setSignUserId(bds.getUpdateUserId());
         bdConfirmManageMapper.confirmSign(bds);
    }

    /**
     * 添加考生号变更记录
     * @param learnId
     * @param stdId
     * @param modifyText
     */
    public void addModifyRecord(String learnId,String stdId,String modifyText,String userId,String userName){
        //如果教材地址有变更，填写变更记录
        BdStudentModify studentModify = new BdStudentModify();
        studentModify.setLearnId(learnId);
        studentModify.setStdId(stdId);
        studentModify.setExt1(modifyText);
        studentModify.setIsComplete("1");
        studentModify.setModifyType(TransferConstants.MODIFY_TYPE_CHANGE_EXAMNO_10);
        studentModify.setModifyId(IDGenerator.generatorId());
        studentOuntService.addStudentModifyRecord(studentModify, userName, userId);
    }
}
