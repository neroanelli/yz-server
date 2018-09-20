package com.yz.network.examination.service;

import java.net.URLDecoder;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.network.examination.croe.util.SessionUtil;
import com.yz.network.examination.dao.NetworkExamFrmLogMapper;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.handler.NetWorkExaminationHandler.NetWorkExamHandlerResult;
import com.yz.network.examination.model.YzNetworkExamLogFrm;
import com.yz.network.examination.vo.LoginUser;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;
import com.yz.util.OSUtil;
import com.yz.util.StringUtil;

@Service(value = "netWorkExamFrmLogService")
public class NetWorkExamFrmLogService {

	private static Logger logger = LoggerFactory.getLogger(NetWorkExamFrmLogService.class);

	@Autowired
	private NetworkExamFrmLogMapper networkExamFrmMapper;

	/**
	 * @desc 记录网报表单日志
	 * @param frm
	 *            网报表单
	 */
	public void logNetWorkExamFrm(BaseNetWorkExamForm frm, int code) {
		NetWorkExamHandlerResult result = frm.getValue();
		long time = (new Date().getTime() - frm.getStartTime().getTime());
		YzNetworkExamLogFrm networkExamFrm = new YzNetworkExamLogFrm();
		HttpUriRequest req = frm.getRequest();
		networkExamFrm.setFrmAddr(OSUtil.getIp());
		networkExamFrm.setFrmId(frm.getId());
		networkExamFrm.setFrmName(frm.getName());
		networkExamFrm.setFrmType(req.getMethod());
		networkExamFrm.setFrmAction(req.getURI().toString());
		networkExamFrm.setFrmKey(frm.getFrmKey());
		networkExamFrm.setFrmNo(frm.getFrmNo());
		networkExamFrm.setFrmTime((int) time);
		networkExamFrm.setFrmCode(String.valueOf(code));
		if (req instanceof HttpGet) {
			networkExamFrm.setFrmParam(req.getURI().getPath());
		} else if (req instanceof HttpPost) {
			HttpPost http = (HttpPost) req;
			try {
				networkExamFrm.setFrmParam(URLDecoder.decode(IOUtils.toString(http.getEntity().getContent(),"gbk"), "gbk"));
			} catch (Exception e) {
				logger.error("IOUtils.toString.error:{}", ExceptionUtil.getStackTrace(e));
			}
		}
		if (result != null) {
			networkExamFrm.setFrmResult(result.isOk() ? 0 : 1);
			networkExamFrm.setFrmErrmsg(result.isOk()?"":StringUtil.obj2String(result.getResult()));
		}
		networkExamFrm.setFrmDate(frm.getStartTime());
		LoginUser user = SessionUtil.getUser();
		if (user != null) {
			networkExamFrm.setFrmUserId(user.getUserId());
			networkExamFrm.setFrmUserName(user.getUserName());
		}
		logger.info("NetWorkExamFrmService.logNetWorkExamFrm.networkExamFrm:{}",
				JsonUtil.object2String(networkExamFrm));
		networkExamFrmMapper.saveNetworkExamFrm(networkExamFrm);
	}

}
