package com.yz.job.mq;

/**
 * 
 * @author Administrator
 *
 * @param <T>
 */
@FunctionalInterface
public interface MQConsumerCallBack<T> {

	public void execute(T t);
}
