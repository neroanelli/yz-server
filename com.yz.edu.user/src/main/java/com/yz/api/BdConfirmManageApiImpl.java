package com.yz.api;

import com.yz.model.BdConfirmStudentInfo;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BdConfirmManageService;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdConfirmManageApiImpl implements BdConfirmManageApi {

	@Autowired
	private BdConfirmManageService bdConfirmManageService;
	
	@Override
	public Object getConfirmManage(Header header,Body body) {
		String taId = body.getString("taId");
		String confirmCity = body.getString("confirmCity");
        String confirmAddressLevel = body.getString("confirmAddressLevel");
		return bdConfirmManageService.getConfirmManageList(taId,confirmCity,confirmAddressLevel);
	}

	@Override
	public Object getConfirmCity(Header header, Body body) {
		return bdConfirmManageService.getConfirmCity();
	}

	@Override
	public Object getTaName(Header header, Body body) {
		String confirmCity = body.getString("confirmCity");
		return bdConfirmManageService.getTaName(confirmCity);
	}
	
	@Override
	public Object getConfirmLevel(Header header,Body body) {
        String taId = body.getString("taId");
        String confirmCity = body.getString("confirmCity");
		return bdConfirmManageService.getConfirmLevel(taId,confirmCity);
	}

	@Override
	public Object getConfirmInfo(Header header,Body body){
		String searchInfo = body.getString("searchInfo");
		String userId = body.getString("userId");
		return bdConfirmManageService.getConfirmInfo(searchInfo,userId);
	}

	@Override
    public void confirmSign(Header header,Body body){
	    String confirmId = body.getString("confirmId");
        String userId = body.getString("userId");
        String userName = body.getString("userName");
        String sceneRemark = body.getString("sceneRemark");
        String examNo = body.getString("examNo");
        String signStatus = body.getString("signStatus");
        BdConfirmStudentInfo bds = new BdConfirmStudentInfo();
        bds.setConfirmId(confirmId);
        bds.setSceneRemark(sceneRemark);
        bds.setExamNo(examNo);
        bds.setUpdateUserId(userId);
        bds.setUpdateUser(userName);
        if(signStatus!=null){
            bds.setSignStatus(Integer.parseInt(signStatus));
        }
        bdConfirmManageService.confirmSign(bds);
    }

}
