package com.yz.network.examination.interceptor;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext; 
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm; 
import com.yz.redis.RedisService;
import com.yz.util.StringUtil;

@Component(value = NetWorkExamConstants.VAILCODE_NETWORKEXAMINATION_INTERCEPTOR)
public class VailCodeNetWorkExamInterceptor implements NetWorkExamInterceptor {

 

	@Override
	public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
		BaseNetWorkExamForm form = (BaseNetWorkExamForm) context.getAttribute("NetWorkExamForm"); // 获取当前表单对象
		List<String> cookies = Lists.newArrayList(response.getAllHeaders()).parallelStream()
				.map(v -> StringUtil.equalsIgnoreCase(v.getName(), "Set-Cookie") ? v.getValue().split(";")[0] : "")
				.filter(StringUtil::isNotBlank).collect(Collectors.toList());
		logger.info("form.id:{}.name:{},cookie:{}", form.getId(), form.getName(), cookies);
		//if (cookies != null && cookies.size() >= 2) { // cookie 异常
			String cookie = StringUtil.join(cookies, ";");
			RedisService.getRedisService().hsetarr(form.getId(), "networkCookie", StringUtil.getString2Utf8(cookie)); // 将cookie放入Redis中存储
			//return;
		//}
		logger.info("form[{}]->{}.getcookies.error,cookie:{}", form.getId(), form.getName(), cookies);
	}

	@Override
	public int getSeq() {
		return 0;
	}

}
