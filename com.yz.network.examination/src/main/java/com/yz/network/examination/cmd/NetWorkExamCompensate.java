package com.yz.network.examination.cmd;

import com.yz.network.examination.form.BaseNetWorkExamForm;

/**
 * @desc 网报补偿命令
 * @author lingdian
 *
 */
@FunctionalInterface
public interface NetWorkExamCompensate {

	/**
	 * @desc 执行补偿操作
	 * @param frm
	 *            当前form对象
	 */
	public boolean doCompensate(BaseNetWorkExamForm frm);

	/**
	 * @desc 执行补偿操作
	 * @param frm
	 *            当前form对象
	 * @param exType
	 *            错误代码
	 */
	default public boolean doCompensate(BaseNetWorkExamForm frm, CompensateCallBack callBack) {
		boolean bol = this.doCompensate(frm);
		if (callBack != null) {
			if (bol) {
				callBack.callBack(frm);
			}
		}
		return bol;
	}

	/**
	 * @desc 执行补偿成功，执行的回调函数
	 * @author lingdian
	 *
	 */
	@FunctionalInterface
	public interface CompensateCallBack {

		/**
		 * 
		 * @desc 补偿成功执行回调函数
		 * 
		 */
		public void callBack(BaseNetWorkExamForm frm);
	}
}
