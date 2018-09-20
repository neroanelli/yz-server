package com.yz.network.examination.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yz.network.examination.croe.util.SessionUtil;
import com.yz.network.examination.form.ToPayNetWorkExamForm;
import com.yz.network.examination.form.ToWeChatPayNetWorkExamForm;
import com.yz.network.examination.service.NetWorkExamFrmInfoService;
import com.yz.network.examination.service.RegNetWorkExamFrmService;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.util.JsonUtil;

@Controller
@RequestMapping("/regNet")
public class RegNetWorkExamController {

	@Autowired
	private RegNetWorkExamFrmService regService;

	@Autowired
	private NetWorkExamStarter netWorkExamStarter;

	@Autowired
	private NetWorkExamFrmInfoService queueService;

	@RequestMapping("/findNetWorkData")
	@ResponseBody
	public Object findNetWorkData(@RequestParam(name = "learnId", required = true) String learnId) {
		return queueService.findNetWorkData(learnId);
	}

	@RequestMapping("/saveNetWorkData")
	@ResponseBody
	public Object saveNetWorkData(@RequestParam(name = "netWork", required = true) Object netWork) {
		Map<String, Object> map = null;
		try {
			JSONObject object=JSONObject.parseObject(netWork.toString());
			map=JsonUtil.object2Map(object);
		} catch (Exception e) {

		}
		if (map == null || map.isEmpty()) {
			return "数据保存出错!";
		}
		return queueService.saveNetWorkData(map);
	}
	
	/**
	 * 学历校验
	 * @param learnId
	 * @return
	 */
	@RequestMapping("/checkExamination")
	@ResponseBody
	public Object checkExamination(@RequestParam(name = "learnId", required = true) String learnId) {
		
		return queueService.checkExamination(learnId);
	}
	
	/**
	 * 查询所有的网报信息
	 * @param idCard
	 * @return
	 */
	@RequestMapping("/findNetWorkList")
	@ResponseBody
	public Object findNetWorkList(@RequestParam(name = "idCard", required = true) String idCard) {
		List<Map<String, String>> infos = queueService.findNetWorkList(idCard);
		return infos;
	}

	@RequestMapping("/exceNetWork")
	@ResponseBody
	public Object exceNetWork(@RequestParam(name = "learnId", required = true) String learnId) {
		return queueService.exceNetWork(learnId);
	}

	/**
	 * 根据身份证号获取网报信息 网报注册
	 * 
	 * @param idCard
	 * @return
	 */
	@RequestMapping("/loginNetWork")
	@ResponseBody
	public Object loginNetWork(@RequestParam(name = "idCard", required = true) String idCard,
			@RequestParam(name = "mobile", required = true) String mobile, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		return regService.loginNetWork(idCard, mobile);
	}

	@RequestMapping("/loginHidden")
	@ResponseBody
	public Object loginNetWebByLearnId(@RequestParam(name = "learnId", required = true) String learnId,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		regService.loginNetWebByLearnId(learnId);
		return "SUCCESS";
	}

	/**
	 * 根据身份证号获取网报信息 网报注册
	 * 
	 * @param idCard
	 * @return
	 */
	@RequestMapping("/getNetWorkInfo")
	@ResponseBody
	public Object getNetWorkInfo(@RequestParam(name = "learnId", required = true) String learnId) throws IOException {
		return regService.getNetWorkInfo(learnId);
	}

	/**
	 * 获取绑定手机验证码
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/sendNetWorkCode")
	@ResponseBody
	public Object sendNetWorkCode(String learnId, String mobile) throws ServletException, IOException {
		return regService.sendNetWorkCode(learnId, mobile);
	}

	/**
	 * 绑定手机号
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/bindMobile")
	@ResponseBody
	public Object bindMobile(String learnId, String mobile, String valicode) throws ServletException, IOException {
		return regService.bindMoble(learnId, mobile, valicode);
	}

	/**
	 * 得到预报名号信息
	 * 
	 * @param learnId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/getRegNetInfo")
	@ResponseBody
	public Object getRegNetInfo(@RequestParam(name = "learnId", required = true) String learnId) throws IOException {
		return regService.getStudentSceneRegisterByLearnId(learnId);
	}

	/**
	 * 得到网报步骤状态
	 * 
	 * @param learnId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/getRegNetStatus")
	@ResponseBody
	public Object getNetReportDataStutus(@RequestParam(name = "learnId", required = true) String learnId)
			throws IOException {
		return regService.getNetReportDataStutus(learnId);
	}

	/**
	 * 去支付
	 * 
	 * @param learnId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/toPay")
	public void toPay(@RequestParam(name = "learnId", required = true) String learnId, HttpServletResponse response)
			throws IOException {
		ToPayNetWorkExamForm frm = new ToPayNetWorkExamForm(learnId);
		netWorkExamStarter.start(frm);
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.getWriter().write(frm.getValue().getResult().toString());
		response.getWriter().flush();
	}

	/**
	 * 去微信支付
	 * 
	 * @param learnId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/toWeChatPay")
	@ResponseBody
	public Object toWeChatPay(@RequestParam(name = "learnId", required = true) String learnId,
			HttpServletResponse response) throws IOException {
		ToWeChatPayNetWorkExamForm frm = new ToWeChatPayNetWorkExamForm(learnId);
		netWorkExamStarter.start(frm);
		Map<String, Object> result = Maps.newHashMap();
		result.put("status", frm.getValue().isOk());
		result.put("result", frm.getValue().getResult());
		return result;
	}

	/**
	 * 获取登陆手机验证码
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/sendLoginCode")
	@ResponseBody
	public Object sendLoginCode(@RequestParam(name = "mobile", required = true) String mobile)
			throws ServletException, IOException {
		regService.valicode(mobile);
		return "SUCCESS";
	}

	/**
	 * 校验验证码
	 * 
	 * @param mobile
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/checkLoginCode")
	@ResponseBody
	public Object checkLoginCode(@RequestParam(name = "mobile", required = true) String mobile,
			@RequestParam(name = "validCode", required = true) String validCode) throws ServletException, IOException {
		regService.checkLoginCode(mobile, validCode);
		return "SUCCESS";
	}

	/**
	 * 获取报考基本信息（手机绑定，缴费，图片采集）
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/getBaseInfoStatus", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getBaseInfoStatus(String learnId) throws ServletException, IOException {
		return regService.getBaseInfoStatus(learnId);
	}

	/**
	 * 退出
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/loginOut")
	@ResponseBody
	public Object loginOut(String learnId) throws IOException {
		SessionUtil.clearUser(learnId);
		return "SUCCESS";
	}

	/**
	 * 单个学员网报注册
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/regNetByLearnId")
	@ResponseBody
	public Object regNetByLearnId(String learnId) throws IOException {
		regService.submitRegNetWorkInfo(learnId);
		return "SUCCESS";
	}

}
