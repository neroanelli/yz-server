package com.yz.network.examination.handler;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.exception.BusinessException;
import com.yz.network.examination.cmd.NetWorkExamCompensate.CompensateCallBack;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.process.NetWorkExamFlow.NetWorkExamFlowEnum;
import com.yz.redis.RedisService;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @desc 网报系统响应处理
 * @author lingdian
 *
 */
@FunctionalInterface
public interface NetWorkExaminationHandler {

	public Logger logger = LoggerFactory.getLogger(NetWorkExaminationHandler.class);

	/**
	 * @desc 将http返回结果转化为字符串
	 * @param entity
	 * @return
	 */
	default public String toStringEntity(HttpEntity entity) {
		try {
			return EntityUtils.toString(entity, "utf-8");
		} catch (ParseException | IOException e) {
			logger.error("toStringEntity.error:{}", ExceptionUtil.getStackTrace(e));
			return StringUtil.EMPTY;
		}
	}

	/**
	 * @desc 将http返回结果转化为byte数组
	 * @param entity
	 * @return
	 */
	default public byte[] toByte(HttpEntity entity) {
		try {
			return EntityUtils.toByteArray(entity);
		} catch (ParseException | IOException e) {
			logger.error("toByte.error:{}", ExceptionUtil.getStackTrace(e));
			return null;
		}
	}
    
	/**
	 * 
	 * @desc 表单执行成功  根据具体的业务回写状态
	 * @param frmId
	 * @param flow
	 */
	default public void postNetWorkFrmFlow(String frmId,NetWorkExamFlowEnum flow) {
          RedisService.getRedisService().hsetarr(frmId, NetWorkExamConstants.NETWORK_EXAM_STEP, 
        		  String.valueOf(flow.getFlowId()));
	}

	/**
	 * @desc 处理表单提交的返回结果
	 * @param form
	 * @param entity
	 * @return
	 */
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form, HttpEntity entity) throws BusinessException;

	/**
	 * 
	 * @desc 网报表单处理结果
	 * @author Administrator
	 *
	 */
	public class NetWorkExamHandlerResult {

		private boolean isOk = true; // 是否处理成功

		private boolean isCompensate = false; // 是否同步补偿

		private boolean logFrm = true; // 是否记录表单，业务恢复使用

		private Object result; // 处理结果

		private String compensateCmd; // 补偿命令
		
		private CompensateCallBack compensateCallBack; //补偿成功回调 操作
		
		public void setCompensateCallBack(CompensateCallBack compensateCallBack) {
			this.compensateCallBack = compensateCallBack;
		}
		
		public CompensateCallBack getCompensateCallBack() {
			return compensateCallBack;
		}

		public boolean isLogFrm() {
			return logFrm;
		}

		public NetWorkExamHandlerResult setLogFrm(boolean logFrm) {
			this.logFrm = logFrm;
			return this;
		}

		public NetWorkExamHandlerResult setCompensateCmd(String compensateCmd) {
			this.compensateCmd = compensateCmd;
			return this;
		}

		public String getCompensateCmd() {
			return compensateCmd;
		}

		public boolean isOk() {
			return isOk;
		}

		public NetWorkExamHandlerResult setOk(boolean isOk) {
			this.isOk = isOk;
			return this;
		}

		public boolean isCompensate() {
			return isCompensate;
		}

		public NetWorkExamHandlerResult setCompensate(boolean isCompensate) {
			this.isCompensate = isCompensate;
			return this;
		}

		public Object getResult() {
			return result;
		}

		public NetWorkExamHandlerResult setResult(Object result) {
			this.result = result;
			return this;
		}
	}
}
