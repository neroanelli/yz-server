package com.yz.redis.hook;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.constants.RedisKeyConstants;
import com.yz.model.GetTokenChannel;

import redis.clients.jedis.Tuple; 
/**
 * 
 * RedisHookFactory
 * 
 * @author Administrator
 *
 */
public interface RedisOpHookHolder {

	static Logger logger = LoggerFactory.getLogger(RedisOpHookHolder.class);

	public static int RedisHookFactory = 0;

	public static int GET_WECHAT_TOKEN_TYPE = 0;

	public static int GET_WECHAT_JSAPITICKET_TYPE = 1;

	public static int GET_JD_TOKEN_TYPE = 2;
	
	public static int GET_JD_ENTITY_TOKEN_TYPE = 3;
	 
	public static RedisOpHook<GetTokenChannel> GET_WECHAT_TOKEN_HOOK = new DefaultRedisOpHook(GET_WECHAT_TOKEN_TYPE,RedisKeyConstants.GET_WECHAT_TOKEN_CHANNEL);

	public static RedisOpHook<GetTokenChannel> GET_JD_TOKEN_HOOK =new DefaultRedisOpHook(GET_JD_TOKEN_TYPE,RedisKeyConstants.GET_JD_TOKEN_CHANNEL);
	
	public static RedisOpHook<GetTokenChannel> GET_ENTITY_JD_TOKEN_HOOK =new DefaultRedisOpHook(GET_JD_ENTITY_TOKEN_TYPE,RedisKeyConstants.GET_JD_ENTITY_TOKEN_CHANNEL);
	
	public static RedisOpHook<GetTokenChannel> GET_WECHAT_JSAPITICKET_HOOK =new DefaultRedisOpHook(GET_WECHAT_JSAPITICKET_TYPE,RedisKeyConstants.GET_WECHAT_TOKEN_CHANNEL);
	
	public static RedisOpHook<Boolean> NEW_USER_REGLIST_HOOK = (redis,param)->{
		Optional<Tuple> tuple = redis.zrangeByScoreWithScores((String)param,0d,(double)new Date().getTime(),10,1).parallelStream().findFirst();
		if(tuple!=null&&tuple.isPresent())
		{
		   double score = tuple.get().getScore();
		   logger.info("NEW_USER_REGLIST_HOOK.start;key:{},score:{}",param,score);
		   redis.zremrangeByScore((String)param, 0, score);
		}
		return true;
	};
}
