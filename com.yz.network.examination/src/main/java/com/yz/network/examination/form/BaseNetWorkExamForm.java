package com.yz.network.examination.form;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yz.exception.BusinessException;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.handler.NetWorkExaminationHandler.NetWorkExamHandlerResult;
import com.yz.network.examination.param.YzNetWorkExamParam;
import com.yz.network.examination.provider.NetWorkExamDataProvider;
import com.yz.util.ApplicationContextUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @desc 网报提交数据表单
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public abstract class BaseNetWorkExamForm implements NetWorkExamConstants, java.io.Serializable,Cloneable {

	public static ApplicationContext context = ApplicationContextUtil.getApplicationContext();

	private String id; // 表单所在唯一数据标志

	private String name; // 提交的名称

	private boolean needLogin = false; // 是否需要登录

	private String domain; // form 域名 与cookie对应

	private boolean loginFrm = false; // 是否登录表单

	private boolean logFrm = true; // 表单执行是否记录日志

	private transient String frmNo; // 表单提交的批次号

	private transient Date startTime; // 表单启动时间
	
	private transient boolean compensateFrm = false; //是否补偿表单

	private Set<YzNetWorkExamParam> params; // 表单构建参数

	private String cacheKey; // 执行成功 默认的key

	private transient Object cacheValue; // 执行成功 缓存的值 ？

	private transient NetWorkExamHandlerResult value; // 返回调用的 值？

	private String frmCode; // frm表单编码

	public BaseNetWorkExamForm() {
		this.params = Sets.newHashSet();
	}

	public BaseNetWorkExamForm(String id) {
		if (StringUtil.isBlank(id)) {
			throw new BusinessException("frm.id must not null ！");
		}
		this.id = id;
		this.params = Sets.newHashSet();
	} 

	public boolean isCompensateFrm() {
		return compensateFrm;
	}

	public void setCompensateFrm(boolean compensateFrm) {
		this.compensateFrm = compensateFrm;
	}

	public String getFrmKey() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName()).append(".").append(this.getId());
		return sb.toString();
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @desc 根据key删除参数
	 * @param key
	 */
	public void delParam(String key) {
		Iterator<YzNetWorkExamParam> iter = this.params.iterator();
		while (iter.hasNext()) {
			YzNetWorkExamParam param = iter.next();
			if (StringUtil.equalsIgnoreCase(param.getName(), key)) {
				iter.remove();
			}
		}
	}

	/**
	 * @desc 根据参数key获取参数值
	 * @param key
	 * @return
	 */
	public String getParam(String key) {
		Iterator<YzNetWorkExamParam> iter = this.params.iterator();
		while (iter.hasNext()) {
			YzNetWorkExamParam param = iter.next();
			if (StringUtil.equalsIgnoreCase(param.getName(), key)) {
				return param.getValue();
			}
		}
		return null;
	}

	/**
	 * @desc 单个添加参数
	 * @param key
	 * @param value
	 */
	public void addParam(String key, NetWorkExamDataProvider<String> provider) {
		if (!this.params.contains(key)) {
			this.params.add(new YzNetWorkExamParam(key, provider.provider(this)));
		}
	}

	/**
	 * @desc 单个添加参数
	 * @param key
	 * @param value
	 */
	public void addParam(String key, Object value) {
		if (!this.params.contains(key)) {
			this.params.add(new YzNetWorkExamParam(key, value));
		}
	}

	/**
	 * @desc 批量添加参数
	 * @param params
	 */
	public void addParams(List<YzNetWorkExamParam> params) {
		this.params.addAll(params);
	}

	/**
	 * @desc 添加map参数
	 * @param param
	 */
	public void addParam(Map<String, String> param) {
		if (param != null && !param.isEmpty()) {
			param.entrySet().parallelStream().forEach(v -> addParam(v.getKey(), v.getValue()));
		}
	}

	public Set<YzNetWorkExamParam> getParams() {
		return this.params;
	}

	public void setFrmCode(String frmCode) {
		this.frmCode = frmCode;
	}

	public String getFrmCode() {
		return frmCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isNeedLogin() {
		return needLogin;
	}

	public void setNeedLogin(boolean needLogin) {
		this.needLogin = needLogin;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDomain() {
		return domain;
	}

	public boolean isLoginFrm() {
		return loginFrm;
	}

	public void setLoginFrm(boolean loginFrm) {
		this.loginFrm = loginFrm;
	}

	public boolean isLogFrm() {
		return logFrm;
	}

	public void setLogFrm(boolean logFrm) {
		this.logFrm = logFrm;
	}

	public NetWorkExamHandlerResult getValue() {
		return value;
	}

	public NetWorkExamHandlerResult setValue(NetWorkExamHandlerResult value) {
		this.value = value;
		return this.value;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public String getCacheKey() {
		if (StringUtil.isBlank(cacheKey)) {
			return this.getClass().getSimpleName();
		}
		return cacheKey;
	}

	public void setCacheValue(Object cacheValue) {
		this.cacheValue = cacheValue;
	}

	public Object getCacheValue() {
		if (cacheValue == null) {
			return this;
		}
		return cacheValue;
	}

	public void setFrmNo(String frmNo) {
		this.frmNo = frmNo;
	}

	public String getFrmNo() {
		return frmNo;
	}

	/**
	 * @desc
	 * @return
	 */
	public List<NameValuePair> wrapperParm() {
		return Lists.newArrayList(params);
	}

	/**
	 * @desc 获取HttpUriRequest
	 * @return
	 */
	public abstract HttpUriRequest getRequest();

	/**
	 * @desc 是否可以启动
	 * @return
	 */
	public abstract boolean isStart();

}
