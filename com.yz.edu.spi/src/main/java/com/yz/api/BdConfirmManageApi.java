package com.yz.api;

import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

/**
 * 现场确认点管理
 * @Description: 
 * @author luxing
 * @date 2018年7月23日 下午6:15:23
 */
public interface BdConfirmManageApi {

	/**
	 * 钉钉-查询现场确认点列表
	 * @param header
	 */
	@YzService(sysBelong="bds",methodName="confirmManage",methodRemark="查询现场确认点列表",needLogin=false)
	public Object getConfirmManage(Header header,Body body);
	
	/**
	 * 钉钉-查询确认城市
	 * @param header
	 * @param body
	 * @return
	 */
	@YzService(sysBelong="bds",methodName="getConfirmCity",methodRemark="查询确认城市",needLogin=false)
	public Object getConfirmCity(Header header,Body body);
	
	/**
	 * 钉钉-查询考试县区
	 * @param header
	 * @param body
	 * @return
	 */
	@YzService(sysBelong="bds",methodName="getTaName",methodRemark="查询考试县区",needLogin=false)
	public Object getTaName(Header header,Body body);
	
	/**
	 * 钉钉-查询确认点专业层次
	 * @param header
	 * @param body
	 * @return
	 */
	@YzService(sysBelong="bds",methodName="getConfirmLevel",methodRemark="查询确认点专业层次",needLogin=false)
	public Object getConfirmLevel(Header header,Body body);


    /**
     * 公众号-获取学员确认信息
     * @param header
     * @param body
     * @return
     */
    @YzService(sysBelong = "bds",methodName = "getConfirmInfo",methodRemark = "获取学员确认信息",needLogin = false)
	public Object getConfirmInfo(Header header,Body body);

    /**
     * 公众号-现场确认签到
     * @param header
     * @param body
     * @return
     */
    @YzService(sysBelong="bds",methodName="confirmSign",methodRemark="现场确认签到",needLogin=false)
    public void confirmSign(Header header,Body body);

}
