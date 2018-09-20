package com.yz.network.examination.dao;

import com.yz.network.examination.vo.LoginUser;
import org.apache.ibatis.annotations.Param;

public interface LoginMapper {
    /**
     * 获取基本的username信息
     * @param userName
     * @return
     */
    LoginUser getUser(@Param("userName") String userName);

    /**
     * 获取详细的登录信息
     * @param userId
     * @return
     */
    LoginUser assembly(@Param("userId") String userId);
}
