package com.yz.network.examination.cmd;

import com.yz.network.examination.aware.NetWorkFormAware;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.yz.http.interceptor.HttpInterceptor;
import com.yz.http.pool.HttpClientPool;
import com.yz.network.examination.form.YzNetWorkForm;
import com.yz.util.ApplicationContextUtil;
import com.yz.util.ExceptionUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @desc 创建熔断器命令
 * @author lingdian
 *
 */
public class NetWorkExamHystrixCommand implements NetWorkFormAware, NetWorkExamConstants {

	private Logger logger = LoggerFactory.getLogger(NetWorkExamHystrixCommand.class);

	private BaseNetWorkExamForm form;

	private List<HttpInterceptor> interceptors;

	public NetWorkExamHystrixCommand(BaseNetWorkExamForm frm, HttpInterceptor... interceptors) {
		this.form = frm;
		this.form.setStartTime(new Date());
		if (StringUtil.isBlank(form.getFrmNo()))
			this.form.setFrmNo(String.valueOf(System.currentTimeMillis()));
		this.interceptors = Lists.newArrayList(interceptors);
	}

	/**
	 * @desc 获取当前form的frmKey
	 * @return
	 */
	public String getId() {
		return this.form.getFrmKey();
	}

	/**
	 * @desc 获取当前操作是否是补偿命令
	 * @return
	 */
	public boolean isCompensateCmd() {
		return this.form.isCompensateFrm();
	}

	public YzNetWorkForm getYzNetWorkForm() {
		YzNetWorkForm frm = getFormConfig(form);
		return frm;
	}

	public void execute() {
		HttpUriRequest request = form.getRequest();
		CloseableHttpResponse response = null;
		try {
			HttpContext context = new BasicHttpContext();
			context.setAttribute("NetWorkExamForm", form);
			YzNetWorkForm frm = this.getYzNetWorkForm();
			HttpInterceptor interceptor = null;
			if (frm != null && StringUtil.isNotBlank(frm.interceptor())) {
				interceptor = ApplicationContextUtil.getBeanIgnoreEx(frm.interceptor());
				interceptors.add(interceptor);
			}
			CloseableHttpClient client = HttpClientPool.getInstance().getHttpClient(interceptors);
			response = client.execute(request, context);
		} catch (Exception e) {
			logger.error("发送Http请求出现异常:{}！", ExceptionUtil.getStackTrace(e));
		}
		// 使用finally块来关闭输入流
		finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error("发送Http请求出现异常:{}！", ExceptionUtil.getStackTrace(e));
				}
			}
		}
	}
}