package com.yz.redis;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yz.constants.RedisKeyConstants;
import com.yz.edu.trace.TraceAnnotation;
import com.yz.edu.trace.TraceTransfer;
import com.yz.edu.trace.constant.TraceConstant;
import com.yz.exception.BusinessException;
import com.yz.redis.RedisService.RedisTransactionExecutor.DomainExecResult;
import com.yz.redis.hook.RedisOpHook;
import com.yz.serializ.FstSerializer;
import com.yz.util.ClassUtil;
import com.yz.util.DateUtil;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

public class RedisService {

	private static YzRedisFactory _instance = YzRedisFactory.getInstance();

	private static Logger logger = LoggerFactory.getLogger(RedisService.class);

	private static int default_expire = 300;

	private YzJedisPool redisPool;

	private FstSerializer fstSerialize = FstSerializer.getInstance();

	private RedisService(YzJedisPool redisPool) {
		this.redisPool = redisPool;
	}

	/**
	 *
	 * @param redisPool
	 * @return
	 */
	public static RedisService newRedisService(YzJedisPool redisPool) {
		return new RedisService(redisPool);
	}

	/**
	 *
	 * @return
	 */
	public static RedisService getRedisService() {
		return _instance.getRedisService();
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public static RedisService getRedisService(String name) {
		return _instance.getRedisService(name);
	}

	/**
	 * @param key
	 * @return
	 */
	public Long incr(String key) { 
		return this.incr(key,default_expire);
	}

	/**
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public Long incr(String key, int seconds) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				Long incr = redis.incr(key);
				if(incr==1)
				   redis.expire(key, seconds);
				return incr;
			}
		};
		return executor.execute("incr", key);
	}

	/**
	 *
	 * @param key
	 * @param random
	 * @return
	 */
	public Long incrBy(String key, int random) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				Long incr = redis.incrBy(key, random);
				return incr;
			}
		};
		return executor.execute("incrBy", key, String.valueOf(random));
	}

	/**
	 * @param key
	 * @return
	 */
	public byte[] getByte(String key) {
		RedisExecutor<byte[]> executor = new RedisExecutor<byte[]>() {
			@Override
			byte[] action(Jedis redis) {
				return redis.get(getString2Utf8(key));
			}
		};
		return executor.execute("getByte", key);
	}

	/**
	 * 从某个具体的db获取
	 * 
	 * @param key
	 * @return
	 */
	public <T> T getByte(String key, Class<?> cls, int dbIndex) {
		RedisExecutor<T> executor = new RedisExecutor<T>(dbIndex) {
			@Override
			T action(Jedis redis) {
				byte[] arr = redis.get(getString2Utf8(key));
				if (arr == null) {
					logger.error("not found the key:{}", key);
					return null;
				}
				return fstSerialize.deserialize(arr, cls);
			}
		};
		return executor.execute("getByte", key, cls.getName());
	}

	/**
	 * @param key
	 * @return
	 */
	public <T> T getByte(String key, Class<?> cls) {
		return this.getByte(key, cls, 0);
	}

	/**
	 *
	 * @param key
	 * @param arr
	 * @return
	 */
	public String setByte(String key, byte[] arr) {
		RedisExecutor<String> executor = new RedisExecutor<String>() {
			@Override
			String action(Jedis redis) {
				return redis.set(getString2Utf8(key), arr);
			}
		};
		return executor.execute("setByte", key);
	}

	/**
	 * 设置字节到某个具体的db
	 * 
	 * @param key
	 * @param arr
	 * @param seconds
	 * @param dbIndex
	 * @return
	 */
	public String setByte(String key, byte[] arr, int seconds, int dbIndex) {
		RedisExecutor<String> executor = new RedisExecutor<String>(dbIndex) {
			@Override
			String action(Jedis redis) {
				byte[] keyArr = getString2Utf8(key);
				String result = redis.set(keyArr, arr);
				redis.expire(key, seconds);
				return result;
			}
		};
		return executor.execute("setByte", key);
	}

	/**
	 * 设置字节
	 * 
	 * @param key
	 * @param arr
	 * @param seconds
	 * @return
	 */
	public String setByte(String key, byte[] arr, int seconds) {
		return this.setByte(key, arr, seconds, 0);
	}

	/**
	 * @param key
	 * @return
	 */
	public String getAndRem(String key) {
		RedisExecutor<String> executor = new RedisExecutor<String>() {
			@Override
			String action(Jedis redis) {
				String result = redis.get(key);
				if (StringUtil.isNotBlank(result)) {
					redis.del(key);
				}
				return result;
			}
		};
		return executor.execute("getAndRem", key);
	}

	/**
	 * @param key
	 * @return
	 */
	public String get(String key) {
		RedisExecutor<String> executor = new RedisExecutor<String>() {
			@Override
			String action(Jedis redis) {
				return redis.get(key);
			}
		};
		return executor.execute("get", key);
	}

	/**
	 * @param key
	 * @return
	 */
	public String get(String key, RedisOpHook<?> op) {
		RedisExecutor<String> executor = new RedisExecutor<String>() {
			@Override
			String action(Jedis redis) {
				int i = 5;
				while (i-- >= 0) {
					String val = redis.get(key);
					if (StringUtil.isNotBlank(val)) {
						return val;
					}
					op.execute(redis, key);
				}
				return null;
			}
		};
		return executor.execute("get", key);
	}

	/**
	 * @param key
	 * @return
	 */
	public String getAndReturn(String key, RedisOpHook<?> op) {
		RedisExecutor<String> executor = new RedisExecutor<String>() {
			@Override
			String action(Jedis redis) {
				int i = 5;
				while (i-- >= 0) {
					String val = redis.get(key);
					if (StringUtil.isNotBlank(val)) {
						return val;
					}
					Object result = op.execute(redis, key);
					if (result != null) {
						redis.set(key, JsonUtil.object2String(result));
						return String.valueOf(result);
					}
				}
				return null;
			}
		};
		return executor.execute("get", key);
	}

	/**
	 * @param key
	 * @return
	 */
	public Long ttl(String key) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				boolean bol = redis.exists(key);
				if (bol) {
					return redis.ttl(key);
				}
				return 0l;
			}
		};
		return executor.execute("ttl", key);
	}

	/**
	 * 
	 * @param keys
	 * @return
	 */
	public String mset(String... keys) {
		RedisExecutor<String> executor = new RedisExecutor<String>() {
			@Override
			String action(Jedis redis) {
				return redis.mset(keys);
			}
		};
		return executor.execute("mset", StringUtil.join(keys));
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public List<String> mget(String... key) {
		RedisExecutor<List<String>> executor = new RedisExecutor<List<String>>() {
			@Override
			List<String> action(Jedis redis) {
				return redis.mget(key);
			}
		};
		return executor.execute("mget", key);
	}

	/**
	 * @param key
	 * @param val
	 * @param ex
	 */
	public String setex(String key, String val, int ex) {
		if (StringUtil.isBlank(val)) {
			logger.error("setex.error,val is null");
			return StringUtil.EMPTY;
		}
		RedisExecutor<String> executor = new RedisExecutor<String>() {
			@Override
			String action(Jedis redis) {
				return redis.setex(key, ex, val);
			}
		};
		return executor.execute("set", key, val, String.valueOf(ex));
	}

	/**
	 * @param key
	 * @param val
	 * @param ex
	 */
	public String setex(String key, byte[] val, int ex) {
		if (val == null) {
			logger.error("setex.error,val is null");
			return StringUtil.EMPTY;
		}
		RedisExecutor<String> executor = new RedisExecutor<String>() {
			@Override
			String action(Jedis redis) {
				return redis.setex(getString2Utf8(key), ex, val);
			}
		};
		return executor.execute("setex", key, String.valueOf(ex));
	}

	/**
	 * @param key
	 * @param val
	 */
	public Long lpush(String key, String... val) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.lpush(key, val);
			}
		};
		return executor.execute("lpush", key, StringUtil.join(val));
	}

	/**
	 *
	 * @param key
	 * @param val
	 */
	public Long lpush(String key, byte[]... val) {
		return this.lpush(key, 0, val);
	}

	/**
	 *
	 * @param key
	 * @param dbIndex
	 *            数据库下标
	 * @param val
	 */
	public Long lpush(String key, int dbIndex, byte[]... val) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>(dbIndex) {
			@Override
			Long action(Jedis redis) {
				return redis.lpush(getString2Utf8(key), val);
			}
		};
		return executor.execute("lpush", key);
	}

	/**
	 * 从redis队列拉取数据
	 */
	public String lpopDbIndex(String key, int dbIndex) {
		List<String> result = this.lpop(key, 1, dbIndex);
		return (result==null||result.isEmpty())?null:result.get(0);
	}

	/**
	 * 从redis队列拉取数据
	 */
	public String lpopStep(String key) {
		List<String> result = this.lpop(key, 1, 0);
		return result.get(0);
	}

	/**
	 * 从redis队列拉取数据
	 * 
	 * @param key
	 *            队列key
	 * @param step
	 *            消费的步长
	 */
	public List<String> lpop(String key, int step, int dbIndex) {
		RedisExecutor<List<String>> executor = new RedisExecutor<List<String>>(dbIndex) {
			@Override
			List<String> action(Jedis redis) {
				List<String> list = Lists.newArrayListWithCapacity(step);
				for (int i = 0; i < step; i++) {
					String result = redis.lpop(key);
					if (StringUtil.isNotBlank(result)) {
						list.add(result);
					} else {
						break;
					}
				}
				return list;
			}
		};
		return executor.execute("lpop", key, String.valueOf(step));
	}

	/**
	 * 
	 * @param key
	 * @param timeout
	 * @return
	 */
	public List<String> blpop(String key, int timeout) {
		return this.blpop(key, timeout, 0);
	}

	/**
	 * 
	 * @param key
	 * @param dbIndex
	 * @param timeout
	 * @return
	 */
	public List<String> blpop(String key, int timeout, int dbIndex) {
		RedisExecutor<List<String>> executor = new RedisExecutor<List<String>>(dbIndex) {
			@Override
			List<String> action(Jedis redis) {
				return redis.blpop(timeout, key);
			}
		};
		return executor.execute("blpop", key, String.valueOf(timeout), String.valueOf(dbIndex));
	}

	/**
	 * 获取list长度
	 */
	public Long llen(String key) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.llen(key);
			}
		};
		return executor.execute("llen", key);
	}

	/**
	 * 从redis队列拉取数据
	 */
	public String rpop(String key) {
		RedisExecutor<String> executor = new RedisExecutor<String>() {
			@Override
			String action(Jedis redis) {
				return redis.rpop(key);
			}
		};
		return executor.execute("rpop", key);
	}

	/**
	 * 从redis队列拉取数据
	 */
	public byte[] lpopObj(String key) {
		RedisExecutor<byte[]> executor = new RedisExecutor<byte[]>() {
			@Override
			byte[] action(Jedis redis) {
				return redis.lpop(getString2Utf8(key));
			}
		};
		return executor.execute("lpopObj", key);
	}

	/**
	 * 从redis队列拉取数据
	 */
	public <T> T lpopObj(String key, Class<?> cls) {
		List<T> datas = this.lpopObj(key, cls, 1, 0);
		if (datas != null && !datas.isEmpty()) {
			return datas.get(0);
		}
		return null;
	}

	/**
	 * 从redis队列拉取数据
	 */
	public <T> List<T> lpopObj(String key, Class<?> cls, final int size) {
		return this.lpopObj(key, cls, size, 0);
	}

	/**
	 * 从redis队列拉取数据
	 */
	public <T> List<T> lpopObj(String key, Class<?> cls, final int size, int dbIndex) {
		RedisExecutor<List<T>> executor = new RedisExecutor<List<T>>(dbIndex) {
			@Override
			List<T> action(Jedis redis) {
				List<T> list = Lists.newArrayListWithCapacity(size);
				byte[] keyArr = getString2Utf8(key);
				for (int index = 0; index < size; index++) {
					byte[] arr = redis.lpop(keyArr);
					if (arr != null) {
						T result = fstSerialize.deserialize(arr, cls);
						if (result != null) {
							list.add(result);
						} else {
							logger.error("{}.fstSerialize.deserialize.cls:{}", key, cls.getSimpleName());
						}
					} else {
						break;
					}
				}
				return list;
			}
		};
		return executor.execute("lpopObj", key);
	}

	/**
	 * 从redis队列拉取数据
	 */
	public Object rpopObj(String key, Class<?> cls) {
		RedisExecutor<Object> executor = new RedisExecutor<Object>() {
			@Override
			Object action(Jedis redis) {
				byte[] arr = redis.rpop(getString2Utf8(key));
				return arr == null ? null : fstSerialize.deserialize(arr, cls);
			}
		};
		return executor.execute("rpopObj", key);
	}

	/**
	 * 从redis队列拉取数据
	 */
	public byte[] rpopObj(String key) {
		RedisExecutor<byte[]> executor = new RedisExecutor<byte[]>() {
			@Override
			byte[] action(Jedis redis) {
				return redis.rpop(getString2Utf8(key));
			}
		};
		return executor.execute("rpop", key);
	}

	/**
	 * @desc 设置过期时间为-1 表示不过期
	 */
	public Long sadd(String key, String... val) {
		return this.sadd(key, -1, val);
	}

	/**
	 *
	 * @param key
	 * @param expire
	 * @param val
	 */
	public Long sadd(String key, int expire, String... val) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				Long result = redis.sadd(key, val);
				if (expire > 0) {
					redis.expire(key, expire);
				}
				return result;
			}
		};
		return executor.execute("sadd", key, StringUtil.join(val));
	}

	/**
	 * @param key
	 * @param val
	 */
	public Long srem(String key, String... val) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.srem(key, val);
			}
		};
		return executor.execute("srem", key, StringUtil.join(val));
	}

	/**
	 * smembers
	 */
	public Set<String> smembers(String key) {
		RedisExecutor<Set<String>> executor = new RedisExecutor<Set<String>>() {
			@Override
			Set<String> action(Jedis redis) {
				return redis.smembers(key);
			}
		};
		return executor.execute("smembers", key);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public String srandmember(String key) {
		RedisExecutor<String> executor = new RedisExecutor<String>() {
			@Override
			String action(Jedis redis) {
				return redis.srandmember(key);
			}
		};
		return executor.execute("srandmember", key);
	}

	/**
	 * 从redis set拉取数据
	 */
	public String spopDbIndex(String key, int dbIndex) {
		List<String> datas = this.spop(key, 1, dbIndex);
		if (datas != null && !datas.isEmpty()) {
			return datas.get(0);
		}
		return null;
	}

	/**
	 * 从redis set拉取数据
	 */
	public String spopStep(String key) {
		List<String> datas = this.spop(key, 1, 0);
		if (datas != null && !datas.isEmpty()) {
			return datas.get(0);
		}
		return null;
	}

	/**
	 * 从redis set拉取数据
	 * 
	 * @param key
	 * @param step
	 */
	public List<String> spop(String key, int step, int dbIndex) {
		RedisExecutor<List<String>> executor = new RedisExecutor<List<String>>() {
			@Override
			List<String> action(Jedis redis) {
				List<String> list = Lists.newArrayListWithCapacity(step);
				for (int i = 0; i < step; i++) {
					String result = redis.spop(key);
					if (StringUtil.isNotBlank(result)) {
						list.add(result);
					} else {
						break;
					}
				}
				return list;
			}
		};
		return executor.execute("spop", key, String.valueOf(step));
	}

	/**
	 *
	 * @param key
	 * @param score
	 * @param val
	 */
	public Long zcount(String key, long min, long max) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.zcount(key, min, max);
			}
		};
		return executor.execute("zadd", key, String.valueOf(min), String.valueOf(max));
	}

	/**
	 *
	 * @param key
	 * @param score
	 * @param val
	 */
	public Long zadd(String key, double score, String val, RedisOpHook<Boolean> hook) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				long result = redis.zadd(key, score, val);
				if (result > 0 && hook != null) {
					hook.execute(redis, key);
				}
				return result;
			}
		};
		return executor.execute("zadd", key, String.valueOf(score), val);
	}

	/**
	 *
	 * @param key
	 * @param score
	 * @param val
	 */
	public Long zadd(String key, double score, String val) {
		return this.zadd(key, score, val, null);
	}

	/**
	 *
	 * @param key
	 * @param data
	 */
	public Long zadd(String key, Map<String, Double> data) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.zadd(key, data);
			}
		};
		return executor.execute("zadd", key, JsonUtil.object2String(data));
	}

	/**
	 * 从redis set拉取数据
	 */
	public Set<String> zscore(String key, double min, double max) {
		RedisExecutor<Set<String>> executor = new RedisExecutor<Set<String>>() {
			@Override
			Set<String> action(Jedis redis) {
				return redis.zrangeByScore(key, min, max);
			}
		};
		return executor.execute("zscore", key, String.valueOf(min), String.valueOf(max));
	}

	/**
	 * 从redis set拉取数据
	 */
	public Tuple zrangeByScoreWithScores(String key, double max) {
		RedisExecutor<Tuple> executor = new RedisExecutor<Tuple>() {
			@Override
			Tuple action(Jedis redis) {
				Set<Tuple> tuple = redis.zrangeByScoreWithScores(key, 0, max, 0, 1);
				if (tuple == null || tuple.isEmpty()) {
					return null;
				}
				return tuple.iterator().next();
			}
		};
		return executor.execute("zrangeByScoreWithScores", key, String.valueOf(max));
	}

	/**
	 * 从redis set拉取数据
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @param offset
	 * @param count
	 * @param asc
	 *            是否升序
	 * @return
	 */
	public Set<String> zrangeByScoreWithScores(String key, double min, double max, int offset, int count, boolean asc) {
		RedisExecutor<Set<String>> executor = new RedisExecutor<Set<String>>() {
			@Override
			Set<String> action(Jedis redis) {
				Set<Tuple> tuple = redis.zrangeByScoreWithScores(key, min, max, offset, count);
				List<Tuple> set = Lists.newArrayList(tuple);
				Set<String> result = Sets.newLinkedHashSet();
				set.stream().sorted((x, y) -> {
					return (x.getScore() >= y.getScore() ? 1 : -1) * (asc ? 1 : -1);
				}).forEachOrdered(v -> {
					result.add(v.getElement());
				});
				return result;
			}
		};
		return executor.execute("zrangeByScoreWithScores", key, String.valueOf(min), String.valueOf(max));
	}

	/**
	 * 从redis set拉取数据
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @param offset
	 * @param count
	 * @return
	 */
	public Set<String> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		return this.zrangeByScoreWithScores(key, min, max, offset, count, false);
	}

	/**
	 * 从redis set拉取数据
	 */
	public Set<String> zrangeWithScores(String key, int offset, int count) {
		RedisExecutor<Set<String>> executor = new RedisExecutor<Set<String>>() {
			@Override
			Set<String> action(Jedis redis) {
				Set<Tuple> tuple = redis.zrangeWithScores(key, offset, count);
				return tuple.parallelStream().map(v -> v.getElement()).collect(Collectors.toSet());
			}
		};
		return executor.execute("zrangeWithScores", key, String.valueOf(offset), String.valueOf(count));
	}

	/**
	 * @return zremrangeByScore
	 */
	public Long zremrangeByScore(String key, double min, double max) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.zremrangeByScore(key, min, max);
			}
		};
		return executor.execute("zremrangeByScore", key, String.valueOf(min), String.valueOf(max));
	}

	/**
	 *
	 * @param key
	 * @param members
	 * @return
	 */
	public Long zrem(String key, String... members) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.zrem(key, members);
			}
		};
		return executor.execute("zrem", key, StringUtil.join(members));
	}

	/**
	 * @param key
	 * @param val
	 */
	public String set(String key, String val) {
		return this.setex(key, val, default_expire);
	}

	/**
	 *
	 * @param key
	 * @param val
	 */
	public void putObject(String key, Object val) {
		if (val != null) {
			set(key, JsonUtil.object2String(val));
		}
	}

	/**
	 *
	 * @param key
	 * @param val
	 */
	public void putObject(String key, Object val, int cacheExpire) {
		if (val != null) {
			setex(key, JsonUtil.object2String(val), cacheExpire);
		}
	}

	/**
	 *
	 * @param key
	 * @param val
	 */
	public void putList(String key, List val) {
		if (val != null) {
			set(key, JsonUtil.object2String(val));
		}
	}

	/**
	 *
	 * @param key
	 * @param val
	 */
	public <T> List<T> getList(String key, Class<T> clz) {
		String result = get(key);
		return JsonUtil.str2List(result, clz);
	}

	/**
	 *
	 * @param key
	 * @param clz
	 * @return
	 */
	public <T> T getObject(String key, Class<T> clz) {
		String result = get(key);
		return JsonUtil.str2Object(result, clz);
	}

	/**
	 * 取值并赋值
	 *
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return 原值
	 */
	public String getSet(String key, String value) {
		RedisExecutor<String> executor = new RedisExecutor<String>() {
			@Override
			String action(Jedis redis) {
				return redis.getSet(key, value);
			}
		};
		return executor.execute("getSet", key, value);
	}

	/**
	 *
	 * @param key
	 * @param filed
	 * @param val
	 * @param expire
	 */
	public Long hset(String key, String filed, String val, int expire) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				Long result = redis.hset(key, filed, val);
				if (expire > 0) {
					redis.expire(key, expire);
				}
				return result;
			}
		};
		return executor.execute("hset", key, filed, val);
	}

	/**
	 * 
	 * @param key
	 * @param filed
	 * @param arr
	 * @param expire
	 * @return
	 */
	public Long hsetarr(String key, String filed, byte[] arr, int expire) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				Long result = redis.hset(getString2Utf8(key), getString2Utf8(filed), arr);
				if (expire > 0) {
					redis.expire(key, expire);
				}
				return result;
			}
		};
		return executor.execute("hsetarr", key, filed);
	}

	
	/**
	 * 
	 * @param key
	 * @param filed
	 * @param str
	 * @return
	 */
	public Long hsetarr(String key, String filed, String str) {
		return this.hsetarr(key, filed, this.getString2Utf8(str), -1);
	}
	
	
	/**
	 * 
	 * @param key
	 * @param filed
	 * @param arr
	 * @return
	 */
	public Long hsetarr(String key, String filed, byte[] arr) {
		return this.hsetarr(key, filed, arr, -1);
	}

	/**
	 * 
	 * @param key
	 * @param filed
	 * @param arr
	 * @return
	 */
	public <T> T hgetarr(String key, String filed, Class<?> cls) {
		RedisExecutor<T> executor = new RedisExecutor<T>() {
			@Override
			T action(Jedis redis) {
				byte[] arr = redis.hget(getString2Utf8(key), getString2Utf8(filed));
				if (arr != null) {
					if (ClassUtil.isBaseDataType(cls)) {
						return (T) new String(arr);
					}
					return (T) fstSerialize.deserialize(arr, cls);
				}
				return null;
			}
		};
		return executor.execute("hget", key, filed);
	}

	/**
	 *
	 * @param key
	 */
	public Long hset(String key, String filed, String val) {
		return this.hset(key, filed, val, -1);
	}

	/**
	 *
	 * @param key
	 * @param data
	 */
	public String hmset(String key, Map<String, String> data, int expire) {
		RedisExecutor<String> executor = new RedisExecutor<String>() {
			@Override
			String action(Jedis redis) {
				String result = redis.hmset(key, data);
				if (expire > 0) {
					redis.expire(key, expire);
				}
				return result;
			}
		};
		return executor.execute("hmset", key, JsonUtil.object2String(data));
	}

	/**
	 *
	 * @param key
	 * @param data
	 */
	public String hmset(String key, Map<String, String> data) {
		return this.hmset(key, data, -1);
	}

	/**
	 * @设置存活时间
	 * @filed
	 */
	public Long hsetex(String key, String filed, String val, int seconds) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				Long result = redis.hset(key, filed, val);
				redis.expire(key, seconds);
				return result;
			}
		};
		return executor.execute("hset", key, filed, val);
	}

	/**
	 *
	 * @param key
	 * @param field
	 * @param value
	 */
	public Long hincrBy(String key, String field, Long value) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.hincrBy(key, field, value);
			}
		};
		return executor.execute("hincrBy", key, field, StringUtil.obj2String(value));
	}

	/**
	 *
	 * @param key
	 * @param field
	 */
	public Long hdel(String key, String... fields) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.hdel(key, fields);
			}
		};
		return executor.execute("hdel", key, StringUtil.join(fields));
	}

	/**
	 *
	 * @param key
	 */
	public Long del(byte[]... keys) {
		return this.del(RedisKeyConstants.REDIS_DB_INDEX_0, keys);
	}

	public Long del(int dbIndex, byte[]... keys) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>(dbIndex) {
			@Override
			Long action(Jedis redis) {
				return redis.del(keys);
			}
		};
		return executor.execute("del", StringUtil.join(keys));
	}

	/**
	 *
	 * @param key
	 */
	public Long del(String... keys) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.del(keys);
			}
		};
		return executor.execute("del", StringUtil.join(keys));
	}

	/**
	 * @desc 根据keys正则删除对应的key
	 * @param key
	 */
	public Long delByPattern(String key) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				Set<String> keysSet = redis.keys(key);
				if (keysSet != null && !keysSet.isEmpty()) {
					keysSet.parallelStream().forEach(v -> redis.del(v));
				}
				return 0l;
			}
		};
		return executor.execute("delByPattern", key);
	}

	/**
	 * @desc redis sub 消息
	 */
	public void subscribe(JedisPubSub sub, String... channels) {
		RedisExecutor<Boolean> executor = new RedisExecutor<Boolean>() {
			@Override
			Boolean action(Jedis redis) {
				redis.subscribe(sub, channels);
				return true;
			}
		};
		executor.execute("subscribe", StringUtil.join(channels));
	}

	/**
	 * @desc redis sub 消息
	 */
	public void psubscribe(JedisPubSub sub, String... channels) {
		RedisExecutor<Boolean> executor = new RedisExecutor<Boolean>() {
			@Override
			Boolean action(Jedis redis) {
				redis.psubscribe(sub, channels);
				return true;
			}
		};
		executor.execute("psubscribe", StringUtil.join(channels));
	}

	/**
	 * @desc redis publish 消息
	 */
	public Long publish(String channel, String message) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.publish(channel, message);
			}
		};
		return executor.execute("publish", channel, message);
	}

	/**
	 * @desc redis publish 消息
	 */
	public Long publish(String channel, Object message) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.publish(StringUtil.getString2Utf8(channel), fstSerialize.serialize(message));
			}
		};
		return executor.execute("publish", channel);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public Set<String> keys(String key) {
		RedisExecutor<Set<String>> executor = new RedisExecutor<Set<String>>() {
			@Override
			Set<String> action(Jedis redis) {
				return redis.keys(key);
			}
		};
		return executor.execute("keys", key);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Set<byte[]> keys(byte[] arr) {
		RedisExecutor<Set<byte[]>> executor = new RedisExecutor<Set<byte[]>>() {
			@Override
			Set<byte[]> action(Jedis redis) {
				Set<byte[]> set = redis.keys(arr);
				if (set != null && !set.isEmpty()) {
					return set;
				}
				return null;
			}
		};
		return executor.execute("keys");
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public Set<String> hkeys(String key) {
		RedisExecutor<Set<String>> executor = new RedisExecutor<Set<String>>() {
			@Override
			Set<String> action(Jedis redis) {
				return redis.hkeys(key);
			}
		};
		return executor.execute("hkeys", key);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public List<String> hvals(String key) {
		RedisExecutor<List<String>> executor = new RedisExecutor<List<String>>() {
			@Override
			List<String> action(Jedis redis) {
				return redis.hvals(key);
			}
		};
		return executor.execute("hvals", key);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetAll(String key) {
		RedisExecutor<Map<String, String>> executor = new RedisExecutor<Map<String, String>>() {
			@Override
			Map<String, String> action(Jedis redis) {
				return redis.hgetAll(key);
			}
		};
		return executor.execute("hgetAll", key);
	}

	/**
	 *
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(String key, String field) {
		RedisExecutor<String> executor = new RedisExecutor<String>() {
			@Override
			String action(Jedis redis) {
				return redis.hget(key, field);
			}
		};
		return executor.execute("hget", key, field);
	}

	/**
	 * @desc 判断是否存在该key
	 * @param key
	 * @param field
	 * @return
	 */
	public Boolean hexistsarr(String key, String field) {
		RedisExecutor<Boolean> executor = new RedisExecutor<Boolean>() {
			@Override
			Boolean action(Jedis redis) {
				return redis.hexists(getString2Utf8(key), getString2Utf8(field));
			}
		};
		return executor.execute("hexistsarr", key, field);
	}

	/**
	 * @desc 判断是否存在该key
	 * @param key
	 * @param field
	 * @return
	 */
	public Boolean hexists(String key, String field) {
		RedisExecutor<Boolean> executor = new RedisExecutor<Boolean>() {
			@Override
			Boolean action(Jedis redis) {
				return redis.hexists(key, field);
			}
		};
		return executor.execute("hexists", key, field);
	}

	/**
	 * 删除表中字段
	 *
	 * @param cacheName
	 *            缓存名称
	 * @param key
	 *            Map中的key
	 */
	public Long hdel(String cacheName, String key) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.hdel(cacheName, key);
			}
		};
		return executor.execute("hdel", cacheName, key);
	}

	/**
	 * @desc 设置过期时间
	 * @param key
	 *            redisKey值 time 过期时间 单位：秒
	 */
	public Long expire(String key, Integer time) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.expire(key, time);
			}
		};
		return executor.execute("expire", key, String.valueOf(time));
	}

	/**
	 * @desc 设置过期时间
	 * @param key
	 *            redisKey值 date 到期时间
	 */
	public Long expireAt(String key, Date date) {
		Long time = date.getTime();
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.expireAt(key, time);
			}
		};
		return executor.execute("expireAt", key, DateUtil.formatDate(date, DateUtil.YYYYMMDDHHMMSS_SPLIT));
	}

	/**
	 * @desc 设置过期时间
	 * @param key
	 *            redisKey值 date 到期时间
	 */
	public Long expireAt(byte[] key, Date date) {
		Long time = date.getTime();
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.expireAt(key, time);
			}
		};
		return executor.execute("expireAt", String.valueOf(key),
				DateUtil.formatDate(date, DateUtil.YYYYMMDDHHMMSS_SPLIT));
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public Long pttl(String key) {
		RedisExecutor<Long> executor = new RedisExecutor<Long>() {
			@Override
			Long action(Jedis redis) {
				return redis.pttl(key);
			}
		};
		return executor.execute("pttl", key);
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	private Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = redisPool.getResource();
		} catch (Exception e) {
			logger.error("--------------------------------- getResource error, disconnected，msg:{}", e.getMessage());
		}
		return jedis;
	}

	/**
	 *
	 * @param resource
	 */
	public void returnResource(Jedis resource) {
		if (resource != null) {
			if (redisPool != null) {
				redisPool.returnResource(resource);
			}
		}
	}

	/**
	 *
	 * @param obj
	 * @return
	 */
	private byte[] getString2Utf8(Object obj) {
		return StringUtil.getString2Utf8(obj);
	}

	/**
	 * @author Administrator
	 * @desc redis 执行器
	 */
	public static interface RedisTransactionExecutor {

		/**
		 * @desc 过域之前操作
		 */
		public boolean preExecute(Object key, Object obj);

		default public DomainExecResult innerExecute(Object key, Object obj) {
			boolean pre = preExecute(key, obj);
			if (pre)
				obj = execute(key, obj);
			return postExecute(key, obj);
		}

		/**
		 * @desc 执行单个domain实例方法
		 */
		public Object execute(Object key, Object obj);

		/**
		 * @desc 过域之后操作
		 */
		public DomainExecResult postExecute(Object key, Object obj);

		/**
		 * @desc 执行成功之后操作
		 */
		public Object callSuccess() throws Exception;

		/**
		 * @desc 执行失败操作
		 */
		public void callFailed() throws Exception;

		/**
		 * 
		 * @desc 清除上下文环境信息
		 */
		public void clearContext();

		public static class DomainExecResult {
			private boolean storage;

			private Object target;

			private int expire; // 过期时间

			private boolean setExpire; // 是否需要设置过期时间

			public void setTarget(Object target) {
				this.target = target;
			}

			public Object getTarget() {
				return target;
			}

			public boolean isStorage() {
				return storage;
			}

			public void setStorage(boolean storage) {
				this.storage = storage;
			}

			public int getExpire() {
				return expire;
			}

			public void setExpire(int expire) {
				this.expire = expire;
			}

			public boolean isSetExpire() {
				return setExpire;
			}

			public void setSetExpire(boolean setExpire) {
				this.setExpire = setExpire;
			}

		}
	}

	/**
	 * @author Administrator
	 * @desc redis 执行器
	 */
	private abstract class RedisExecutor<T> {

		private StringBuilder remark = new StringBuilder();

		private Jedis redis;

		private int dbIndex;

		public RedisExecutor(int dbIndex) {
			this.dbIndex = dbIndex;
			Long start = System.currentTimeMillis();
			redis = getJedis();
			remark.append("getResourceTime=" + (System.currentTimeMillis() - start));
		}

		public RedisExecutor() {
			this(RedisKeyConstants.REDIS_DB_INDEX_0);
		}

		abstract T action(Jedis redis);

		public T execute(String operation, String... param) {
			T result = null;
			StopWatch watch = new StopWatch();
			long duration =0l;
			try { 
				watch.start();
				redis.select(dbIndex);
				String content = "param=" + StringUtil.join(param) + ", operation=" + operation;
				remark.append(content); 
				result = action(redis);  
				duration = watch.getTime();
				logger.debug("RedisExecutor.remark:{},result:{};content:{};duration:{}", remark, result, content, duration); 
				return result;
			} catch(Exception ex){
				logger.error("RedisExecutor.error:{}",ExceptionUtil.getStackTrace(ex));
				return null;
			}
			finally {
				TraceAnnotation annotation = new TraceAnnotation();
				annotation.setResouceType(TraceConstant.TRACE_REDIS);
				annotation.setOperation(remark.toString());
				annotation.setDestination(duration==0?watch.getTime():duration);
				annotation.setResouceURI(redisPool.getName() + "-{" + redisPool.getHost() + "}:{" + redisPool.getPort() + "}-dbIndex:{" + this.dbIndex + "}");
				TraceTransfer.getTrace().addAnnotation(annotation); // traceInfo
				if (redis != null) { 
					returnResource(redis);
				}
			}
		}
	}

	/**
	 * @param RedisCommandInvoker<?
	 *            extends MhsBaseDomain> invoker
	 * @param keys
	 *            执行的keys
	 * @param callBack
	 *            触发的回调操作
	 * @desc 执行command指令
	 */
	public Object execStepCmds(RedisTransactionExecutor transactionExecutor, int dbIndex, byte[]... keys)
			throws BusinessException {
		RedisExecutor<Object> executor = new RedisExecutor<Object>(dbIndex) {
			boolean hasError = false;

			@Override
			Object action(Jedis redis) {
				int i = 5;
				while (i-- >= 0) // 重试5次
				{
					String result = redis.watch(keys);
					if (StringUtil.equalsIgnoreCase(result, "ok")) {
						List<byte[]> arr = redis.mget(keys);
						Transaction tr = redis.multi();
						try {
							// 获取域内数据
							int index = 0;
							for (byte[] key : keys) {
								byte[] temp = arr.get(index++);
								Object obj = null;
								if (temp != null && temp.length > 0) {
									obj = fstSerialize.deserialize(temp, Object.class);
								}
								DomainExecResult re = transactionExecutor.innerExecute(key, obj);
								if (re != null && re.isStorage()) {
									tr.set(key, fstSerialize.serialize(re.getTarget()));
									if (re.setExpire)
										tr.expire(key, re.expire);
								}
							}
							List<Object> obj = tr.exec();
							hasError = (obj == null || obj.isEmpty()); // 执行任务失败 // // // ，抛出异常前端响应
							logger.info("tr.exec.result.resultList:{}", JsonUtil.object2String(obj));
							break;
						} catch (Exception ex) {
							tr.discard();
							transactionExecutor.clearContext();
							logger.error("executeCommand.e:{}", ExceptionUtil.getStackTrace(ex));
							throw ex;
						}
					} else {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							logger.error("executeCommand.e:{}", ExceptionUtil.getStackTrace(e));
						}
					}
				}
				try {
					if (hasError) {
						transactionExecutor.callFailed();
						throw new BusinessException("E60044");
					}
					return transactionExecutor.callSuccess();
				} catch (BusinessException ex) {
					logger.error("transactionExecutor.callSuccess.BusinessException.msg:{}", ExceptionUtil.getStackTrace(ex));
					throw ex;
				}catch (Exception ex) {
					logger.error("transactionExecutor.callSuccess.error:{}", ExceptionUtil.getStackTrace(ex));
					throw new BusinessException("E00000");
				} finally {
					transactionExecutor.clearContext();
				}
			}
		};
		return executor.execute("executeStepCmds",
				JsonUtil.object2String(fstSerialize.deserialize(keys, Object.class)));
	}
}
