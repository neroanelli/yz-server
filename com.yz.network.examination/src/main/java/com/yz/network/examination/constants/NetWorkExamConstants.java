package com.yz.network.examination.constants;

/**
 * @desc 网报常量
 * @author Administrator
 *
 */
public interface NetWorkExamConstants {

	public static final String NETWORK_EXAM_GROUP = "NetWorkExamGroup";

	public static final String REG_NETWORK_EXAM_ACTION = "http://www.ecogd.edu.cn/cr/cgbm/ybm_save.jsp"; // 注册请求url

	public static final String BINDMOBILE_NETWORK_EXAM_ACTION = "http://www.ecogd.edu.cn/cr/kssjbdServlet"; // 绑定手机号请求url

	public static final String BASEINFO_NETWORK_EXAM_ACTION = "http://www.ecogd.edu.cn/cr/cgbm/ybcg.jsp"; // 获取报考信息请求url

	public static final String CONFIRMINFO_NETWORK_EXAM_ACTION = "http://www.ecogd.edu.cn/cr/cgbm/qrcg.jsp"; // 获取已确认信息请求url

	public static final String GETSMSCODE_NETWORK_EXAM_ACTION = "http://www.ecogd.edu.cn/cr/kssjbdServlet"; // 绑定手机号请求url

	public static final String LOGIN_NETWORK_EXAM_ACTION = "http://www.ecogd.edu.cn/cr/cgbm/login.do"; // 登录请求url

	public static final String TOPAY_NETWORK_EXAM_ACTION = "http://www.ecogd.edu.cn/cr/cgbm/reqpay.jsp"; // 去支付

	public static final String EDUCATIONCHECK_NETWORK_EXAM_ACTION = "http://www.ecogd.edu.cn/cr/cgbm/chsi_check.jsp"; // 学历校验

	public static final String VAILCODE_NETWORK_EXAM_ACTION = "http://www.ecogd.edu.cn/cr//servlet/VerifyCodeServlet"; // 网报请求url

	public static final String YYBMD_NETWORK_EXAM_NAME_ACTION = "https://chengzhao.hneao.cn/ks/ajax_operation.aspx"; // 预约报名确认点url

	public static final String NETWORK_EXAM_DIPLOMA_ACTION = "http://www.ecogd.edu.cn/cr/cgbm/chsi_xlcx.jsp"; // 学历验证

	public static final String GETAPPLYNO_NETWORK_ACTION = "http://www.ecogd.edu.cn/cr/cgbm/kssjbd.jsp";

	public static final String FINDAPPLYNO_NETWORK_ACTION = "http://www.ecogd.edu.cn/cr/cgbm/zhbmh.jsp";

	public static final String CHECK_EXAM_NAME_ACTION = "https://chengzhao.hneao.cn/ks/ajax_operation.aspx"; // 检查院校代码url

	public static final String REG_NETWORK_EXAM_NAME = "远智网报注册"; // 注册请求url
	
	public static final String UPDATE_NETWORK_EXAM_NAME = "远智网报更新信息"; // 注册请求url

	public static final String LOGIN_NETWORK_EXAM_NAME = "远智网报登录"; // 登录请求url

	public static final String ENROLL_NETWORK_EXAM_NAME = "远智网报网报"; // 网报请求url

	public static final String VAILCODE_NETWORK_EXAM_NAME = "远智网报验证码"; // 网报请求url

	public static final String TOPAY_NETWORK_EXAM_NAME = "远智网报去支付"; // 远智网报去支付url

	public static final String YYBMD_NETWORK_EXAM_NAME = "预约报名确认点"; // 预约报名确认点url

	public static final String BINDMOBILE_NETWORK_EXAM_NAME = "远智网报绑定手机号"; // 绑定手机号请求url

	public static final String NETWORK_EXAM_DIPLOMA_NAME = "远智网报学历验证"; // 绑定手机号请求url

	public static final String BASEINFO_NETWORK_EXAM_NAME = "获取报考基本信息"; // 获取报考信息请求url
	
	public static final String CONFIRMINFO_NETWORK_EXAM_NAME = "获取已确认基本信息"; // 获取已确认请求url

	public static final String GETAPPLYNO_NETWORK_NAME = "获取预报名号";

	public static final String FINDAPPLYNO_NETWORK_NAME = "找回预报名号";

	public static final String CHECK_NETWORK_NAME = "检查院校代码";

	public static final String EDUCATIONCHECK_NETWORK_EXAM_NAME = "学历验证";

	public static final String LOGIN_NETWORKEXAMINATION_HANDLER = "loginNetworkExaminationHandler";

	public static final String REG_NETWORKEXAMINATION_HANDLER = "regNetworkExaminationHandler";
	
	public static final String UPDATE_NETWORKEXAMINATION_HANDLER = "updateNetworkExaminationHandler";

	public static final String BINDMOBILE_NETWORKEXAMINATION_HANDLER = "bindMobileNetworkExaminationHandler";

	public static final String ONLINEPAY_NETWORKEXAMINATION_HANDLER = "onlinePayNetworkExaminationHandler";

	public static final String SAVEWECHAT_NETWORKEXAMINATION_HANDLER = "saveWeChatNetworkExaminationHandler";

	public static final String GETBASEINFO_NETWORKEXAMINATION_HANDLER = "getBaseInfoNetworkExaminationHandler";
	
	public static final String GETCONFIRMINFO_NETWORKEXAMINATION_HANDLER = "getConfirmInfoNetworkExaminationHandler";

	public static final String GETSMSCODE_NETWORKEXAMINATION_HANDLER = "getSmsCodeNetworkExaminationHandler";

