package com.yz.service.sceneMng;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.dao.sceneMng.NetToAccountMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BmsUser;
import com.yz.model.admin.BmsUserResponse;
import com.yz.model.common.IPageInfo;
import com.yz.util.CodeUtil;

/**
 * 网报账号管理
 * @author lx
 * @date 2018年7月30日 上午11:05:03
 */
@Service
@Transactional
public class NetToAccountService {

	@Autowired
	private NetToAccountMapper netToAccountMapper;
	
	/**
	 * 获取账号列表
	 * @param start
	 * @param length
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo queryUserList(int start, int length, BmsUser user) {
		PageHelper.offsetPage(start, length);
		List<BmsUserResponse> users = netToAccountMapper.queryUserList(user);
		return new IPageInfo((Page) users);
	}
	/**
	 * 编辑账号
	 * @param user
	 * @return
	 */
	public int editUser(BmsUser user) {
		return netToAccountMapper.editUser(user);
	}
	/**
	 * 判断账号是否存在
	 * @param userName
	 * @param oldUserName
	 * @return
	 */
	public boolean isUserNameExist(String userName, String oldUserName) {
		if (userName.equalsIgnoreCase(oldUserName)) {
			return true;
		}
		if (netToAccountMapper.checkUserNameIsExist(userName, oldUserName) <= 0) {
			return true;
		}
		return false;
	}
	/**
	 * 新增账号
	 * @param user
	 */
	public void insertUser(BmsUserResponse user){
		user.setUserId(IDGenerator.generatorId());
		user.setModule("2");
		user.setUserPwd(CodeUtil.MD5.encrypt("123456"));
		netToAccountMapper.insertUser(user);
	}
	/**
	 * 详细
	 * @param userId
	 * @return
	 */
	public BmsUserResponse queryUserById(String userId) {
		return netToAccountMapper.selectUserById(userId);
	}
	/**
	 * 修改
	 * @param user
	 */
	public void updateUser(BmsUserResponse user){
		netToAccountMapper.updateUser(user);
	}
}
