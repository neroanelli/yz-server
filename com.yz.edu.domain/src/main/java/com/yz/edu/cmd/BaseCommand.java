package com.yz.edu.cmd;

 
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder; 

import com.google.common.collect.Sets;
import com.yz.edu.constants.YzDomainConstants;
import com.yz.edu.domain.hook.YzDomainExecuteHook;
import com.yz.model.WechatMsgVo;
import com.yz.serializ.FstSerializer;
import com.yz.util.OSUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @desc domain cmd 基类
 * @author lingdian
 *
 */
public abstract class BaseCommand implements java.io.Serializable, Comparable<BaseCommand>, YzDomainConstants {

	/**
	 *
	 */ 
	private static transient  final long serialVersionUID = 1L;

 
	private transient Set<YzDomainExecuteHook> domainExecHooks = null; // domain
	// 执行的钩子函数

	private static final transient FstSerializer fstSerializer = FstSerializer.getInstance();

	private Date createDate; // 创建时间

	private String addr; // 创建地址

	private boolean asyn = false; // 是否异步方式 commnad处理结果是否未异步

	private String topic; // Command 订阅的topic 对应相应的domain的消费

	private int operaton = DOMAIN_OPERATION_UPDATE; // 0 update
	// 1 create
	private int step = 1; // 步骤 根据step分组 分阶段提交cmd命令

	private int version = COMMON_DOMAIN_VERSION; // 版本号

	private boolean runNow = false; // 设置为false 由domain容器提供原子性操作

	private boolean trace = true; // 是否记录trace日志

	private boolean dataCollectCmd = false; // 是否数据收集指令

	private boolean mustSuccess = true; // 是否必须成功

	private int status = COMMAND_STATUS_SUCCESS; // 1 处理中 2 成功 3 失败

	private String errorCode; // 错误代码

	private boolean pushMsg = false; // 是否推送消息

	public BaseCommand() {
		this.addr = OSUtil.getIp();
		this.createDate = new Date();
		this.domainExecHooks = Sets.newHashSet();
	}

	public boolean isPushMsg() {
		return pushMsg;
	}

	public void setPushMsg(boolean pushMsg) {
		this.pushMsg = pushMsg;
	}

	public void addDomainExecHooks(YzDomainExecuteHook hook) {
		if (hook != null) {
			domainExecHooks.add(hook);
		}
	}

	public Set<YzDomainExecuteHook> getDomainExecHooks() {
		return domainExecHooks;
	}

	public FstSerializer getFstSerializer() {
		return fstSerializer;
	}

	public boolean isMustSuccess() {
		return mustSuccess;
	}

	public void setMustSuccess(boolean mustSuccess) {
		this.mustSuccess = mustSuccess;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @desc 设置错误代码
	 * @param errorCode
	 */
	public void setErrorCode(String errorCode) {
		if (!StringUtil.isEmpty(errorCode)) {
			this.errorCode = errorCode;
			this.status = COMMAND_STATUS_FAILED;
		}

	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getOperaton() {
		return operaton;
	}

	public void setOperaton(int operaton) {
		this.operaton = operaton;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public boolean isAsyn() {
		return asyn;
	}

	public void setAsyn(boolean asyn) {
		this.asyn = asyn;
	}

	public Date getCreateDate() {
		return createDate == null ? null : (Date) createDate.clone();
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	@Override
	public int compareTo(BaseCommand cmd) {
		return this.step >= cmd.getStep() ? 1 : -1;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @desc 域聚合根实体对象
	 */
	public abstract Object getId();

	/**
	 * @desc 制定域内的具体方法
	 */
	public abstract String getMethodName();

	/**
	 * @desc 制定域的类型
	 */
	public abstract Class<?> getDomainCls();

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public boolean isRunNow() {
		return runNow;
	}

	public void setRunNow(boolean runNow) {
		this.runNow = runNow;
	}

	public boolean isDataCollectCmd() {
		return dataCollectCmd;
	}

	public void setDataCollectCmd(boolean dataCollectCmd) {
		this.dataCollectCmd = dataCollectCmd;
	}

	public boolean isTrace() {
		return trace;
	}

	public void setTrace(boolean trace) {
		this.trace = trace;
	}

	public WechatMsgVo getPushMsgVo() {
		return null;
	}

	public boolean isSuccess() {
		return StringUtil.isEmpty(errorCode);
	}

}
