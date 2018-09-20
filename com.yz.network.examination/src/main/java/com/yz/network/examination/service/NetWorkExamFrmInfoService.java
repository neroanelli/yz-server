package com.yz.network.examination.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.dao.NetworkExamFrmInfoMapper;
import com.yz.network.examination.form.EducationCheckExamForm;
import com.yz.network.examination.form.FindApplyNoNetWorkForm;
import com.yz.network.examination.form.LoginNetWorkExamForm;
import com.yz.network.examination.form.RegNetWorkExamForm;
import com.yz.network.examination.format.YzNetWorkFormater;
import com.yz.network.examination.handler.NetWorkExaminationHandler.NetWorkExamHandlerResult;
import com.yz.network.examination.model.YzNetworkExamFrmAttr;
import com.yz.network.examination.model.YzNetworkExamInfoFrm;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service(value = "netWorkExamFrmInfoService")
public class NetWorkExamFrmInfoService {

	private static Logger logger = LoggerFactory.getLogger(NetWorkExamFrmInfoService.class);

	@Autowired
	private NetworkExamFrmInfoMapper networkExamFrmInfoMapper;

	@Autowired
	private NetWorkExamStarter start;

	@Autowired
	private EducationCheckExamFrmService service;

	/**
	 * @desc step1: 保存网报表单 step2: 保存网报表单属性列表
	 * @param frm
	 */
	public void saveNetWorkExamFrm(YzNetworkExamInfoFrm frm) {
		this.networkExamFrmInfoMapper.saveNetworkExamFrm(frm); // 保存表单信息
		List<YzNetworkExamFrmAttr> attrs = frm.getFrmAttrs();
		this.networkExamFrmInfoMapper.saveNetworkExamFrmAttrs(attrs);
		logger.info("saveNetWorkExamFrm.frm:{}", JsonUtil.object2String(frm));
	}

	/**
	 * 查询数据
	 * 
	 * @param idCard
	 * @return
	 */
	public List<Map<String, String>> findNetWorkList(String idCard) {
		List<Map<String, String>> releaseList = networkExamFrmInfoMapper.getNetWorkList(idCard);
		return releaseList;
	}

	// 数据查询，判断是否存在待网报列表 如果存在，判断是否存在记录，如果存在记录，同步网报状态
	// 如果待网报表存在数据，提交网报数据，如果不存在，手动构建数据，匹配专业。
	// 同步提交到网报，等待网报结果
	public Object exceNetWork(String learnId) {
		String data = "已经停止网报了";
		// String dataCount = networkExamFrmInfoMapper.getcount(learnId);
		// // 已经存在待网报数据
		// if (StringUtil.isNotBlank(dataCount) && Integer.valueOf(dataCount) >
		// 0) {
		//
		// String netRegist =
		// networkExamFrmInfoMapper.getNetRegistCount(learnId);
		// // 判断是否存在网报记录
		// if (StringUtil.isNotBlank(netRegist) && Integer.valueOf(netRegist) >
		// 0) {
		// data = "此学籍已网报";
		// } else {
		// // 调用找回预报名号，看学员是否存在预报名号
		// FindApplyNoNetWorkForm applyForm = new
		// FindApplyNoNetWorkForm(learnId);
		// Map<String, String> map =
		// networkExamFrmInfoMapper.getStudentInfo(learnId);
		// // 解析出密码和考试县区代码
		// applyForm.addParam("pwd", NetWorkExamConstants.NETTO_PWD_PREFIX
		// + YzNetWorkFormater.pwd.formater(map.get("id_card"), null));
		// applyForm.addParam("xqshort", map.get("ta_short"));
		// applyForm.addParam("zjh", map.get("id_card"));
		// applyForm.addParam("xm", map.get("std_name"));
		// start.start(applyForm);
		// if (applyForm.getValue().isOk()) {
		// // 已存在网报数据
		// data = "此学籍已网报";
		// } else {
		// // 开始网报
		// Map<String, String> r =
		// networkExamFrmInfoMapper.getRegReportData(learnId);
		// // 处理毕业证书问题
		// if (r.get("kslbdm").equals("5")) {
		// if (r.get("byzshm").length() > 8) {
		// String byzshm = r.get("byzshm");
		// r.remove("byzshm");
		// r.put("byzshm", StringUtil.substring(byzshm, byzshm.length() - 5,
		// byzshm.length()));
		// }
		// if (r.get("byxx").length() > 8) {
		// String byxx = r.get("byxx");
		// r.remove("byxx");
		// r.put("byxx", StringUtil.substring(byxx, byxx.length() - 8,
		// byxx.length()));
		// }
		// }
		// logger.error("getUnvsDataForExam:{}", JsonUtil.object2String(r));
		// NetWorkExamHandlerResult result = regNetWork(r);
		// if (result.isOk()) {
		// data = "学员网报成功!";
		// } else {
		// data = "网报失败:" + result.getResult().toString();
		// }
		// }
		// }
		// } else
		//
		// {
		// // 不存在预网报数据，开始组装网报数据
		// Map<String, String> r =
		// networkExamFrmInfoMapper.getStdReportData(learnId);
		// logger.error("getStdReportData:{}", JsonUtil.object2String(r));
		// if (r != null) {
		// // 组合志愿代码
		// Map<String, String> objCode =
		// networkExamFrmInfoMapper.getUnvsDataForExam(r.get("unvs_code"),
		// r.get("pfsn_level"), r.get("pfsn_name"), r.get("ta_name"));
		// if (objCode == null || objCode.isEmpty()) {
		// objCode =
		// networkExamFrmInfoMapper.getUnvsDataForExam(r.get("unvs_code"),
		// r.get("pfsn_level"),
		// r.get("pfsn_name"), "");
		// }
		// if (objCode == null || objCode.isEmpty()) {
		// data = "未匹配到学员报考志愿!";
		// } else {
		// logger.error("getUnvsDataForExam:{}",
		// JsonUtil.object2String(objCode));
		// r.putAll(objCode);
		// logger.error("regNetWork:{}", JsonUtil.object2String(r));
		// // 保存数据达到reportData
		// networkExamFrmInfoMapper.addNetReportData(r);
		// // 发起网报
		// NetWorkExamHandlerResult result = regNetWork(r);
		// logger.error("getUnvsDataForExam:{}",
		// JsonUtil.object2String(result));
		// if (result.isOk()) {
		// data = "学员网报成功!";
		// } else {
		// data = "网报失败:" + result.getResult().toString();
		// }
		// // r.put("zsbpc1bkyx1", objCode.get(""));//第一志愿院校代码
		// // r.put("first_unvs_name", "");//第一志愿院校名称
		// // r.put("zsbpc1bkyx1zy1", "");//第一志愿专业代码
		// // r.put("first_pfsn_name", "");//第一志愿专业名称
		// // r.put("first_pfsn_level", "");//第一志愿报考层次
		// // r.put("zsbpc1bkyx2", "");//第二志愿院校代码
		// // r.put("second_unvs_name", "");//第二志愿院校代码
		// // r.put("zsbpc1bkyx2zy1", "");//第二志愿院校代码
		// // r.put("second_pfsn_name", "");//第二志愿院校代码
		//
		// }
		// } else {
		// data = "学员数据异常!";
		// }
		// }
		return data;
	}

