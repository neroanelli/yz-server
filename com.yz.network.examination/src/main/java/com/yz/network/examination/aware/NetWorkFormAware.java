package com.yz.network.examination.aware;

import java.util.Map;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.form.YzNetWorkForm;

/**
 * 
 * 
 * @author Administrator
 *
 */
public interface NetWorkFormAware {

	public static final Map<Class<?>, YzNetWorkForm> conf = new MapMaker().makeMap();
	
	/**
	 * @desc 根据form获取配置信息
	 * @param form
	 * @return
	 */
	default public YzNetWorkForm getFormConfig(BaseNetWorkExamForm form) {
		if (form != null) {
			Class<?> cls = form.getClass();
			if (conf.containsKey(cls)) {
				return conf.get(cls);
			}
			conf.put(cls, cls.getAnnotation(YzNetWorkForm.class));
			return this.getFormConfig(form);
		}
		return null;
	}
}
