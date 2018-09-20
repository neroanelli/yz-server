package com.yz.network.examination.handler;

 
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.croe.util.RegexUtil;
import com.yz.network.examination.form.BaseNetWorkExamForm; 
import com.yz.network.examination.form.GetConfrimInfoNetWorkExamForm;
import com.yz.network.examination.service.RegNetWorkExamFrmService;
import com.yz.network.examination.starter.NetWorkExamStarter; 
import com.yz.util.StringUtil;
import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc 获取已确认 基本信息返回处理
 * @author zhuliping
 *
 */
@Component(value = NetWorkExamConstants.GETCONFIRMINFO_NETWORKEXAMINATION_HANDLER)
public class GetComfirmInfoNetWorkExaminationHandler implements NetWorkExaminationHandler {

	@Autowired
	private RegNetWorkExamFrmService regFrmService;
 

	@Autowired
	private NetWorkExamStarter netWorkExamStarter;
	
	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form ,HttpEntity entity ) 
	{
		String result = toStringEntity(entity);
		logger.info("getBaseInfoForm.name{}.id:{},result:{}", form.getName(), form.getId(), result);
		if(StringUtil.isNotBlank(result) && !result.contains("您尚未登录")){
			GetConfrimInfoNetWorkExamForm bindForm = (GetConfrimInfoNetWorkExamForm)form;


			if(result.contains("报名确认成功")){
				String examNoResult = RegexUtil.RegexMatch("考生号为：<font color=\"red\">(.*?)</font>",result);
				regFrmService.updateStudentSceneConfirmStatusAndExamNo(form.getId(),examNoResult);
			}
			
			NetWorkExamHandlerResult result2 = new NetWorkExamHandlerResult();
			result2.setOk(true);
			//返回采集状态
			Map<String,Object> mapStatus = new HashMap<String,Object>();
			mapStatus.put("picCollectStatus","1");
			mapStatus.put("examPayStatus","1");
			mapStatus.put("mobileBindStatus","1");
			mapStatus.put("sceneConfirmStatus","1");
			result2.setResult(mapStatus);
			bindForm.setValue(result2);
			return result2;
		}else {
			if(StringUtil.contains(result, "您尚未登录")) {
				NetWorkExamHandlerResult result2 = new NetWorkExamHandlerResult();
				result2.setOk(false);
				result2.setCompensateCmd(NetWorkExamConstants.UNLOGIN_NETWORKEXAMCOMPENSATE);
				result2.setCompensateCallBack(r->{netWorkExamStarter.start(form);});
				return form.setValue(result2);
			}
		}
		return form.setValue(new NetWorkExamHandlerResult().setOk(false).setResult("获取域报名号异常:result{" + result +"}"));
	}
	
}
