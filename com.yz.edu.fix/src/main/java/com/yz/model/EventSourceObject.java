package com.yz.model;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.alibaba.fastjson.JSONObject;
import com.yz.constant.ObservatoryStarConstant;
import com.yz.edu.cmd.BaseCommand;
import com.yz.edu.consumer.DomainConsumeVo;
import com.yz.edu.domain.YzBaseDomain;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @desc 版本溯源
 * @author Administrator
 *
 */
public class EventSourceObject extends BaseEsObject {

	private String batchNo; // 批号

	private String appName; // 应用名称

	private String cmdId; // 聚合根

	private String name;// cmd -> 名称

	private BaseCommand cmd;

	private YzBaseDomain domain;

	/**
	 * @desc 将cunsumerVo对象封装成EventSourceObj对象
	 * @param consumerVo
	 * @return
	 */
	public static EventSourceObject wrapEventSourceObject(DomainConsumeVo consumerVo) {
		EventSourceObject obj = new EventSourceObject();
		BaseCommand cmd = consumerVo.getCmd();
		YzBaseDomain domain = consumerVo.getDomain();
		obj.setName(cmd.getClass().getName());
		obj.setBatchNo(consumerVo.getTraceId());
		obj.setAppName(consumerVo.getAppName());
		obj.setCmdId(String.valueOf(cmd.getId()));
		obj.setCmd(cmd);
		obj.setDomain(domain);
		return obj;
	}

	/**
	 * @desc 讲对象转化为XContentBuilder
	 * @return
	 */
	public XContentBuilder toXContentBuilder() {
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
			builder.field("appName", appName);
			builder.field("cmdId", cmdId);
			builder.field("batchNo", batchNo);
			builder.field("name", name);
			builder.field("cmd", warpperCommand(cmd));
			builder.field("domain", warpperDomain(domain));
			builder.endObject();
			return builder;
		} catch (Exception e) {
			logger.error("toXContentBuilder.error:{}", e);
			return null;
		}
	}

	
	
	/**
	 * @desc 将cmd对象封装成Json对象？
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
	private JSONObject warpperDomain(YzBaseDomain domain) throws Exception {
		JSONObject object = new JSONObject();
		object.put("attr", JsonUtil.object2String(domain));
		return object;
	}
	
	/**
	 * @desc 将cmd对象封装成Json对象？
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
	private JSONObject warpperCommand(BaseCommand cmd) throws Exception {
		JSONObject object = new JSONObject();
		object.put("topic", cmd.getTopic());
		object.put("createDate", DateUtil.formatDate(cmd.getCreateDate(), DateUtil.YYYYMMDDHHMMSS_SPLIT));
		object.put("addr", cmd.getAddr());
		object.put("status", cmd.getStatus());
		object.put("version", cmd.getVersion());
		object.put("step", cmd.getStep());
		object.put("errorCode", cmd.getErrorCode());
		object.put("remark", cmd.getDomainCls().getName() + "." + cmd.getMethodName());
		JSONObject attr = new JSONObject();
		BeanInfo beanInfo = Introspector.getBeanInfo(cmd.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			// 过滤class属性
			if (!key.equals("class") && !key.equals("alarm") && !object.containsKey(key)) 
			{
				if(!StringUtil.equalsIgnoreCase("fstSerializer", key)&&!StringUtil.equalsIgnoreCase("domainExecHooks", key))
				{
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(cmd);
					if (value instanceof Date) {
						value = DateUtil.formatDate((Date) value, DateUtil.YYYYMMDDHHMMSS_SPLIT);
					}
					attr.put(key, value);
				}
			}
		}
		object.put("attr", JsonUtil.object2String(attr));
		return object;
	}

	@Override
	public String getId() {
		return StringUtil.UUID();
	}

	@Override
	public String indexName() {
		return ObservatoryStarConstant.DOMAIN_EVENT_SOURCE;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppName() {
		return appName;
	}

	public void setCmdId(String cmdId) {
		this.cmdId = cmdId;
	}

	public String getCmdId() {
		return cmdId;
	}

	public void setCmd(BaseCommand cmd) {
		this.cmd = cmd;
	}

	public BaseCommand getCmd() {
		return cmd;
	}

	public YzBaseDomain getDomain() {
		return domain;
	}

	public void setDomain(YzBaseDomain domain) {
		this.domain = domain;
	}
}
