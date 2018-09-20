package com.yz.dao.sceneMng;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BmsUser;
import com.yz.model.admin.BmsUserResponse;

public interface NetToAccountMapper {

	//查询网报账户信息
	List<BmsUserResponse> queryUserList(BmsUser user);
	
	//编辑用户
	int editUser(BmsUser record);
	
	//验证是否存在
	int checkUserNameIsExist(@Param(value = "userName") String userName, @Param(value = "oldUserName") String oldUserName);
	
	//新增账户
	public void insertUser(BmsUserResponse user);
	
	//详细
	BmsUserResponse selectUserById(String userId);
	
	//修改账户
	public void updateUser(BmsUserResponse user);
}
