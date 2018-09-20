package com.yz.service.transfer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.finance.BdOrderMapper;
import com.yz.dao.transfer.BdStudentChangeMapper;
import com.yz.dao.transfer.BdStudentModifyMapper;
import com.yz.dao.transfer.BdStudentRecoveryMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.transfer.BdStudentChange;
import com.yz.model.transfer.BdStudentModify;
import com.yz.util.DateUtil;

/**
 * 学员学籍还原
 * 
 * @ClassName: BdStudentRecoveryService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhanggh
 * @date 2018年6月30日
 *
 */

@Service
@Transactional
public class BdStudentRecoveryService {

	@Autowired
	private BdStudentRecoveryMapper bdStudentRecoveryMapper;

	@Autowired
	private BdStudentChangeMapper studentChangeMapper;
	@Autowired
	private BdOrderMapper orderMapper;
	@Autowired
	private BdStudentModifyMapper studentModifyMapper;

	/**
	 * 还原
	 * 
	 * 1.检查现在的学籍是否能进行还原（当前学籍不能缴费，缴费后不允许还原）
	 * 
	 * 2.把现在的学籍进行退学处理，增加退学记录。
	 * 
	 * 3.把之前的学籍进行恢复。
	 * 
	 * 4.把之前的退学记录设置为已删除。
	 * 
	 * @desc 1.检查现在的学籍是否能进行还原（当前学籍不能缴费，缴费后不允许还原）
	 * @param stdId
	 * @param oldLearnId
	 */
	public boolean studentRecovery(String learnId, String stdId, String oldLearnId) throws BusinessException {
		int count = bdStudentRecoveryMapper.selectSubOrderForLearnId(learnId);
		if (count > 0) {
			throw new BusinessException("E000124"); // 已存在缴费订单，不允许还原
		}
		BdLearnInfo learnInfo = bdStudentRecoveryMapper.selectBdLearnInfoForLearnId(learnId);
		BdLearnInfo oldLearnInfo = bdStudentRecoveryMapper.selectBdLearnInfoForLearnId(oldLearnId);
		String changeId = studentChangeMapper.getchangeIdForLearnId(oldLearnId);
		BaseUser user = SessionUtil.getUser();
		// 改变学员现在的学籍信息
		studentChangeMapper.exitStudent(learnId);
		// 订单作废
		orderMapper.outOrder(learnId);
		// 恢复之前的订单
		orderMapper.recoveryOrder(oldLearnId);
		// 恢复之前的学业
		studentChangeMapper.recoveryLearnId(oldLearnId, changeId);
		// 删除优惠券
		studentChangeMapper.deleteStudentCoupon(stdId);
		// 写入变更记录
		BdStudentModify studentModify = new BdStudentModify();
		// 审核类型为新生信息修改
		studentModify.setCheckType(TransferConstants.CHECK_TYPE_CHANGE_BEFORE);
		// 变更类型为新生信息修改
		studentModify.setModifyType(TransferConstants.MODIFY_TYPE_CHANGE_sceneRegister_13);
		studentModify.setCheckOrder("1");
		studentModify.setLearnId(oldLearnId);
		studentModify.setStdId(stdId);
		studentModify.setUnvsId(learnInfo.getUnvsId());
		studentModify.setNewUnvsId(oldLearnInfo.getUnvsId());
		studentModify.setNewTaId(oldLearnInfo.getTaId());
		studentModify.setTaId(learnInfo.getTaId());
		studentModify.setNewPfsnId(oldLearnInfo.getPfsnId());
		studentModify.setPfsnId(learnInfo.getPfsnId());
		studentModify.setNewScholarship(oldLearnInfo.getScholarship());
		studentModify.setScholarship(learnInfo.getScholarship());
		studentModify.setNewStdStage(StudentConstants.STD_STAGE_PURPOSE);
		studentModify.setOldStdStage(learnInfo.getStdStage());
		studentModify.setIsComplete("1");
		studentModify.setCreateUser(user.getRealName());
		studentModify.setCreateUserId(user.getUserId());
		studentModify.setModifyId(IDGenerator.generatorId());
		// 插入
		studentModifyMapper.insertSelective(studentModify);
		// 保存要改变的记录
		BdStudentChange studentChange = new BdStudentChange();
		studentChange.setChangeId(IDGenerator.generatorId());
		studentChange.setCheckOrder("2");
		studentChange.setCheckType("1");
		studentChange.setCreateTime(DateUtil.getNowDateAndTime());
		studentChange.setCreateUser(user.getRealName());
		studentChange.setCreateUserId(user.getUserId());
		studentChange.setUpdateUser(user.getRealName());
		studentChange.setUpdateUserId(user.getUserId());
		studentChange.setIsComplete("1");
		studentChange.setLearnId(learnId);
		studentChange.setStdId(learnInfo.getStdId());
		studentChange.setStdName(learnInfo.getStdName());
		studentChange.setStdStage(learnInfo.getStdStage());
		studentChange.setNewLearnId(oldLearnId);
		studentChange.setNewScholarship(oldLearnInfo.getScholarship());
		studentChange.setNewUnvsId(oldLearnInfo.getUnvsId());
		studentChange.setNewPfsnId(oldLearnInfo.getPfsnId());
		studentChange.setNewTaId(oldLearnInfo.getTaId());
		studentChangeMapper.insertSelective(studentChange);
		return true;
	}
}
