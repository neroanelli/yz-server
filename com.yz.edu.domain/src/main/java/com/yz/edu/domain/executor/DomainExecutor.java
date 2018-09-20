package com.yz.edu.domain.executor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.yz.edu.cmd.BaseCommand;
import com.yz.edu.domain.YzBaseDomain;
import com.yz.edu.domain.hook.YzDomainExecuteHook;
import com.yz.exception.BusinessException;
import com.yz.exception.CustomException;
import com.yz.exception.IRuntimeException;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil; 

/**
 * 
 * @author Administrator
 *
 */
public class DomainExecutor {

	private static Logger logger = LoggerFactory.getLogger(DomainExecutor.class);

	private static Map<String, Method> methodMap = Maps.newConcurrentMap();

	/**
	 * 
	 * @param domain
	 * @param cmd
	 * @return
	 */
	public static YzBaseDomain callDomainCmd(YzBaseDomain domain, BaseCommand cmd) throws IRuntimeException {
		String methodName = cmd.getMethodName();
		methodName = domain.getClass().getName() + "." + methodName;
		Method me = methodMap.get(methodName);
		if (me == null) {
			me = getCommandMethod(domain, cmd);
			methodMap.put(methodName, me);
		}
		try {
			return (YzBaseDomain) me.invoke(domain, cmd);
		} catch (InvocationTargetException e) {
			logger.error("execDomainCmd.error:{},methodInfo:{}", ExceptionUtil.getStackTrace(e), me);
			throw (IRuntimeException) e.getTargetException();
		} catch (Exception e) {
			logger.error("execDomainCmd.error:{},methodInfo:{}", ExceptionUtil.getStackTrace(e), me);
			throw new BusinessException("000000");
		}
	}

	/**
	 * 
	 * @param domain
	 * @param cmd
	 * @return
	 */
	public static YzBaseDomain execDomainCmd(YzBaseDomain domain, BaseCommand cmd) throws BusinessException {
		return new YzDomainExecuteHookExecutor(domain, cmd) {
			@Override
			public YzBaseDomain action() {
				return callDomainCmd(domain, cmd);
			}
		}.execute();
	}

	/**
	 * 
	 * 
	 * @param domain
	 * @return
	 */
	private static Method getCommandMethod(YzBaseDomain domain, BaseCommand cmd) {
		try {
			return domain.getClass().getDeclaredMethod(cmd.getMethodName(), cmd.getClass());
		} catch (Exception e) {
			logger.error("getCommandMethod.ex:{}", e);
		}
		logger.error("not found " + cmd.getMethodName() + " in " + domain.getClass().getName());
		throw new CustomException("not found " + cmd.getMethodName() + " in " + domain.getClass().getName());
	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	private static abstract class YzDomainExecuteHookExecutor {
		private Set<YzDomainExecuteHook> hooks;

		private YzBaseDomain domain;

		private BaseCommand cmd;

		public YzDomainExecuteHookExecutor(YzBaseDomain domain, BaseCommand cmd) {
			this.domain = domain;
			this.hooks = cmd.getDomainExecHooks();
			this.hooks.add(new LoggingExecuteHookExecutor());
			this.cmd = cmd;
		}

		/**
		 * @desc
		 * @return
		 */
		public YzBaseDomain execute() {
			for (YzDomainExecuteHook hook : hooks) {
				hook.preExecute(domain, this.cmd);
			}
			YzBaseDomain obj = action();
			for (YzDomainExecuteHook hook : hooks) {
				hook.postExecute(domain, this.cmd);
			}
			return obj;
		}

		/**
		 * 
		 * @return
		 */
		public abstract YzBaseDomain action();
	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	private static class LoggingExecuteHookExecutor implements YzDomainExecuteHook {
		private Logger logger = LoggerFactory.getLogger(LoggingExecuteHookExecutor.class);

		/**
		 * 
		 * @param domain
		 *            执行Command前domain实例
		 * @param cmd
		 */
		public void preExecute(YzBaseDomain domain, BaseCommand cmd) {
			logger.info("preExecute.cmd:{},domain:{}", JsonUtil.object2String(cmd), JsonUtil.object2String(domain));
		}

		/**
		 * 
		 * @param domain
		 *            执行Command的domain实例
		 * @param cmd
		 */
		public void postExecute(YzBaseDomain domain, BaseCommand cmd) {
			logger.info("postExecute.cmd:{},domain:{}", JsonUtil.object2String(cmd), JsonUtil.object2String(domain));
		}
	}
}
