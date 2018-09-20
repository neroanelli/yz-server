package com.yz.network.examination.service;

import com.yz.network.examination.dao.LoginMapper;
import com.yz.network.examination.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ Author     ：林建彬.
 * @ Date       ：Created in 11:28 2018/8/9
 * @ Description：登录操作
 */
@Service
public class LoginService {

    @Autowired
    private LoginMapper loginMapper;

    /**
     * 获取username基本信息
     * @param userName
     * @return
     */
    public LoginUser getUser(String userName) {
        return loginMapper.getUser(userName);
    }

    /**
     * 密码正确获取详细信息
     * @param userId
     * @return
     */
    public LoginUser assembly(String userId) {
        return loginMapper.assembly(userId);
    }
}
