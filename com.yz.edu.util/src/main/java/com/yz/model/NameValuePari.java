package com.yz.model;

/**
 * 
 * 
 * @author Administrator
 *
 */
public class NameValuePari implements java.io.Serializable {

	public NameValuePari() {

	}

	public NameValuePari(String key, String value) {
		this.key = key;
		this.value = value;
	}

	private String key;

	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}