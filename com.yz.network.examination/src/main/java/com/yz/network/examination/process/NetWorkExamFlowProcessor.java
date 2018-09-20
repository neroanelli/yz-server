package com.yz.network.examination.process;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.process.NetWorkExamFlow.NetWorkExamFlowEnum;
import com.yz.redis.RedisService;
import com.yz.util.StringUtil;

/**
 * 
 * @desc 网报表单状态扭转处理器
 * @author lingdian
 *
 */
public class NetWorkExamFlowProcessor {

	private static Logger logger = LoggerFactory.getLogger(NetWorkExamFlowProcessor.class);

	/**
	 * @desc 下一步流程处理
	 * @param flow
	 * @return
	 */
	public boolean nextFlow(NetWorkExamFlow flow) throws NetWorkExamFlowException {
		String frmId = flow.getFrmId();
		String stepStr = RedisService.getRedisService().hgetarr(frmId, NetWorkExamConstants.NETWORK_EXAM_STEP,
				String.class);
		if (StringUtil.isNotBlank(stepStr)) {
			int step = NumberUtils.toInt(stepStr); // frm 当前状态
			NetWorkExamFlowEnum next = flow.getNextFlow(); // 表单流入下一个状态
			logger.info("frm.id:{};Step:{}", frmId, step);
		}
		return true;
	}

	private boolean checkFlow() {
		return false;
	}
}
