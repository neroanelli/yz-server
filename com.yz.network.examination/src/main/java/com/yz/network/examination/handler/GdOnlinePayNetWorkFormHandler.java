package com.yz.network.examination.handler;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.yz.exception.BusinessException;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.croe.util.RegexUtil;
import com.yz.network.examination.form.BaseNetWorkExamForm; 
import com.yz.network.examination.form.SaveWeChatPaymentForm;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.util.RegUtils;
import com.yz.util.StringUtil;

@Component(value = NetWorkExamConstants.ONLINEPAY_NETWORKEXAMINATION_HANDLER)
public class GdOnlinePayNetWorkFormHandler implements NetWorkExaminationHandler {

	@Autowired
	private NetWorkExamStarter netWorkExamStarter;

	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form, HttpEntity entity) throws BusinessException {
		String result = toStringEntity(entity);
	 
		
		String base = RegexUtil.RegexMatch("var basePath=\"(\\S+)\";", result);
		String weChatReqDataStr = StringUtil.substringAfter(result, "var reqDataStr='");
		weChatReqDataStr = StringUtil.substringBefore(weChatReqDataStr, "';");
		BaseNetWorkExamForm chekcPayNoticeFrm = this.getChekcPayNoticeFrm(base, weChatReqDataStr); 
		netWorkExamStarter.start(chekcPayNoticeFrm);
		
	    if(chekcPayNoticeFrm.getValue().isOk())
	    {
	    	BaseNetWorkExamForm saveWeChatPaymentForm = this.getSaveWeChatPaymentForm(base, weChatReqDataStr); 
			netWorkExamStarter.start(saveWeChatPaymentForm);
			
		    if(!saveWeChatPaymentForm.getValue().isOk())
		    {
		    	Map<String, Object> param = Maps.newHashMap();
				param.put("unitNo", RegexUtil.RegexMatch("\\?unitNo=\"\\+\'(\\S+)\';", result));
				param.put("payNoticeNo", RegexUtil.RegexMatch("\\&payNoticeNo=\"\\+'(\\S+)';", result));
				param.put("districtNo", RegexUtil.RegexMatch("\\&districtNo=\"\\+'(\\S+)';", result));
				param.put("totMoney", RegexUtil.RegexMatch("\\&totMoney=\"\\+'(\\S+)';", result));
				param.put("deadlineTime", RegexUtil.RegexMatch("\\&deadlineTime=\"\\+'(\\S+)';", result));
				param.put("payerName", RegexUtil.RegexMatch("\\&payerName=\"\\+'(\\S+)';", result));
				param.put("deviceType", RegexUtil.RegexMatch("\\&deviceType=(\\S+)\";", result));
				String wxpayUrl = RegUtils.replaceTemplateFormData(NetWorkExamConstants.WXPAY_NETWORKEXAMINATION_URL, param);
				return form.setValue(new NetWorkExamHandlerResult().setOk(true).setResult(wxpayUrl));
		    }
		    return form.setValue(saveWeChatPaymentForm.getValue());
	    }
	
	    return form.setValue(chekcPayNoticeFrm.getValue());
	}
	
	/**
	 * 
	 * @param base
	 * @param weChatReqDataStr
	 * @return
	 */
	private BaseNetWorkExamForm getSaveWeChatPaymentForm(String base,String weChatReqDataStr)
	{  
		StringBuilder action = new StringBuilder(base)
				.append("weChatPayGateway/saveWeChatPayment.do?temp=" + Math.random()); 
		return this.getWeChatPaymentForm(action.toString(),base, weChatReqDataStr);
	}
	
	
	/**
	 * 
	 * @param base
	 * @param weChatReqDataStr
	 * @return
	 */
	private BaseNetWorkExamForm getChekcPayNoticeFrm (String base,String weChatReqDataStr)
	{  
		StringBuilder action = new StringBuilder(base)
				.append("weChatPayGateway/chekcPayNotice.do?temp=" + Math.random()); 
		return this.getWeChatPaymentForm(action.toString(),base, weChatReqDataStr);
	}
	
	/**
	 * 
	 * @param base
	 * @param weChatReqDataStr
	 * @return
	 */
	private BaseNetWorkExamForm getWeChatPaymentForm(String action,String base,String weChatReqDataStr)
	{
		SaveWeChatPaymentForm saveWeChatPaymentForm = new SaveWeChatPaymentForm(); 
		saveWeChatPaymentForm.setFrmAction(action.toString());
		saveWeChatPaymentForm.addParam("weChatReqDataStr", weChatReqDataStr);
		saveWeChatPaymentForm.addParam("weChatDeviceType", "1"); 
		return saveWeChatPaymentForm;
	}

	public static void main(String[] args) {
		// var reqDataStr='{\"transTime\":\"2018-09-04 15:05:03\"}';
		String src = "var reqDataStr='{\"transTime\":\"2018-09-04 15:05:03\"};";

		src = "var reqDataStr='{\"name\":\"2018-09-04\"}';";
		System.out.println(RegexUtil.RegexMatch("var reqDataStr='(\\S+)\\';", src));
	}

}
