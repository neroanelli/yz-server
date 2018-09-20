package com.yz.network.examination.process;

import java.util.Optional;

import com.google.common.collect.Lists;

/**
 * 
 * @desc 网报表单流程
 * @author lingdian
 *
 */
@SuppressWarnings("serial")
public class NetWorkExamFlow implements java.io.Serializable {

	private String frmId; // 表单Id

	private NetWorkExamFlowEnum nextFlow; // 下一步流转

	public String getFrmId() {
		return frmId;
	}

	public void setFrmId(String frmId) {
		this.frmId = frmId;
	}

	public void setNextFlow(NetWorkExamFlowEnum nextFlow) {
		this.nextFlow = nextFlow;
	}

	public NetWorkExamFlowEnum getNextFlow() {
		return nextFlow;
	}

	public enum NetWorkExamFlowEnum {
		BASE(0, "基础表单"), // 基础表单
		UN_INIT(1, "待初始化"), // 待初始化
		UN_BIND(2, "待绑定"), // 待绑定
		UN_COLLECTION(3, "待收集"), // 待收集
		UN_DIPLOMA(4, "待学历验证"), // 待学历验证
		UN_PAY(5, "待支付"), // 待支付
		UN_CONFIRM(6, "待确认");// 待确认

		private int flowId; // 步骤

		private String remark; // 备注

		private NetWorkExamFlowEnum(int flowId, String remark) {
			this.flowId = flowId;
			this.remark = remark;
		}

		/**
		 * @desc 根据流程Id获取当前的枚举
		 * @param flowId
		 * @return
		 */
		public static NetWorkExamFlowEnum parseNetWorkExamFlow(int flowId) {
			Optional<NetWorkExamFlowEnum> optional = Lists.newArrayList(values()).parallelStream()
					.filter(v -> v.getFlowId() == flowId).findFirst();
			if (optional.isPresent()) {
				return optional.get();
			}
			throw NetWorkExamFlowException.makeFrmStatusException();
		}

		public void setFlowId(int flowId) {
			this.flowId = flowId;
		}

		public int getFlowId() {
			return flowId;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

	}
}