	public Object findNetWorkData(String learnId) {
		Map<String, String> r = null;
		String dataCount = networkExamFrmInfoMapper.getcount(learnId);
		// 已经存在待网报数据
		if (StringUtil.isNotBlank(dataCount) && Integer.valueOf(dataCount) > 0) {

			String netRegist = networkExamFrmInfoMapper.getNetRegistCount(learnId);
			// 判断是否存在网报记录
			if (StringUtil.isNotBlank(netRegist) && Integer.valueOf(netRegist) > 0) {
			} else {
				// 调用找回预报名号，看学员是否存在预报名号
				FindApplyNoNetWorkForm applyForm = new FindApplyNoNetWorkForm(learnId);
				Map<String, String> map = networkExamFrmInfoMapper.getStudentInfo(learnId);
				// 解析出密码和考试县区代码
				applyForm.addParam("pwd", NetWorkExamConstants.NETTO_PWD_PREFIX
						+ YzNetWorkFormater.pwd.formater(map.get("id_card"), null));
				applyForm.addParam("xqshort", map.get("ta_short"));
				applyForm.addParam("zjh", map.get("id_card"));
				applyForm.addParam("xm", map.get("std_name"));
				start.start(applyForm);
				if (applyForm.getValue().isOk()) {
					// 已存在网报数据
				} else {
					// 开始网报
					r = networkExamFrmInfoMapper.getRegReportData(learnId);
					// NetWorkExamHandlerResult result = regNetWork(r);
					// if (result.isOk()) {
					// data = "学员网报成功!";
					// } else {
					// data = "网报失败:" + result.getResult().toString();
					// }
				}
			}
		} else {
			// 不存在预网报数据，开始组装网报数据
			r = networkExamFrmInfoMapper.getStdReportData(learnId);
			logger.error("getStdReportData:{}", JsonUtil.object2String(r));
			if (r != null) {
				// 组合志愿代码
				Map<String, String> objCode = networkExamFrmInfoMapper.getUnvsDataForExam(r.get("unvs_code"),
						r.get("pfsn_level"), r.get("pfsn_name"), r.get("ta_name"));
				if (objCode == null || objCode.isEmpty()) {
					objCode = networkExamFrmInfoMapper.getUnvsDataForExam(r.get("unvs_code"), r.get("pfsn_level"),
							r.get("pfsn_name"), "");
				}
				if (objCode == null || objCode.isEmpty()) {
					// data = "未匹配到学员报考志愿!";
				} else {
					logger.error("getUnvsDataForExam:{}", JsonUtil.object2String(objCode));
					r.putAll(objCode);
					logger.error("regNetWork:{}", JsonUtil.object2String(r));
					// 保存数据达到reportData
					networkExamFrmInfoMapper.addNetReportData(r);
					// 发起网报
					// NetWorkExamHandlerResult result = regNetWork(r);
					// logger.error("getUnvsDataForExam:{}",
					// JsonUtil.object2String(result));
					// if (result.isOk()) {
					// data = "学员网报成功!";
					// } else {
					// data = "网报失败:" + result.getResult().toString();
					// }
					// r.put("zsbpc1bkyx1", objCode.get(""));//第一志愿院校代码
					// r.put("first_unvs_name", "");//第一志愿院校名称
					// r.put("zsbpc1bkyx1zy1", "");//第一志愿专业代码
					// r.put("first_pfsn_name", "");//第一志愿专业名称
					// r.put("first_pfsn_level", "");//第一志愿报考层次
					// r.put("zsbpc1bkyx2", "");//第二志愿院校代码
					// r.put("second_unvs_name", "");//第二志愿院校代码
					// r.put("zsbpc1bkyx2zy1", "");//第二志愿院校代码
					// r.put("second_pfsn_name", "");//第二志愿院校代码

				}
			} else {
				// data = "学员数据异常!";
			}
		}
		return r;
	}

