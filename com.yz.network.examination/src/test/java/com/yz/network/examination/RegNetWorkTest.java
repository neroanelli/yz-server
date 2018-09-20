package com.yz.network.examination;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.yz.network.examination.app.YzNetWorkExaminationApp;
import com.yz.network.examination.form.EducationCheckExamForm;
import com.yz.network.examination.form.LoginNetWorkExamForm;
import com.yz.network.examination.starter.NetWorkExamStarter;

@RunWith(SpringRunner.class)
@SpringApplicationConfiguration(classes = YzNetWorkExaminationApp.class)
public class RegNetWorkTest {

	@Autowired
	private NetWorkExamStarter start;

	// @Test
	// public void loadCheck(){
	// EducationCheckExamForm form = new
	// EducationCheckExamForm("153355109617445540");
	// form.addParam("ybmh","270553");
	// form.addParam("xjxl","1");
	// form.addParam("zjh","360731199203252933");
	// form.addParam("xm","张光红");
	// start.start(form);
	// }

	@Test
	public void loadErrorCheck() {
		LoginNetWorkExamForm loginNetWorkExamForm = new LoginNetWorkExamForm("533132");
		loginNetWorkExamForm.addParam("id", "533132");
		loginNetWorkExamForm.addParam("pwd", "yz619921");
		loginNetWorkExamForm.addParam("dlfs", "1");
		loginNetWorkExamForm.addValidCode();
		start.start(loginNetWorkExamForm);
		if (loginNetWorkExamForm.getValue().isOk()) {
			EducationCheckExamForm form = new EducationCheckExamForm("533132");
			form.addParam("ybmh", "533132");
			form.addParam("xjxl", "1");
			form.addParam("zjh", "441481198505107049");
			form.addParam("xm", "曾明苑");
			start.start(form);
		}
	}
}
