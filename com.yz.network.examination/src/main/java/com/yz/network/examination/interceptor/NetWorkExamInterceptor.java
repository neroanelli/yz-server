package com.yz.network.examination.interceptor;

import java.io.IOException;  
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse; 
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import com.yz.http.interceptor.HttpInterceptor; 
/**
 * 
 * @desc 网报系统拦截器
 * @author lingdian
 *
 */
public interface NetWorkExamInterceptor extends HttpInterceptor {

	public static final Logger logger = LoggerFactory.getLogger(NetWorkExamInterceptor.class);

	@Override
	default void process(HttpRequest request, HttpContext context) throws HttpException, IOException { }

	@Override
	default void process(HttpResponse response, HttpContext context) throws HttpException, IOException { }

}
