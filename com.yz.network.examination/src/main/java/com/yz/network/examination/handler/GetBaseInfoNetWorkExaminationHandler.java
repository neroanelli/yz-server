package com.yz.network.examination.handler;

 

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.croe.util.RegexUtil;
import com.yz.network.examination.form.BaseNetWorkExamForm; 
import com.yz.network.examination.form.GetBaseInfoNetWorkExamForm; 
import com.yz.network.examination.service.RegNetWorkExamFrmService;
import com.yz.network.examination.starter.NetWorkExamStarter; 
import com.yz.util.StringUtil;
import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @desc 获取报考基本信息返回处理
 * @author jiangyt
 *
 */
@Component(value = NetWorkExamConstants.GETBASEINFO_NETWORKEXAMINATION_HANDLER)
public class GetBaseInfoNetWorkExaminationHandler implements NetWorkExaminationHandler {

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
			GetBaseInfoNetWorkExamForm bindForm = (GetBaseInfoNetWorkExamForm)form;

			// 更新状态
			int examPayStatus=0;
			int picCollectStatus=0;
			int mobileBindStatus=0;
			String printHtml=getPrintHtml(result);

			String mobileResult = RegexUtil.RegexMatch("1[0-9]{10}（(.*?)）",result);
			if(!mobileResult.contains("未绑定")){
				mobileBindStatus = 1;
			}
		
			String collectResult = RegexUtil.RegexMatch("相片采集：</td>\\W+<td.*?>(.*?)</td>",result);

			if(!collectResult.contains("未验证")){
				if(collectResult.contains("人工待验")){
					picCollectStatus = 2;
				}else {
					picCollectStatus = 1;
					regFrmService.updateNetWorkStatus("3", bindForm.getId());
				}
			}
			String payResult = RegexUtil.RegexMatch("交费情况：</td>\\W+<td.*?>(.*?)</td>",result);
			if(!payResult.contains("未交")){
				examPayStatus = 1;
				regFrmService.updateNetWorkStatus("4",bindForm.getId());
			}
			
			if(result.contains("确认成功")){
				String examNoResult = RegexUtil.RegexMatch("考生号为：<font color=\"red\">(.*?)</font>",result);
				regFrmService.updateStudentSceneConfirmStatusAndExamNo(form.getId(),examNoResult);
			}
			
			long time = System.currentTimeMillis();
			regFrmService.updateSceneConfirmStatus(examPayStatus,picCollectStatus,mobileBindStatus,bindForm.getId());
			if(StringUtil.hasValue(printHtml)) {
				regFrmService.updateScenePrintHtml(printHtml, 1, bindForm.getId());
			}
			System.out.println("time:"+(System.currentTimeMillis()-time));

			NetWorkExamHandlerResult result2 = new NetWorkExamHandlerResult();
			result2.setOk(true);
			//返回采集状态
			Map<String,Object> mapStatus = new HashMap<String,Object>();
			mapStatus.put("picCollectStatus",picCollectStatus);
			mapStatus.put("examPayStatus",examPayStatus);
			mapStatus.put("mobileBindStatus",mobileBindStatus);
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

	/**
	 * 获取打印html
	 * @param result
	 * @return
	 */
	public String getPrintHtml(String result){
		String html = StringUtil.EMPTY;
		try {
			String sprnstr = "<!--startprint-->";
			String eprnstr = "<!--endprint-->";

			html = result.substring(result.indexOf(sprnstr) + 17);
			html = html.substring(0, html.indexOf(eprnstr));
		}catch (Exception ex){
			return null;
		}
		return html;
	}
	
}
