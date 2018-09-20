package com.yz.network.examination.param;

import org.apache.http.message.BasicNameValuePair;

import com.yz.network.examination.format.YzNetWorkFormater;
import com.yz.util.StringUtil;

/**
 * 
 * @desc 网报系统参数
 * @author lingdian
 *
 */
@SuppressWarnings("serial")
public class YzNetWorkExamParam extends BasicNameValuePair {

	private int seq; // 参数类型

	private transient YzNetWorkFormater format; // 参数格式化处理？

	private String provider; // 参数提供者 0 手动赋值 1 获取第三方系统数据

	public YzNetWorkExamParam(String name, YzNetWorkFormater format, Object value, Object def) {
		super(name, format.formater(value, def));
	}

	public YzNetWorkExamParam(String name, YzNetWorkFormater format, Object value) {
		this(name, format, value, StringUtil.EMPTY);
	}
	
	
	public YzNetWorkExamParam(String name, Object value) {
		this(name,YzNetWorkFormater.def, value, StringUtil.EMPTY);
	}

	public int getSeq() {
		return seq;
	}

	public YzNetWorkExamParam setSeq(int seq) {
		this.seq = seq;
		return this;
	}

	public YzNetWorkFormater getFormat() {
		return format;
	}

	public void setFormat(YzNetWorkFormater format) {
		this.format = format;
	}

	public String getProvider() {
		return provider;
	}

	public YzNetWorkExamParam setProvider(String provider) {
		this.provider = provider;
		return this;
	}
}