	public static final String GETAPPLYNO_NETWORKEXAMINATION_HANDLER = "getApplyNoNetWorkExaminationHandler";

	public static final String FINDAPPLYNO_NETWORKEXAMINATION_HANDLER = "findApplyNoNetWorkExaminationHandler";

	public static final String EDUCATIONCHECK_NETWORKEXAMINATION_HANDLER = "educationCheckNetWorkExaminationHandler";

	public static final String GETEDUCATION_NETWORKEXAMINATION_HANDLER = "getEducationNetworkExaminationHandler";

	public static final String TOPAY_NETWORKEXAMINATION_HANDLER = "toPayNetworkExaminationHandler";

	public static final String WECHAT_PAY_NETWORKEXAMINATION_HANDLER = "wechatPayNetworkExaminationHandler";

	public static final String VAILCODE_NETWORKEXAMINATION_HANDLER = "vailCodeNetworkExaminationHandler";

	public static final String VAILCODE_NETWORKEXAMINATION_INTERCEPTOR = "vailCodeNetworkExaminationInterceptor";

	public static final String CORE_NETWORKEXAMINATION_INTERCEPTOR = "coreNetworkExaminationInterceptor";

	public static final String YYBMD_NETWORKEXAMINATION_HANDLER = "yybmdNetworkExaminationHandler";

	public static final String DIPLOMA_NETWORKEXAMINATION_HANDLER = "diplomaNetworkExaminationHandler";

	public static final int NETWORKEXAMINATION_UN_LOGIN = 403; // 未登陆的错误代码

	public static final String NETWORKEXAMINATION_DOMAIN = "www.ecogd.edu.cn";

	public static final String NETWORKEXAMINATION_COMPENSATE_QUEUE = "com.yz.network.exam.compensate.queue";

	public static final String NETWORKEXAMINATION_SCHEDULE_QUEUE = "com.yz.network.exam.schedule.queue";

	public static final String NETWORKEXAMINATION_DATA_QUEUE = "com.yz.network.exam.data.queue";

	public static final String NETWORKEXAMINATION_DATA_QUEUE_APPLYNO = "com.yz.network.exam.data.queue.applyno";

	public static final String NETWORKEXAMINATION_DATA_QUEUE_FIND_APPLYNO = "com.yz.network.exam.data.queue.find.applyno";

	public static final String EDUCATIONCHECK_DATA_QUEUE = "com.yz.network.education.check.data.queue";
	
	public static final String ALREADYRESERVD_DATA_QUEUE = "com.yz.network.education.alreadyreserved.data.queue";

	public static final String EDUCATIONGET_DATA_QUEUE = "com.yz.network.education.get.data.queue";

	public static final String EDUCATION_UPDATE_DATA_QUEUE = "com.yz.network.education.u.data.queue";
	//更新网报数据
	public static String EDUCATION_UPDATE_1_DATA_QUEUE = "com.yz.network.education.update.data.queue";

	public static final String UNLOGIN_NETWORKEXAMCOMPENSATE = "unLoginCompensate";

	public static final String RETRY_NETWORKEXAMCOMPENSATE = "reTryCompensate";

	public static final String CHECK_DIPLOMA_COMPENSATE = "checkDiplomaCompensate";

	public static final String FRM_COMMON_REPCODE = "queryNetworkFrmCode";

	public static final String NETWORK_EXAM_STEP = "network_exam_step";

	public static final String JOB_TYPE_DATAFLOW = "dataflow";

	public static final String JOB_TYPE_SIMPLE = "simple";

	public static final String NETWORKEXAMINATION_COMPENSATE_DESC = "网报系统补偿任务 ";

	public static final String NETWORKEXAMINATION_SCHEDULE_DESC = "网报调度表单 ";

	public static final String NETWORKEXAMINATION_SUBMIT_DESC = "网报提交表单 ";

	public static final String NETWORKEXAMINATION_BASEINFO_DESC = "网报采集学员报考基本信息任务 ";

	public static final String NETWORKEXAMINATION_GET_APPLYNO_DESC = "获取网报报名号提交表单 ";

	public static final String NETWORKEXAMINATION_CHECK_DIPLOMA_DESC = "学历验证提交表单 ";

	public static final String NETWORKEXAMINATION_GET_DIPLOMA_DESC = "首页获取学历 ";

	public static final String NETWORKEXAMINATION_UPDATE_DIPLOMA_DESC = "修改资料提交表单 ";

	public static final String NETWORKEXAMINATION_FIND_APPLYNO_DESC = "找回网报报名号提交表单 ";

	public static final short NETWORK_SUBMIT_STATUS_SUCCESS = (short) 1; // 网络表单提交成功

	public static final short NETWORK_SUBMIT_STATUS_FAILED = (short) 0; // 网络表单提交失败

	public static final int EVERY_GET_DATA_NUM = 5; // 每次获取网报的数据

	public static final String LAST_GET_MAX_ID = "last.get.max.id"; // 批量网报时最后一次取出的最大值

	public static final String FLOW_UNFOUND_EXCEPTION = "E400000"; // 流程Id暂未定义

	public static final String NETTO_PWD_PREFIX = "yz"; // 网报密码前缀

	public static final String NETTO_ADDRESS_SUFFIX = "sjqn"; // 通讯地址后缀

	public static final String GETPAY_NETWORKEXAMINATION_HANDLER = "payNetworkexaminationHandler";

	public static final String WXPAY_NETWORKEXAMINATION_URL = "http://wsjfwx.gdgpo.gov.cn/nontax-wxpay/pay_demand_result.html?scene=${deviceType}&tzsxx=${districtNo}|${unitNo}|${payNoticeNo}|${totMoney}|04445|55628||${payerName}|||||";

}
