package com.yz.network.examination.process;

import com.yz.exception.BusinessException;
import com.yz.network.examination.constants.NetWorkExamConstants;

/**
 * @desc 网报表单流程处理异常
 * @author lingdian
 *
 */
public class NetWorkExamFlowException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NetWorkExamFlowException(String errCode) {
		super(errCode);
	}

	/**
	 * @desc 网报表单流程状态异常
	 * @return
	 */
	public static NetWorkExamFlowException makeFrmStatusException() {
		return new NetWorkExamFlowException("");
	}
	
	
	/**
	 * @desc makeFlowIdUnFoundException
	 * @return
	 */
	public static NetWorkExamFlowException makeFlowIdUnFoundException() {
		return new NetWorkExamFlowException(NetWorkExamConstants.FLOW_UNFOUND_EXCEPTION);
	}

}
