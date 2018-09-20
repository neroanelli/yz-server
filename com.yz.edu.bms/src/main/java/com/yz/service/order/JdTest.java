package com.yz.service.order;

import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JdTest {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//getStkBalance();
		//getJdGoodDetail("1107854");
		//getJdGoodDetail("4384890");
		getJdOrderInfoByjdOrderId("76807730085");//76699252357
		//getMessage();
		//delMessage("72931392958");
		//orderTrackByNo("76807730085");
	}
	
	
	 /**
     * 得到京东实体卡账户余额
     * @return
     */ 
    public static String getStkBalance() {
    	String balance="0.0";
    	String url="http://bizapi.jd.com/vir/api/pay/getBalance";
    	String token="ff33JtrX37zEpXaBY3pV61e37";
    	//String token = RedisService.getRedisService().get("jdEntityCardToken");
		String   data="token="+token+"&type=4";	
		String   result =HttpUtil.sendPost(url , data,HttpTraceInterceptor.TRACE_INTERCEPTOR);
		System.out.println(result.toString());
		JSONObject resultObject =JSONObject.fromObject(result);
		if(resultObject.has("success")&&resultObject.getBoolean("success")) {
			balance=resultObject.getString("result");
		}
		return balance;		
		
    }
    
    public static String getJdGoodDetail(String skuId) {
    	
    	String url="https://bizapi.jd.com/api/product/getDetail";
    	String token="ff33JtrX37zEpXaBY3pV61e37";
    	//String token = RedisService.getRedisService().get("jdEntityCardToken");
		String   data="token="+token+"&sku="+skuId;
		String detailResult = HttpUtil.sendPost(url,  data,HttpTraceInterceptor.TRACE_INTERCEPTOR);
	    JSONObject goodDetailObject =JSONObject.fromObject(detailResult);
	    System.out.println(goodDetailObject.toString());
		return goodDetailObject.toString();
    	
    }
    
  //得到京东订单编号得到订单信息
    public static void getJdOrderInfoByjdOrderId  (String jdOrderId) {
    	String url="https://bizapi.jd.com/api/order/selectJdOrder";
    	//String token = RedisService.getRedisService().get("jdAccessToken");
    	String token="V3G0hmT0klyuiaI3hXgTq8v7C";
		String   data="token="+token+"&jdOrderId="+jdOrderId+"&&queryExts=jdOrderState";
		String   result = HttpUtil.sendPost(url,  data,HttpTraceInterceptor.TRACE_INTERCEPTOR);
		  
	    JSONObject orderObject = JSONObject.fromObject(result);
	    System.out.println(orderObject.toString());
		if(orderObject.has("success")&&orderObject.getBoolean("success")) {
				JSONObject orderResult =(JSONObject) orderObject.get("result");
	    		if(orderResult!=null){  
	    			//订单状态  0 为取消订单  1 为有效 如果返回的订单状态为0则返回空
	    			if(orderResult.has("orderState")&&orderResult.getString("orderState")!=null&&orderResult.getString("orderState").equals("0")) {
	    				System.out.println("return null;");
	    			}
	    			
	    			
	    			//如果返回是子订单:订单类型   1 是父订单   2 是子订单 
	    			if(orderResult.has("type")&&orderResult.getString("type").equals("2")) {
	    				//物流状态 0 是新建  1 是妥投   2 是拒收
		    			if(orderResult.has("state")&&orderResult.getString("state")!=null) {
		    				String state=orderResult.getString("state");
		    				System.out.println("return state:"+state);
		    				
		    			}
		    			if(orderResult.has("orderPrice")&&orderResult.getString("orderPrice")!=null) {
		    				String orderPrice=orderResult.getString("orderPrice");//订单总价格 
		    				System.out.println("return orderPrice:"+orderPrice);
		    			}
		    			
		    			if(orderResult.has("freight")&&orderResult.getString("freight")!=null) {
		    				String freight=orderResult.getString("freight");//运费（合同有运费配置才会返回，否则不会返回该字段）
		    				System.out.println("return freight:"+freight);
		    			}
		    			JSONArray skujsonArray = JSONArray.fromObject(orderResult.get("sku") ); 
		    			if(skujsonArray!=null&&skujsonArray.size()>0){  
		    				if(null!=skujsonArray.get(0)) {
		    					JSONObject skuJson = JSONObject.fromObject(skujsonArray.get(0));
		    					if(skuJson.containsKey("num")&&skuJson.containsKey("price")&&skuJson.containsKey("name")) {
		    						String num=skuJson.getString("num");
		    						String price=skuJson.getString("price");//商品价格
				    				String name=skuJson.getString("name");//商品名称
				    				System.out.println("return num:"+num);
				    				System.out.println("return price:"+price);
				    				System.out.println("return name:"+name);
				    				
		    					}else {
		    						System.out.println("log.error(\"jdOrderId:{},result:{}\",jdOrderId,result)");
		    					}
			    				
			    				
		    				}
		    				
		    			}
	    			}//如果返回是父订单
	    			else if(orderResult.has("type")&&orderResult.getString("type").equals("1")) {
	    				//得到子订单
	    				JSONArray cOrderArray = JSONArray.fromObject(orderResult.get("cOrder") ); 
	    				if(cOrderArray!=null&&cOrderArray.size()>0){  
	    					JSONObject cOrderJson = JSONObject.fromObject(cOrderArray.get(0));
	    					if(cOrderJson.has("state")&&cOrderJson.getString("state")!=null) {
			    				String state=cOrderJson.getString("state");
			    				System.out.println("return state:"+state);
			    			}
	    					
	    				}
	    				
	    				//得到父订单
	    				JSONObject pOrderResult =(JSONObject) orderResult.get("pOrder");
	    				if(pOrderResult.has("orderPrice")&&pOrderResult.getString("orderPrice")!=null) {
		    				String orderPrice=pOrderResult.getString("orderPrice");//订单总价格 
		    				System.out.println("return orderPrice:"+orderPrice);
		    			}
		    			
		    			if(pOrderResult.has("freight")&&pOrderResult.getString("freight")!=null) {
		    				String freight=pOrderResult.getString("freight");//运费（合同有运费配置才会返回，否则不会返回该字段） 
		    				System.out.println("return freight:"+freight);
		    			}
		    			JSONArray skujsonArray = JSONArray.fromObject(pOrderResult.get("sku") ); 
		    			if(skujsonArray!=null&&skujsonArray.size()>0){  
		    				if(null!=skujsonArray.get(0)) {
		    					JSONObject skuJson = JSONObject.fromObject(skujsonArray.get(0));
		    					if(skuJson.containsKey("num")&&skuJson.containsKey("price")&&skuJson.containsKey("name")) {
		    						String num=skuJson.getString("num");
		    						String price=skuJson.getString("price");//商品价格
				    				String name=skuJson.getString("name");//商品名称
				    				System.out.println("return num:"+num);
				    				System.out.println("return price:"+price);
				    				System.out.println("return name:"+name);

		    					}else {
		    						System.out.println("log.error(\"jdOrderId:{},result:{}\",jdOrderId,result)");
		    					}
		    				}
		    				
		    			}
	    				
	    			}else {
	    				System.out.println("log.error(\"jdOrderId:{},result:{}\",jdOrderId,result)");
	    			}
	    			
	    		}
	    		
	    		System.out.println("success!!!!!");
		}	
		
    	
    }
    
    public static void getMessage() {
    	String url="https://bizapi.jd.com/api/message/get";
    	//String token = RedisService.getRedisService().get("jdAccessToken");
    	String token="pP58zUBD5ilSnzufiM8QWCowD";
		String   data="token="+token+"&type=5";
		String   result = HttpUtil.sendPost(url,  data,HttpTraceInterceptor.TRACE_INTERCEPTOR);
		  
	    JSONObject orderObject = JSONObject.fromObject(result);
	    System.out.println(orderObject.toString());
	    JSONObject resultObject = JSONObject.fromObject(result);
	    if(resultObject.has("success")&&resultObject.getBoolean("success")) {
	    	JSONArray orderjsonArray = JSONArray.fromObject(resultObject.getString("result")); 
	    	if(orderjsonArray!=null&&orderjsonArray.size()>0){  
	    		for (Object object : orderjsonArray) {
	    			JSONObject orderJson=(JSONObject) object;
	    			String id=orderJson.getString("id");
	    			String time=orderJson.getString("time");
	    			
	    			JSONObject orderIdJson = JSONObject.fromObject(orderJson.getString("result"));
	    			if(orderIdJson.has("orderId")) {
	    				String jdOrderId=orderIdJson.getString("orderId");
	    				
	    				
	    				System.out.println("jdOrderId:"+jdOrderId);
	    			}
	    			System.out.println("id:"+id);
	    			System.out.println("time:"+time);
	    			
	    			//删除推送信息
	    			//delMessage("5202921158");
	    			
				}
	    	}
	    	
			
			
		}
		
    }
    
    /**
	 * 获取订单配送信息
	 * @param header
	 * @param body
	 * @return
	 */
    public static String orderTrackByNo(String jdOrderId){	
		String trackUrl = "https://bizapi.jd.com/api/order/orderTrack";
		StringBuilder sb = new StringBuilder();
		//String token = "V3G0hmT0klyuiaI3hXgTq8v7C";
		String token = "nY1jcPy6PeVd5ysSpUdmJEyyT";
		
		sb.append("token="+token);
		sb.append("&jdOrderId="+jdOrderId);
		String trackResult = HttpUtil.sendPost(trackUrl, sb.toString(),HttpTraceInterceptor.TRACE_INTERCEPTOR);
		System.out.println("下单结果:-------"+trackResult);
		JSONObject obj = JSONObject.fromObject(trackResult);
		if(null != obj && obj.containsKey("resultCode") && obj.getString("resultCode").equals("0000")){
			JSONObject resultObj = JSONObject.fromObject(obj.getString("result"));
			return resultObj.getString("orderTrack");
		}
		return null;	
	}
    
    
    
    public static void delMessage(String id) {
    	String url="https://bizapi.jd.com/api/message/del ";
    	//String token = RedisService.getRedisService().get("jdAccessToken");
    	String token="pP58zUBD5ilSnzufiM8QWCowD";
		String   data="token="+token+"&id="+id;
		String   result = HttpUtil.sendPost(url, data,HttpTraceInterceptor.TRACE_INTERCEPTOR);  
	    JSONObject orderObject = JSONObject.fromObject(result);
	    System.out.println(orderObject.toString());
    }


	/**
	 * 同步订单完成时间
	 * @param token
	 */
	public static void setJdOrderTime(String token) {
		String url="https://bizapi.jd.com/api/message/get";
		String   data="token="+token+"&type=5";
		String   result = HttpUtil.sendPost(url,  data,null); 
	    JSONObject resultObject = JSONObject.fromObject(result);
	    if(resultObject.has("success")&&resultObject.getBoolean("success")) {
	    	JSONArray orderjsonArray = JSONArray.fromObject(resultObject.getString("result")); 
	    	if(orderjsonArray!=null&&orderjsonArray.size()>0){  
	    		for (Object object : orderjsonArray) {
	    			JSONObject orderJson=(JSONObject) object;
	    			String id=orderJson.getString("id");
	    			String time=orderJson.getString("time");
	    			JSONObject orderIdJson = JSONObject.fromObject(orderJson.getString("result"));
	    			if(orderIdJson.has("orderId")) {
	    				String jdOrderId=orderIdJson.getString("orderId");
	    				//orderService.updateOrderCompleteTime(jdOrderId, time);
	    			}
	    			//sleep(200);
	    			
//	    			String oss = YzSysConfig.getBucket();
//	    			if(StringUtil.hasValue(oss)&&oss.equals("yzims")) {
//	    				//删除推送信息只有在正测试环下操作
//		    			//delMessage(token,id);
//	    			}
	    			
				}
	    	}
	    	
			
			
		}
		
	}
}

