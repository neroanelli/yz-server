package com.yz.service.finance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.constants.TransferConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.finance.BdOrderMapper;
import com.yz.dao.finance.BdSubOrderMapper;
import com.yz.dao.finance.EditPayableMapper;
import com.yz.dao.transfer.BdStudentModifyMapper;
import com.yz.edu.paging.bean.IPageInfo;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.finance.BdSubOrder;
import com.yz.model.finance.EditPayableQuery;
import com.yz.model.finance.stdfee.BdPayableInfoResponse;
import com.yz.model.recruit.StudentAllListInfo;
import com.yz.model.transfer.BdStudentModify;
import com.yz.util.StringUtil;

@Service
@Transactional
public class EditPayableService {

	@Autowired
	private EditPayableMapper mapper;

	@Autowired
	private BdOrderMapper bdOrderMapper;

	@Autowired
	private BdSubOrderMapper bdSubOrderMapper;

	@Autowired
	private BdStudentModifyMapper modifyMapper;

	public Object selectStudentByPage(int start, int length, EditPayableQuery query) {
		if (StringUtil.hasValue(query.getStudentName()) || StringUtil.hasValue(query.getMobile())
				|| StringUtil.hasValue(query.getIdCard()) || StringUtil.hasValue(query.getRecruitType())
				|| StringUtil.hasValue(query.getScholarship()) || StringUtil.hasValue(query.getStdStage())) {
			PageHelper.offsetPage(start, length);
			List<BdPayableInfoResponse> payables = mapper.getEditPayableList(query);
			return new IPageInfo((Page) payables);
		} else {
			return new IPageInfo<StudentAllListInfo>();
		}
	}

	public Object selectStudent(String learnId) {
		return mapper.getStudent(learnId);
	}

	public void edit(String learnId, String stdId, String remark, String fileUrl, String[] itemCodes,
			String[] subOrderNos, String[] amounts) {
		if (itemCodes.length == 0) {
			return;
		}
		// 查询订单号
		String orderNo = bdOrderMapper.selectOrderNoByLearnId(learnId);
		String ext1 = "学员应缴费用变更:   ";
		// 改变子订单金额
		DecimalFormat myformat = new java.text.DecimalFormat("0.00");
		for (int i = 0; i < itemCodes.length; i++) {
			mapper.editPayables(subOrderNos[i], myformat.format(Double.valueOf(amounts[i])));
			ext1 += itemCodes[i] + "应缴金额变为:" + amounts[i] + ",";
		}
		// 计算订单总金额
		BigDecimal amount = new BigDecimal("0.00");
		BigDecimal a = null;
		List<BdSubOrder> bdSubOrders = bdSubOrderMapper.getSubOrderForOrderNo(orderNo);
		for (BdSubOrder o : bdSubOrders) {
			a = new BigDecimal(o.getPayable());
			amount = amount.add(a);
		}
		mapper.updateOrder(orderNo, String.valueOf(amount.setScale(2, RoundingMode.HALF_DOWN).doubleValue()));
		// 改变主订单金额
		ext1 = ext1.substring(0, ext1.length() - 1);
		// 新增变更记录
		BdStudentModify studentModify = new BdStudentModify();
		studentModify.setCheckType(TransferConstants.CHECK_TYPE_NEW_MODIFY);
		studentModify.setModifyType("8");
		studentModify.setLearnId(learnId);
		studentModify.setStdId(stdId);
		studentModify.setRemark(remark);
		studentModify.setFileUrl(fileUrl);
		studentModify.setExt1(ext1);
		studentModify.setIsComplete("1");
		BaseUser user = SessionUtil.getUser();
		if (null != user) {
			studentModify.setCreateUser(user.getUserName());
			studentModify.setCreateUserId(user.getUserId());
		}
		studentModify.setModifyId(IDGenerator.generatorId());
		modifyMapper.insertSelective(studentModify);
	}
}
