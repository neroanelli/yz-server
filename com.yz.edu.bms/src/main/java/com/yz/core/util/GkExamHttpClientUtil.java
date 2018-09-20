package com.yz.core.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import com.yz.core.discern.DiscernUtil;
import com.yz.util.StringUtil;
import org.apache.log4j.LogManager;  
import org.apache.log4j.Logger;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * @描述: ${DESCRIPTION}
 * @作者: Zhuliping
 * @创建时间: 2017/11/23 19:47
 * @版本号: V1.0
 */
public class GkExamHttpClientUtil {
	
	 
	/** 验证图片链接*/
	public static final String VALI_CODE = "http://server3.cdce.cn/student/CodeImage.aspx";
	
	/** 统考成绩是否合格页面 */
	public static final String SCORE_RESULT = "http://server3.cdce.cn/student/ScorePass.aspx";
	
	/** 统考成绩报考科目页 */
	public static final String SUBJECT_RESULT = "http://server3.cdce.cn/Student/StuUpSignInfo.aspx";
	
	/** Referer */
	public static final String Referer_URL = "http://server3.cdce.cn/Student/Index.aspx";
	
	/** LoginUrl */
	public static final String Login_URL = "http://server3.cdce.cn/student/default.aspx";
	 
	private static final Logger LOG = LogManager.getLogger(GkExamHttpClientUtil.class); 
	public  static CloseableHttpClient httpClient = null;  
    public  static HttpClientContext context = null;  
    public  static CookieStore cookieStore = null;  
    public  static RequestConfig requestConfig = null;
    private static CloseableHttpResponse  loignResponse=null;
    private static void init() {  
        context = HttpClientContext.create();  
        if(cookieStore==null)
        	cookieStore = new BasicCookieStore();  
        // 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）  
        requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000)  
                       .setConnectionRequestTimeout(60000).build();  
        // 设置默认跳转以及存储cookie  
        httpClient = HttpClientBuilder.create()  
                     .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())  
                     .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)  
                     .setDefaultCookieStore(cookieStore).build();  
    }  

    static {  
    	init();
    }  
    
    /**
     * 统考系统登录
     * @param ksh
     * @param csrq
     * @return
     */
    public static boolean login(String schoolRoll,String pwd) {
        try {
        	//获取登录页面的__VIEWSTATE
            HttpGet httpGet = new HttpGet(Login_URL);
            httpGet.addHeader("Host", "server3.cdce.cn");
            httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
            
            HttpResponse homeResponse = httpClient.execute(httpGet, context);
            String result=toString(homeResponse);
            String eventAlidation="";
            String viewState="";
            if (!StringUtil.isBlank(result)) {
            	Document doc = Jsoup.parse(result);// 把html字符串解析为一个新的Documnet文档
                if (null != doc) {
                	viewState=doc.getElementById("__VIEWSTATE").val();// 通过tag标签获取元素
                	eventAlidation=doc.getElementById("__EVENTVALIDATION").val();  
                }
            }
            
            //获取图片验证码
        	String random = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
            HttpGet httpGetVali = new HttpGet(VALI_CODE+"?"+random);
            httpGetVali.addHeader("Accept", "image/webp,image/*,*/*;q=0.8");
            httpGetVali.addHeader("Host", "server3.cdce.cn");
            httpGetVali.addHeader("Referer", "http://server3.cdce.cn/Student/default.aspx");
            httpGetVali.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            CloseableHttpResponse  responseVali = httpClient.execute(httpGetVali);
            
            byte[] imgByte = EntityUtils.toByteArray(responseVali.getEntity());
            DiscernUtil.init(null);//TODO 去掉
            String valiCode = DiscernUtil.getCode(imgByte);
            if(valiCode == null){
                return false;
            }
            
            Header[] header = responseVali.getAllHeaders();
            String cookies = "";
            for (int i=0;i<header.length;i++) {
                if("Set-Cookie".equals(header[i].getName())){
                    cookies += header[i].getValue().split(";")[0]+";";
                }
            }
            if(cookies.length()>0)
            	cookies = cookies.substring(0,cookies.length()-1);
            
            //###########################登录操作########################
            HttpPost httpost  = new HttpPost(Login_URL);
            httpost.addHeader("Referer", "http://server3.cdce.cn/Student/default.aspx");
            httpost.addHeader("Cookie",cookies);
            httpost.addHeader("Host", "server3.cdce.cn");
            httpost.addHeader("Accept-Encoding", "gzip, deflate");
            httpost.addHeader("Origin", "http://server3.cdce.cn");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
            nvps.add(new BasicNameValuePair("txtEmail", schoolRoll));  
            nvps.add(new BasicNameValuePair("txtPWD", pwd));  
            nvps.add(new BasicNameValuePair("txtCheck", valiCode));  
            nvps.add(new BasicNameValuePair("hid1", ""));  
            nvps.add(new BasicNameValuePair("btnLogin.x", "28"));  
            nvps.add(new BasicNameValuePair("btnLogin.y", "7")); 
            nvps.add(new BasicNameValuePair("__EVENTVALIDATION", eventAlidation));  
            nvps.add(new BasicNameValuePair("__VIEWSTATE", viewState));
             
             /*登陆成功，获取返回的数据，即html文件*/
             httpost.setEntity(new UrlEncodedFormEntity (nvps, Charset  
                     .forName("gb2312")));  
             loignResponse = httpClient.execute(httpost,context);      

             if (loignResponse.getStatusLine().getStatusCode() == 302) {  
            	 Header locationHeader = loignResponse.getFirstHeader("Location");
                 if(locationHeader!=null&&locationHeader.getValue().indexOf("/student/Index.aspx")>=0){
                	 LOG.info("学号["+schoolRoll+"]登录成功！" );   
                 } else {  
                	 LOG.info("学号["+schoolRoll+"]登录失败！" );   
                     return false;
                 }  
                 
                 httpost.abort();  
                 httpGetVali.abort();  
             } else {  
            	 LOG.info("学号["+schoolRoll+"]登录失败！" );   
                 return false;
             }  
             
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;  
    }

    
  
    
    public static HashMap<String,String> getScoreInfo(String schoolRoll,String pwd) {
        /*如果注册成功了，输入相应后的html*/   
        if (login(schoolRoll,pwd)) { 
        	HashMap<String,String> mapResult=new HashMap<String,String>();
        	try {
	            HttpPost httpost  = new HttpPost(SCORE_RESULT);
	            httpost.addHeader("Referer", "http://server3.cdce.cn/student/index.aspx");
	            httpost.addHeader("Host", "server3.cdce.cn");
	            httpost.addHeader("Accept-Encoding", "gzip, deflate");
	            httpost.addHeader("Origin", "http://server3.cdce.cn");
	            List<NameValuePair> nvpsScroe = new ArrayList<NameValuePair>(); 
	            nvpsScroe.add(new BasicNameValuePair("__VIEWSTATE", "/wEPDwULLTEwMTA3NDkwNjUPZBYCAgMPZBYGAgMPDxYCHgRUZXh0BQ0xNzQ0MTAxMjA5MjgwZGQCBw8PFgIfAAUJ6YKx54eV6JCNZGQCCQ88KwANAQAPFgQeC18hRGF0YUJvdW5kZx4LXyFJdGVtQ291bnQCAWQWAmYPZBYEAgEPZBYGZg8PFgIfAAUSMjAxN+W5tDEy5pyI57uf6ICDZGQCAQ8PFgIfAAUN5aSn5a2m6Iux6K+tQmRkAgIPDxYCHwAFBuWQiOagvGRkAgIPDxYCHgdWaXNpYmxlaGRkGAEFCUdyaWRWaWV3MQ88KwAKAQgCAWSzSXTF/LAuw154tsTnoM9A3lAujA=="));
	             
	            /*登陆成功，获取返回的数据，即html文件*/
	            httpost.setEntity(new UrlEncodedFormEntity (nvpsScroe, Charset.forName("gb2312")));  
	            CloseableHttpResponse scoreResponse = httpClient.execute(httpost,context);
	            String result=toString(scoreResponse);
	            
	            
	            if (!StringUtil.isBlank(result)) {
	            	Document doc = Jsoup.parse(result);// 把html字符串解析为一个新的Documnet文档
	                if (null != doc) {
	                	Elements elements = doc.getElementById("GridView1").select("tr");
	                	for (Element element : elements) {
	                		if(element.getElementsByTag("th").size()>0)	 continue;
	                		if(element.getElementsByTag("td").size()>2) {
	                			mapResult.put(element.getElementsByTag("td").get(1).toString(), element.getElementsByTag("td").get(2).toString());
	                            System.out.println(element.getElementsByTag("td").get(1).toString());
	                            System.out.println(element.getElementsByTag("td").get(2).toString());
	                		}
	                		
                        }
	                }
	            }
	            
	            //##################### 得到缴费信息
	            HttpPost subhttppost  = new HttpPost(SUBJECT_RESULT);
	            subhttppost.addHeader("Referer", "http://server3.cdce.cn/student/index.aspx");
	            subhttppost.addHeader("Host", "server3.cdce.cn");
	            subhttppost.addHeader("Accept-Encoding", "gzip, deflate");
	            subhttppost.addHeader("Origin", "http://server3.cdce.cn");
	            List<NameValuePair> nvpsSub = new ArrayList<NameValuePair>(); 
	            nvpsScroe.add(new BasicNameValuePair("__VIEWSTATE", "/wEPDwULLTEwMTA3NDkwNjUPZBYCAgMPZBYGAgMPDxYCHgRUZXh0BQ0xNzQ0MTAxMjA5MjgwZGQCBw8PFgIfAAUJ6YKx54eV6JCNZGQCCQ88KwANAQAPFgQeC18hRGF0YUJvdW5kZx4LXyFJdGVtQ291bnQCAWQWAmYPZBYEAgEPZBYGZg8PFgIfAAUSMjAxN+W5tDEy5pyI57uf6ICDZGQCAQ8PFgIfAAUN5aSn5a2m6Iux6K+tQmRkAgIPDxYCHwAFBuWQiOagvGRkAgIPDxYCHgdWaXNpYmxlaGRkGAEFCUdyaWRWaWV3MQ88KwAKAQgCAWSzSXTF/LAuw154tsTnoM9A3lAujA=="));
	             
	            /*登陆成功，获取返回的数据，即html文件*/
	            subhttppost.setEntity(new UrlEncodedFormEntity (nvpsSub, Charset.forName("gb2312")));  
	            CloseableHttpResponse subResponse = httpClient.execute(subhttppost,context);
	            String subresult=toString(subResponse);
	            if (!StringUtil.isBlank(subresult)) {
	            	Document doc = Jsoup.parse(subresult);// 把html字符串解析为一个新的Documnet文档
	                if (null != doc) {
	                	Elements elements = doc.getElementById("GridView1").select("tr");
	                	for (Element element : elements) {
	                		if(element.getElementsByTag("th").size()>0)	 continue;
	                		if(element.getElementsByTag("td").size()>2) {
	                			mapResult.put(element.getElementsByTag("td").get(1).toString(), element.getElementsByTag("td").get(2).toString());
	                            System.out.println(element.getElementsByTag("td").get(1).toString());
	                            System.out.println(element.getElementsByTag("td").get(2).toString());
	                		}
	                		
                        }
	                }
	            }
	            
				httpost.abort();
				subhttppost.abort();
				return mapResult;
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return null;  
       
    }  
    

    /**  
     * 手动增加cookie  
     * @param name  
     * @param value  
     * @param domain  
     * @param path  
     */  
    public static void addCookie(String name, String value, String domain, String path) {  
        BasicClientCookie cookie = new BasicClientCookie(name, value);  
        cookie.setDomain(domain);  
        cookie.setPath(path);  
        cookieStore.addCookie(cookie);  
    }  
    
   
    
    
    /**
     * 直接把Response内的Entity内容转换成String
     *
     * @param httpResponse
     * @return
     */
    private static String toString(HttpResponse httpResponse) {
        // 获取响应消息实体
        String result = null;
        try {
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ((CloseableHttpResponse) httpResponse).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /*main方法*/
    public static void main(String[] args) {  
    	String schoolRoll="1744101203658";
    	String pwd="yz123456";
    	GkExamHttpClientUtil.getScoreInfo(schoolRoll,pwd);
        
    }  
}
