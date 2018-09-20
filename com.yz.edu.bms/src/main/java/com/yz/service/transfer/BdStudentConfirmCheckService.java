package com.yz.service.transfer;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.constants.CheckConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.transfer.BdStudentConfirmModifyCheckMapper;
import com.yz.dao.transfer.BdStudentModifyMapper;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.model.admin.BaseUser;
import com.yz.model.transfer.BdStudentConfirmModify;
import com.yz.model.transfer.BdStudentModify;
import com.yz.model.transfer.StudentModifyMap;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdStudentConfirmCheckService{

	private static final Logger log = LoggerFactory.getLogger(BdStudentConfirmCheckService.class);


	@Autowired
	private BdStudentConfirmModifyCheckMapper studentModifyMapper;
		
	@Autowired
	private BdStudentModifyMapper BdstudentModifyMapper;
	
	public List<Map<String, String>> findStudentCheckModify(StudentModifyMap studentModifyMap, int start, int length) {
		PageHelper.offsetPage(start, length);
		//职称权限设置。
		BaseUser user = SessionUtil.getUser();
		if(null !=  user.getJtList()){
				if(user.getJtList().contains("XCQR_ALL")){
						user.setUserLevel("1");
				}
		}
		return studentModifyMapper.findConfirmModify(studentModifyMap, user);
	}

	public BdStudentConfirmModify findStudentModifyById(String modifyId) {
		// TODO Auto-generated method stub
		return studentModifyMapper.findStudentModifyById(modifyId);
	}

	public List<Map<String, String>> findStudentModifyByModifyId(String modifyId) {
		// TODO Auto-generated method stub
		return studentModifyMapper.findStudentModifyByModifyId(modifyId);
	}

	public void passStudentModifyCheck(String modifyId,String checkStatus,String remark,String reason){
		
			BaseUser user = SessionUtil.getUser();
			 if("3".equals(checkStatus)){
				 BdStudentConfirmModify bdStudentModify = findStudentModifyById(modifyId);
				// 修改后收费标准ID
				 if (StringUtil.hasValue(bdStudentModify.getnPfsnId())
							|| StringUtil.hasValue(bdStudentModify.getnScholarship())
							|| StringUtil.hasValue(bdStudentModify.getnTaId())) {

						if (!StringUtil.hasValue(bdStudentModify.getnPfsnId())) {
							bdStudentModify.setnPfsnId(bdStudentModify.getPfsnId());
						}
						if (!StringUtil.hasValue(bdStudentModify.getnScholarship())) {
							bdStudentModify.setnScholarship(bdStudentModify.getScholarship());
						}

						if (!StringUtil.hasValue(bdStudentModify.getnTaId())) {
							bdStudentModify.setnTaId(bdStudentModify.getTaId());
						}
					String feeId = BdstudentModifyMapper.selectFeeStandard(bdStudentModify.getnPfsnId(),bdStudentModify.getnTaId(), bdStudentModify.getnScholarship());

					if (!StringUtil.hasValue(feeId)) {
						String[] name = new String[] { bdStudentModify.getStdName() };
						throw new BusinessException("E000077", name); // 无收费标准
					}

					// 当前收费标准ID
					String nowFeeId = BdstudentModifyMapper.selectNowFeeId(bdStudentModify.getLearnId());
					if (!StringUtil.hasValue(nowFeeId)) {
						throw new BusinessException("E000089"); // 现收费标准不存在
					}
				 }
				 studentModifyMapper.passStudentModifyCheck(modifyId,remark,user);
				 studentModifyMapper.passStudentModifyRecordOneCheck(checkStatus,modifyId,reason,remark,user);
				 studentModifyMapper.passStudentModifyRecordTwoCheck(checkStatus,modifyId);
			 }else{
				 studentModifyMapper.passStudentModifyRecordOneCheck(checkStatus,modifyId,reason,remark,user);
			 }
			 
			 
		
	}
	
	public void passModifyBatch(String[] modifyIds){
		for (String modifyId : modifyIds) {
			BdStudentConfirmModify bdStudentModify = findStudentModifyById(modifyId);
			 if (StringUtil.hasValue(bdStudentModify.getnPfsnId())
						|| StringUtil.hasValue(bdStudentModify.getnScholarship())
						|| StringUtil.hasValue(bdStudentModify.getnTaId())) {

					if (!StringUtil.hasValue(bdStudentModify.getnPfsnId())) {
						bdStudentModify.setnPfsnId(bdStudentModify.getPfsnId());
					}
					if (!StringUtil.hasValue(bdStudentModify.getnScholarship())) {
						bdStudentModify.setnScholarship(bdStudentModify.getScholarship());
					}

					if (!StringUtil.hasValue(bdStudentModify.getnTaId())) {
						bdStudentModify.setnTaId(bdStudentModify.getTaId());
					}
				String feeId = BdstudentModifyMapper.selectFeeStandard(bdStudentModify.getnPfsnId(),bdStudentModify.getnTaId(), bdStudentModify.getnScholarship());

				if (!StringUtil.hasValue(feeId)) {
					String[] name = new String[] { bdStudentModify.getStdName() };
					throw new BusinessException("E000077", name); // 无收费标准
				}

				// 当前收费标准ID
				String nowFeeId = BdstudentModifyMapper.selectNowFeeId(bdStudentModify.getLearnId());
				if (!StringUtil.hasValue(nowFeeId)) {
					throw new BusinessException("E000089"); // 现收费标准不存在
				}
			 }
/*			passModify(modifyId, CheckConstants.PASS_CHECK_2, null);*/
		}
		BaseUser user = SessionUtil.getUser();
		studentModifyMapper.passModifyBatch(modifyIds,user);
	}

}
