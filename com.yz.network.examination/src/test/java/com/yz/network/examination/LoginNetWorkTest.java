package com.yz.network.examination;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.yz.network.examination.form.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import com.yz.network.examination.app.YzNetWorkExaminationApp;
import com.yz.network.examination.format.YzNetWorkFormater;
import com.yz.network.examination.handler.NetWorkExaminationHandler.NetWorkExamHandlerResult;
import com.yz.network.examination.provider.ValidCodeNetWorkExamProvider;
import com.yz.network.examination.service.RegNetWorkExamFrmService;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.redis.RedisService;
import com.yz.util.DateUtil; 

@RunWith(SpringRunner.class)
@SpringApplicationConfiguration(classes = YzNetWorkExaminationApp.class)
public class LoginNetWorkTest {

	@Autowired
	private NetWorkExamStarter start ; 
	
	@Autowired
	private ValidCodeNetWorkExamProvider validCodeNetWorkExamParamProvider;
	
	@Autowired
	private RegNetWorkExamFrmService regFrmService;
	
	@Test
	public void loginGd()
	{
		LoginNetWorkExamForm loginNetWorkExamForm = new LoginNetWorkExamForm("2763928470236320024");
		loginNetWorkExamForm.addParam("dlfs", "1");
		loginNetWorkExamForm.addParam("id", "295937");
		loginNetWorkExamForm.addParam("pwd","yz840860");
		loginNetWorkExamForm.addValidCode();
		start.start(loginNetWorkExamForm);
	}
	
	@Test
	public void sendSmsCode()
	{
		GetSmsCodeNetWorkForm smsCodeForm = new GetSmsCodeNetWorkForm("100009");
		smsCodeForm.setNeedLogin(true);
		smsCodeForm.addParam("lxsj","13528430160");
		smsCodeForm.addParam("yzbj","1");
		start.start(smsCodeForm);
	}
	
	@Test
	public void BindMobile()
	{
		BindMobileNetWorkExamForm bindForm=new BindMobileNetWorkExamForm("100009");
		bindForm.setNeedLogin(true);
		bindForm.addParam("lxsj","13528430160");
		bindForm.addParam("sjyzm","051037");
		bindForm.addParam("yzbj","1");
		start.start(bindForm);
		NetWorkExamHandlerResult result = bindForm.getValue();
		System.out.println(result.getResult());
		
	}

	public void testDiplomaNetWorkForm()
	{
		DiplomaNetWorkForm diplomaNetWorkForm = new DiplomaNetWorkForm("100052");
		diplomaNetWorkForm.addParam("ybmh", "100052");
		diplomaNetWorkForm.addParam("xm", "张彩华");
		diplomaNetWorkForm.addParam("zjh","441881198410238523");   
		diplomaNetWorkForm.addParam("xjxl","1");   
		start.start(diplomaNetWorkForm);
	}
	
	@Test
	public void toPay()throws Exception
	{ 
		ToPayNetWorkExamForm toPayNetWorkExamForm = new ToPayNetWorkExamForm("100052");
		start.start(toPayNetWorkExamForm); 
		System.in.read();
	}
	
	@Test
	public void toReg(){
		String idCard = "445224199605081560w";
		Map<String, String> regMap = regFrmService.getStudentNetWorkInfo(idCard);
		if(null != regMap){
			RegNetWorkExamForm regForm = new RegNetWorkExamForm(regMap.get("learn_id"));
			//regForm.addParam(regMap);
			regForm.addParam("pwd",YzNetWorkFormater.pwd.formater(idCard, null));
			//start.start(regForm);
		}else{
			System.out.println("没有学业信息"); 
		}
	}

	@Test
	public void getBaseInfo(){

		GetBaseInfoNetWorkExamForm baseInfoNetWorkExamForm = new GetBaseInfoNetWorkExamForm("153535870241844650");
		baseInfoNetWorkExamForm.setNeedLogin(true);
		start.start(baseInfoNetWorkExamForm);
	}
	
}
