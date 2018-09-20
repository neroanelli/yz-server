package com.yz.edu.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yz.constants.FinanceConstants;
import com.yz.edu.cmd.UserAccountCmd;
import com.yz.edu.domain.engine.DomainExecEngine;
import com.yz.job.app.MyTaskApplication;
import com.yz.util.JsonUtil;

/**
 * 规则匹配单元测试
 * 
 * @ClassName: RechargeTest
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhanggh
 * @date 2018年5月29日
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyTaskApplication.class)
public class ChangeAccountTest {
	private static Logger logger = LoggerFactory.getLogger(ChangeAccountTest.class);
	
	@Autowired
	private DomainExecEngine domainExecEngine;
	
	@Test
	public void Test() {
		// 发送指令
		List<UserAccountCmd> cmdInfos = new ArrayList<>();
		UserAccountCmd cmd = new UserAccountCmd();
		cmd.setAccountType(FinanceConstants.ACC_TYPE_ZHIMI);
		cmd.setUserId("153087718672006504");
		cmd.setAmount(new BigDecimal("20"));
		cmd.setType(FinanceConstants.ACC_ACTION_IN);
		cmd.setRemark("单元测试" + ",获得" + "20" + "智米");
		cmd.setMappingId("88888888888");
		cmdInfos.add(cmd);
		logger.info("发送赠送智米指令:{},userId:{}", JsonUtil.object2String(cmd), "153087718672006504");
		domainExecEngine.executeCmds(cmdInfos, null);
	}
}
