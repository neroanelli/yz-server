package com.yz.network.examination.task;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.dangdang.ddframe.job.api.ElasticJob; 
import com.google.common.collect.Lists;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @desc
 * @author lingdian
 *
 */
public abstract class AbstractNetworkExamJob implements ElasticJob {
	
	
	protected Logger logger = LoggerFactory.getLogger(AbstractNetworkExamJob.class);

	private String desc; // 任务描述

	private String corn; // 任务执行表达式

	private int shardingTotalCount; // 任务的分片数

	private String jobType; // 任务类型

	private List<NetWorkExamParam> jobParams;

	public AbstractNetworkExamJob() {
		this.jobParams = Lists.newArrayList();
	}

	public void addJobParam(String key, Object val) {
		NetWorkExamParam param = new NetWorkExamParam();
		param.setName(key);
		param.setValue(val);
		param.setSeq(this.jobParams.size() + 1);
		this.jobParams.add(param);
	}

	public Object getJobParam(String key) {
		if (jobParams == null || jobParams.isEmpty()) {
			return StringUtil.EMPTY;
		}
		Optional<NetWorkExamParam> optional = jobParams.parallelStream()
				.filter(v -> StringUtil.equalsIgnoreCase(v.getName(), key)).findFirst();
		if (optional.isPresent())
			optional.get().getValue();
		return StringUtil.EMPTY;
	}

	public List<NetWorkExamParam> getJobParams() {
		return this.jobParams;
	}

	public String getJobParamStr() {
		if (jobParams == null || jobParams.isEmpty()) {
			return StringUtil.EMPTY;
		}
		return JsonUtil.object2String(jobParams);
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobType() {
		return jobType;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCorn() {
		return corn;
	}

	public void setCorn(String corn) {
		this.corn = corn;
	}

	public int getShardingTotalCount() {
		return shardingTotalCount;
	}

	public void setShardingTotalCount(int shardingTotalCount) {
		this.shardingTotalCount = shardingTotalCount;
	}

	public class NetWorkExamParam implements java.io.Serializable {
		private String name;

		private Object value;

		private int seq;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public int getSeq() {
			return seq;
		}

		public void setSeq(int seq) {
			this.seq = seq;
		}
	}

}
