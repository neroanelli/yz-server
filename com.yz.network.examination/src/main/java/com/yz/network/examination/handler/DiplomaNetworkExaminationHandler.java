package com.yz.network.examination.handler;

import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.exception.BusinessException;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.dao.RegNetWorkExamFrmMapper;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.network.examination.starter.NetWorkExamStarter;
import com.yz.util.StringUtil;

@Component(value = NetWorkExamConstants.DIPLOMA_NETWORKEXAMINATION_HANDLER)
public class DiplomaNetworkExaminationHandler implements NetWorkExaminationHandler {

	@Autowired
	private NetWorkExamStarter netWorkExamStarter;

	@Autowired
	private RegNetWorkExamFrmMapper mapper;

	@Override
	public NetWorkExamHandlerResult handler(BaseNetWorkExamForm form, HttpEntity entity) throws BusinessException {
		String result = toStringEntity(entity);
		NetWorkExamHandlerResult result2 = new NetWorkExamHandlerResult();
		if(StringUtil.contains(result, "查询考生信息出错！"))
		{
			result2.setOk(false);
			result2.setCompensateCmd(NetWorkExamConstants.UNLOGIN_NETWORKEXAMCOMPENSATE);
			result2.setCompensateCallBack(v->{netWorkExamStarter.start(form);});
			return result2;
		}
		if(StringUtil.contains(result, "不合格"))
		{
			String str = StringUtil.substringAfterLast(result, "毕业证号:</div>");
			str = StringUtil.replace(StringUtil.substringBefore(str, "</tr>"),"<td>","");
			str = StringUtil.replace(str,"</td>","");
			result2.setResult(StringUtil.trim(str));
			mapper.updateByzshm(str, form.getId());
		}
		return result2;
	}
}
