package com.yz.http.pool;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.yz.http.interceptor.HttpInterceptor;

/**
 * 
 * @author Administrator
 *
 */
public class HttpClientPool {

	private static Logger logger = LoggerFactory.getLogger(HttpClientPool.class);

	PoolingHttpClientConnectionManager cm = null;

	private static class HttpClientPoolHolder {
		private static HttpClientPool _instance = new HttpClientPool();
	}

	public static HttpClientPool getInstance() {
		return HttpClientPoolHolder._instance;
	}

	private HttpClientPool() {
		LayeredConnectionSocketFactory sslsf = null;
		try {
			sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
		} catch (NoSuchAlgorithmException e) {
			logger.error("init.error:{}", e);
		}
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
		cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		cm.setMaxTotal(200);
		cm.setDefaultMaxPerRoute(20);
		Runtime.getRuntime().addShutdownHook(new Thread(cm::close));
	}

	/**
	 * 
	 * @param interceptor
	 * @return
	 */
	public CloseableHttpClient getHttpClient(HttpInterceptor... interceptors) {
		return getHttpClient(Lists.newArrayList(interceptors));
	}

	/**
	 * 
	 * @param interceptor
	 * @return
	 */
	public CloseableHttpClient getHttpClient(List<HttpInterceptor> interceptors) {
		if (interceptors == null || interceptors.isEmpty()) {
			return HttpClients.custom().setConnectionManager(cm).setDefaultCookieStore(new BasicCookieStore()).build();
		}
		HttpClientBuilder builder = HttpClients.custom().setDefaultCookieStore(new BasicCookieStore())
				.setConnectionManager(cm);
		interceptors.parallelStream().filter(Objects::nonNull).sorted((x, y) -> (x.getSeq() <= y.getSeq() ? 1 : -1))
				.forEach(interceptor -> {
					builder.addInterceptorFirst((HttpRequestInterceptor) interceptor)
							.addInterceptorLast((HttpResponseInterceptor) interceptor);
				});
		return builder.build();
	}

	/**
	 * 
	 * @param interceptor
	 * @return
	 */
	public String invoke(HttpUriRequest request, HttpInterceptor... interceptors) {
		StopWatch sw = new StopWatch();
		sw.start();
		CloseableHttpResponse response = null;
		InputStream in = null;
		try {
			HttpContext context = new BasicHttpContext();
			context.setAttribute("sw", sw);
			CloseableHttpClient client = this.getHttpClient(interceptors);
			response = client.execute(request, context);
			in = response.getEntity().getContent();
			return IOUtils.toString(in);
		} catch (Exception e) {
			logger.error("发送Http请求出现异常！", e);
		}
		// 使用finally块来关闭输入流
		finally {
			IOUtils.closeQuietly(in);
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error("发送Http请求出现异常！", e);
				}
			}
		}
		return null;
	}
}
