package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

/**
 * @描述: 全额奖学金活动
 * @作者: DuKai
 * @创建时间: 2018/2/27 12:22
 * @版本号: V1.0
 */
public interface BdsFullScholarshipApi {

    /**
     * 获取报名人数
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
	@YzService(sysBelong="bds",methodName="getEnrolmentCount",methodRemark="获取全额奖学金活动报名人数",needLogin=false)
    public Object getEnrolmentCount(Header header, Body body) throws IRpcException;


    /**
     * 获取最新报名学员信息
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
	@YzService(sysBelong="bds",methodName="getNewEnrolmentList",methodRemark="获取全额奖学金活动最新报名学员信息",needLogin=false)
    public Object getNewEnrolmentList(Header header, Body body) throws IRpcException;


    /**
     * 获取系统时间
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
	@YzService(sysBelong="bds",methodName="getSystemDateTime",methodRemark="获取系统时间",needLogin=false)
    public Object getSystemDateTime(Header header, Body body) throws IRpcException;
	
	/**
	 * 获取某个优惠类型活动的留言信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getEnrollMsgList",methodRemark="获取某个报读类型的留言信息",needLogin=false)
    public Object getEnrollMsgList(Header header, Body body) throws IRpcException;
	
	/**
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getMsgCount",methodRemark="获取某个人某次优惠报读类型的留言次数",needLogin=false)
    public Object getMsgCount(Header header, Body body) throws IRpcException;
	
	/**
	 * 某个优惠报读类型留言
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="enrollMsg",methodRemark="某个优惠报读类型留言",needLogin=true)
    public Object enrollMsg(Header header, Body body) throws IRpcException;
}
