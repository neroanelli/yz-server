package com.yz.network.examination.interceptor;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.exception.BusinessException;
import com.yz.exception.CustomException;
import com.yz.network.examination.aware.NetWorkFormAware;
import com.yz.network.examination.cmd.NetWorkExamCompensate;
import com.yz.network.examination.cmd.NetWorkExamCompensate.CompensateCallBack;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.form.LoginNetWorkExamForm;
import com.yz.network.examination.form.YzNetWorkForm;
import com.yz.network.examination.handler.NetWorkExaminationHandler;
import com.yz.network.examination.handler.NetWorkExaminationHandler.NetWorkExamHandlerResult;
import com.yz.network.examination.provider.FrmNetWorkExamDataProvider;
import com.yz.network.examination.service.NetWorkExamFrmLogService;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.redis.RedisService;
import com.yz.serializ.FstSerializer;
import com.yz.util.ApplicationContextUtil;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Component(value = NetWorkExamConstants.CORE_NETWORKEXAMINATION_INTERCEPTOR)
public class CoreNetWorkExamInterceptor implements NetWorkExamInterceptor, NetWorkFormAware {

	@Autowired
	private NetWorkExamFrmLogService netWorkExamFrmService;

	@Autowired
	private FrmNetWorkExamDataProvider frmNetWorkExamDataProvider;

	@Autowired
	private NetWorkExamStarter netWorkExamStarter;

	private FstSerializer fstSerializer = FstSerializer.getInstance();

	@Override
	/**
	 * @desc 发送http请求之前进行状态判断
	 * @step 1 判断是否需要登录 2 判断是否需要登录
	 */
	public void process(HttpRequest httpRequest, HttpContext context) throws HttpException, IOException {
		final BaseNetWorkExamForm form = (BaseNetWorkExamForm) context.getAttribute("NetWorkExamForm");
		if (form != null && form.isStart()) {
			HttpRequestWrapper request = (HttpRequestWrapper) httpRequest;
			if (form.isNeedLogin()) // 当前表单需要登录？
			{ // 当前学前id是否存在cookie信息
				setLoginInfo(form, httpRequest);
			}
			URI uri = request.getURI();
			if (request.getOriginal() instanceof HttpPost) {
				HttpPost http = (HttpPost) request.getOriginal();
				logger.info("param:{};path:{}", IOUtils.toString(http.getEntity().getContent()), uri.getPath());
			} else {
				logger.info("host:{};path:{}", uri.getHost(), uri.getPath());
			}
			return;
		}
		NetWorkExamHandlerResult result = form.getValue();
		if (result != null) {
			NetWorkExamCompensate cmd = ApplicationContextUtil.getBeanIgnoreEx(result.getCompensateCmd());
			if (cmd != null) {
				cmd.doCompensate(form, new CompensateCallBack() {
					@Override
					public void callBack(BaseNetWorkExamForm frm) {
						setLoginInfo(form, httpRequest);
					}
				});
				return;
			}
		}
		throw new CustomException((form == null ? "" : form.getName()) + "form表单启动失败！");
	}

	@Override
	public void process(HttpResponse response, HttpContext context)
			throws HttpException, IOException, BusinessException {
		int code = response.getStatusLine().getStatusCode();
		BaseNetWorkExamForm form = (BaseNetWorkExamForm) context.getAttribute("NetWorkExamForm");
		YzNetWorkForm conf = getFormConfig(form);
		try {
			if (code == 200) {
				if (conf != null && StringUtil.isNotBlank(conf.handler())) {
					NetWorkExaminationHandler handler = ApplicationContextUtil.getBeanIgnoreEx(conf.handler());
					if (handler != null) {
						NetWorkExamHandlerResult result = handler.handler(form, response.getEntity());
						logger.info("frm:{}.result:{}", JsonUtil.object2String(form), JsonUtil.object2String(result));
						if (result != null)// 处理成功？ 将当前frm存储 // 方便当前业务快速恢复？
						{
							if (result.isOk()) {
								if (false && conf.persistenceFrm()) {
									RedisService.getRedisService().hsetarr(form.getId(), form.getCacheKey(),
											fstSerializer.serialize(form.getCacheValue()));
									
								}
							} else // 处理失败？
							{
								doCompensate(form, result.getCompensateCmd(),result.getCompensateCallBack());
							}
						}
					}
				}
			} else {
				if (conf != null) {
					doCompensate(form, conf.compensateCmd(),null);
				}
			}
		} catch (Exception ex) {
			form.getValue().setOk(false);
			form.getValue().setResult(ExceptionUtil.getStackTrace(ex));
		} finally {
			if (form.isLogFrm()) {
				netWorkExamFrmService.logNetWorkExamFrm(form, code); // 记录网报表单
			}
		}

	}

	/**
	 * @desc 启动补偿命令
	 * @param frm
	 *            具体的补偿表单
	 * @parm compensateCmd 具体的补偿指令
	 */
	private void doCompensate(BaseNetWorkExamForm frm, String compensateCmd,CompensateCallBack call) {
		if (StringUtil.isNotBlank(compensateCmd)) {
			NetWorkExamCompensate netWorkExamCompensateCmd = ApplicationContextUtil.getBeanIgnoreEx(compensateCmd);
			if (netWorkExamCompensateCmd != null) {
				frm.setCompensateFrm(true);
				netWorkExamCompensateCmd.doCompensate(frm,call);
			}
		}
	}

	/**
	 * 
	 * @param form
	 * @param httpRequest
	 */
	private void setLoginInfo(BaseNetWorkExamForm form, HttpRequest httpRequest) {
		if (form.isNeedLogin()) // 当前表单需要登录？
		{ // 当前学前id是否存在cookie信息
			if (RedisService.getRedisService().hexistsarr(form.getId(), "networkCookie"))// 不存在cookie信息
			{
				String cookies = RedisService.getRedisService().hgetarr(form.getId(), "networkCookie", String.class);
				logger.info("frm.id:{},frm.name:{},cookies:{}",form.getId(),form.getName(),cookies); 
				httpRequest.setHeader("Cookie", cookies);
				return;
			}
			logger.info("frm.id:{},frm.name:{}",form.getId(),form.getName()); 
		    loginCompensate(form);
		}
	}

	/**
	 * @desc 登录补偿操作
	 * @param login
	 * 
	 */
	private void loginCompensate(BaseNetWorkExamForm frm) {
		LoginNetWorkExamForm loginNetWorkExamForm = new LoginNetWorkExamForm(frm.getId());
		YzNetWorkForm yzNetWorkForm = getFormConfig(frm);
		if (yzNetWorkForm.syncCompensate()) {
			loginNetWorkExamForm = (LoginNetWorkExamForm) frmNetWorkExamDataProvider.provider(loginNetWorkExamForm);
			loginNetWorkExamForm.addValidCode();
			netWorkExamStarter.start(loginNetWorkExamForm);
			return;
		}
		RedisService.getRedisService().lpush(NetWorkExamConstants.NETWORKEXAMINATION_COMPENSATE_QUEUE,
				JsonUtil.object2String(frm));
	}

	@Override
	public int getSeq() {
		return 1;
	}

}