	/**
	 * 学历校验
	 * 
	 * @param map
	 * @return
	 */
	public String checkExamination(String learnId) {
		String data = "学历校验失败";
		Map<String, String> r = networkExamFrmInfoMapper.getNetWorkDucation(learnId);
		if (r == null || r.isEmpty()) {
			return "该学员未网报或数据未同步!";
		}
		if (r != null && r.get("education_check").equals("1")) {
			return "该学员已通过学历鉴定!";
		}
		if (r.get("education_check").equals("1")) {
			data = "学历校验成功!";
			service.educationCheck(r.get("learnId"), "1");
		} else {
			LoginNetWorkExamForm loginNetWorkExamForm = new LoginNetWorkExamForm(learnId);
			loginNetWorkExamForm.addParam("id", r.get("username"));
			loginNetWorkExamForm.addParam("pwd", r.get("password"));
			loginNetWorkExamForm.addParam("dlfs", "1");
			loginNetWorkExamForm.addValidCode();
			start.start(loginNetWorkExamForm);
			if (loginNetWorkExamForm.getValue().isOk()) {
				if (StringUtil.equalsIgnoreCase(loginNetWorkExamForm.getValue().getResult().toString(), "confirm")) {
					service.educationCheck(r.get("learnId"), "1");
				} else {
					EducationCheckExamForm form = new EducationCheckExamForm(r.get("learnId"));
					form.addParam("ybmh", r.get("username"));
					form.addParam("xjxl", "1");
					form.addParam("zjh", r.get("zjdm"));
					form.addParam("xm", r.get("xm"));
					start.start(form);
					if (form.getValue() != null && form.getValue().isOk()) {
						data = "学历校验成功!";
					} else {
						if (form.getValue() != null && form.getValue().getResult() != null)
							data = data + ",原因:" + form.getValue() == null ? ""
									: form.getValue().getResult().toString();
					}
				}
			} else {
				data += "," + loginNetWorkExamForm.getValue().getResult().toString();
			}
		}
		return data;
	}

	public String saveNetWorkData(Map<String, Object> map) {
		String data = "保存成功!";
		if (map == null || map.isEmpty()) {
			data = "数据有误，保存失败!";
		} else {
			networkExamFrmInfoMapper.upNetReportData(map);
		}
		return data;
	}

	/**
	 * 网报
	 * 
	 * @param learnId
	 * @return
	 */
	private NetWorkExamHandlerResult regNetWork(Map<String, String> r) {
		RegNetWorkExamForm regForm = new RegNetWorkExamForm(r.get("learn_id"));
		regForm.addParam(r);
		regForm.delParam("pwd");
		regForm.addParam("pwd",
				NetWorkExamConstants.NETTO_PWD_PREFIX + YzNetWorkFormater.pwd.formater(r.get("zjdm"), null));
		regForm.addParam("hidden_yzdm", "1"); // 写死
		regForm.addParam("czbj", "0");
		String txdz = r.get("txdz");
		if (StringUtil.equalsIgnoreCase(r.get("zsbpc1bkyx1"), "11540")) { // 广金特殊处理
			txdz = "036";
			txdz = txdz + (txdz.length() > 5 ? txdz.substring(txdz.length() - 5, txdz.length()) : txdz);
			txdz = txdz + NetWorkExamConstants.NETTO_ADDRESS_SUFFIX;
			regForm.delParam("txdz");
			regForm.addParam("txdz", txdz);
		} else {
			txdz = (txdz.length() > 5 ? txdz.substring(txdz.length() - 5, txdz.length()) : txdz);
			txdz = txdz + NetWorkExamConstants.NETTO_ADDRESS_SUFFIX;
			regForm.delParam("txdz");
			regForm.addParam("txdz", txdz);
		}
		logger.error("regNetWork.getParams:{}", JsonUtil.object2String(regForm.getParams()));
		start.start(regForm);
		return regForm.getValue();
	}

}
