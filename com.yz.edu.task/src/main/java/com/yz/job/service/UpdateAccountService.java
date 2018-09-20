package com.yz.job.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.constants.FinanceConstants; 
import com.yz.edu.cmd.UserAccountCmd; 
import com.yz.edu.domain.YzUserDomain;
import com.yz.generator.IDGenerator;
import com.yz.job.common.YzDomainConsumer;
import com.yz.job.common.YzTaskContext;
import com.yz.job.dao.UserRegisterMapper;
import com.yz.job.model.AtsAccount;
import com.yz.job.model.AtsAccountSerial;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;

@Service(value = YzTaskConstants.YZ_UPDATE_ACCOUNT_TASK) 
public class UpdateAccountService implements YzDomainConsumer<UserAccountCmd, YzUserDomain>{

	private Logger logger = LoggerFactory.getLogger(UpdateAccountService.class);

	@Autowired
	private UserRegisterMapper userRegisterMapper;

	public void consumer(UserAccountCmd cmd, YzUserDomain domain) {
		AtsAccount account = userRegisterMapper.getAccountInfo(domain.getUserId(), cmd.getAccountType());
		BigDecimal decimal=null;
		if(cmd.getType().equals(FinanceConstants.ACC_ACTION_IN)){
			decimal=domain.getZhimi().subtract(cmd.getAmount());
		}else{
			decimal=domain.getZhimi().add(cmd.getAmount());
		}
		AtsAccountSerial serial = new AtsAccountSerial();
		serial.setAccId(account.getAccId());
		serial.setAccType(cmd.getAccountType());
		serial.setAction(cmd.getType());
		serial.setBeforeAmount(String.valueOf(decimal));
		serial.setAmount(String.valueOf(cmd.getAmount()));
		serial.setAfterAmount(String.valueOf(domain.getZhimi()));
		serial.setUserId(domain.getUserId());
		serial.setEmpId(domain.getEmpId());
		serial.setStdId(domain.getStdId());
		serial.setExcDesc(cmd.getRemark());
		serial.setMappingId(cmd.getMappingId());
		serial.setUpdateTime(DateUtil.dateToDateStringFormat(cmd.getCreateDate()));
		serial.setAccSerialStatus(FinanceConstants.ACC_SERAIL_STATUS_SUCCESS);
		serial.setIsVisiable("1");
		serial.setAccSerialNo(IDGenerator.generatorId());
		this.userRegisterMapper.saveSerial(serial);
		logger.info("AtsAccountSerial.serial:{} ", JsonUtil.object2String(serial));
		account.setAccAmount(String.valueOf(cmd.getAmount())); 
		userRegisterMapper.updateAccount(account.getAccId(),account.getAccAmount(),cmd.getType());
		logger.info("updateAccount.account:{},变动数量:{},描述:{}", String.valueOf(domain.getZhimi()),cmd.getAmount(),cmd.getRemark());
	}
}
