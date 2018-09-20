package com.yz.edu.domain.callback;

/**
 * 
 * @desc 域指令执行成功的回调操作
 * 
 * @author Administrator
 *
 * @param <T>
 */
@FunctionalInterface
public interface DomainCallBack {

	/**
	 *
	 * @desc 执行指令返回的成功的域实例
	 *
	 */
	Object callSuccess();

	
	default void callFailed() { }

	public static DomainCallBack empty = () -> {
		return true;
	};

	default DomainCallBack withResult(DomainResult result) {
		return () -> {
			Object r = this.callSuccess();
			result.set(r);
			return r;
		};
	}

	default DomainCallBack andThen(DomainCallBack callBack) {
		return () -> {
			this.callSuccess();
			return callBack.callSuccess();
		};
	}

	public static class DomainResult {
		private Object object;

		private DomainResult() {
		}

		public static DomainResult create() {
			return new DomainResult();
		}

		@SuppressWarnings("WeakerAccess")
		public void set(Object o) {
			object = o;
		}

		public Object get() {
			return object;
		}
	}
}
