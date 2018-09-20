package com.yz.service.operate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.operate.BdEnrollLotteryMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.operate.BdEnrollLotteryAttr;
import com.yz.model.operate.BdEnrollLotteryInfo;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdEnrollLotteryService {

	@Autowired
	private BdEnrollLotteryMapper enrollLotteryMapper;
	
	/**
	 * 抽奖活动列表
	 * @param page
	 * @param pageSize
	 * @param info
	 * @return
	 */
	public IPageInfo<BdEnrollLotteryInfo> getEnrollLotteryList(int page,int pageSize,BdEnrollLotteryInfo info) {
		PageHelper.offsetPage(page, pageSize);
		List<BdEnrollLotteryInfo> lotteryList = enrollLotteryMapper.getEnrollLotteryList(info);
		return new IPageInfo<>((Page<BdEnrollLotteryInfo>)lotteryList);
	}
	
	/**
	 * 获取字典优惠类型
	 * @return
	 */
	public List<Map<String, String>> getScholarshipList(){
		return enrollLotteryMapper.getScholarshipList();
	}
	
	/**
	 * 增加
	 * @param lotteryInfo
	 */
	public void add(BdEnrollLotteryInfo lotteryInfo) {
		BaseUser user = SessionUtil.getUser();
		ArrayList<BdEnrollLotteryAttr> itemsList = new ArrayList<>();
		if(lotteryInfo.getItems().size() >0){
			lotteryInfo.getItems().stream().forEach(v ->{
				if(StringUtil.isNotBlank(v.getAttrValue()) && NumberUtils.toInt(v.getAttrValue())>0){
					itemsList.add(v);
				}
			});
		}
		lotteryInfo.setUpdateUser(user.getRealName());
		lotteryInfo.setUpdateUserId(user.getUserId());
		lotteryInfo.setLotteryId(IDGenerator.generatorId());
		lotteryInfo.setItems(itemsList);
		enrollLotteryMapper.addLotteryInfo(lotteryInfo);
	}
	/**
	 * 修改
	 * @param lotteryInfo
	 */
	public void update(BdEnrollLotteryInfo lotteryInfo) {
		BaseUser user = SessionUtil.getUser();
		ArrayList<BdEnrollLotteryAttr> itemsList = new ArrayList<>();
		if(lotteryInfo.getItems().size() >0){
			lotteryInfo.getItems().stream().forEach(v ->{
				if(StringUtil.isNotBlank(v.getAttrValue()) && NumberUtils.toInt(v.getAttrValue())>0){
					itemsList.add(v);
				}
			});
		}
		lotteryInfo.setUpdateUser(user.getRealName());
		lotteryInfo.setUpdateUserId(user.getUserId());
		lotteryInfo.setItems(itemsList);
		enrollLotteryMapper.updateLotteryInfo(lotteryInfo);
	}
	
	/**
	 * 获取抽奖活动
	 * @param lotteryId
	 * @return
	 */
	public BdEnrollLotteryInfo getEnrollLotteryInfo(String lotteryId){
		return enrollLotteryMapper.getEnrollLotteryInfo(lotteryId);
	}
	
	/**
	 * 根据状态获取抽奖信息
	 * @param status
	 * @return
	 */
	public Object getLotteryInfoByStatus(String status){
		return enrollLotteryMapper.getLotteryInfoByStatus(status);
	}
	/**
	 * 获取特定的优惠券
	 * @return
	 */
	public Object getCouponInfoByCond(){
		return enrollLotteryMapper.getCouponInfoByCond();
	}
}
