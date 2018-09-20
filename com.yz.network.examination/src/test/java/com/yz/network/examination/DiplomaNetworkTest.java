package com.yz.network.examination;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;
import com.yz.network.examination.app.YzNetWorkExaminationApp;
import com.yz.network.examination.form.DiplomaNetWorkForm;
import com.yz.network.examination.form.EducationCheckExamForm;
import com.yz.network.examination.form.LoginNetWorkExamForm;
import com.yz.network.examination.form.RegNetWorkExamForm;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.util.StringUtil;

@RunWith(SpringRunner.class)
@SpringApplicationConfiguration(classes = YzNetWorkExaminationApp.class)
public class DiplomaNetworkTest {

	@Autowired
	private NetWorkExamStarter start;

	@Test
	public void testDiplomaNetwork() throws Exception {
		LoginNetWorkExamForm login = new LoginNetWorkExamForm("343585");
		login.addParam("id", "343585");
		login.addParam("pwd", "yz111111");
		login.addParam("dlfs", "1");
		login.addValidCode();
		start.start(login);
		DiplomaNetWorkForm form = new DiplomaNetWorkForm("343585");
		start.start(form);
		System.in.read();
	}

	@Test
	public void testRegGd() throws Exception {
		RegNetWorkExamForm regForm = new RegNetWorkExamForm("441881198410238523");
		String arr = FileUtils.readFileToString(new File("d://reg.txt"), "gbk");
		Lists.newArrayList(StringUtil.split(arr, "&")).parallelStream().forEach(v -> {
			regForm.addParam(StringUtil.substringBefore(v, "="), StringUtil.substringAfter(v, "="));
		});
		start.start(regForm);
		System.in.read();
	}
}
