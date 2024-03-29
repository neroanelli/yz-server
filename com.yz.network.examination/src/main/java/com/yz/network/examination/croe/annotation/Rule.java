package com.yz.network.examination.croe.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Rule {
	/**
	 * 是否检测登录
	 * @return
	 */
	public boolean checkLogin() default true;

}
