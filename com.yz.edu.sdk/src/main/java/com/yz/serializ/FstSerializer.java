package com.yz.serializ;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.yz.util.ExceptionUtil;

/**
 * 
 * @author Administrator
 *
 */
public class FstSerializer {

	private static final Logger logger = LoggerFactory.getLogger(FstSerializer.class);
	private static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration().setForceSerializable(false);

	private static FstSerializer instance = null;

	/**
	 * 
	 * @return
	 */
	public static FstSerializer getInstance() {
		if (instance == null) {
			synchronized (FstSerializer.class) {
				if (instance == null) {
					instance = new FstSerializer();
				}
			}
		}
		return instance;
	}

	private FstSerializer() {
	}

	/**
	 * 
	 * @param object
	 * @return
	 */
	public byte[] serialize(Object object) {
		if (object == null) {
			return null;
		}
		byte[]arr = null;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		FSTObjectOutput out = conf.getObjectOutput(stream);
		try {
			out.writeObject(object);
			out.flush(); 
			arr = stream.toByteArray();
		} catch (IOException e) { 
			logger.error("serialize error:{}", ExceptionUtil.getStackTrace(e)); 
		} finally {
			IOUtils.closeQuietly(stream); 
		}
		return arr;
	}

	/**
	 * 
	 * @param arr
	 * @param cls
	 * @return
	 */
	public <T> T deserialize(byte[] arr, Class<?> cls) {
		if (arr == null) {
			return null;
		}
		T result = null;
		ByteArrayInputStream stream = new ByteArrayInputStream(arr);
		try {
			FSTObjectInput in = conf.getObjectInput(stream);
			result = (T) in.readObject();
		} catch (Exception e) { 
			logger.error("deserialize error:{}", ExceptionUtil.getStackTrace(e));
		} finally {
			IOUtils.closeQuietly(stream); 
		}
		return result;
	}

	/**
	 * 
	 * @param arrs
	 * @param cls
	 * @return
	 */
	public <T> List<T> deserialize(byte[][] arrs, Class<?> cls) {
		List<T> result = Lists.newArrayList();
		try {
			for (byte[] arr : arrs) {
				T obj = deserialize(arr, cls);
				if (obj != null)
					result.add(obj);
			}
		} catch (Exception e) {
			logger.error("deserialize error:{}", ExceptionUtil.getStackTrace(e));
		}
		return result;
	}

}
