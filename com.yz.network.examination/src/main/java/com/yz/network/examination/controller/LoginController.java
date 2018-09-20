package com.yz.network.examination.controller;

import com.yz.exception.BusinessException;
import com.yz.network.examination.croe.annotation.Rule;
import com.yz.network.examination.croe.exception.ExamException;
import com.yz.network.examination.croe.util.SessionUtil;
import com.yz.network.examination.service.LoginService;
import com.yz.network.examination.vo.LoginUser;
import com.yz.util.CodeUtil;
import com.yz.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ Author     ：林建彬.
 * @ Date       ：Created in 10:53 2018/8/9
 * @ Description：登录操作
 */
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 登录
     *
     * @param userName
     *            用户名
     * @param password
     *            密码
     * @param validCode
     *            验证码
     * @param req
     * @param resp
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public Object login(@RequestParam(name = "username", required = true) String userName,
                        @RequestParam(name = "password", required = true) String password) throws IOException {


        LoginUser user = loginService.getUser(userName);

        if (user != null) {
            String pwd = user.getPassword();

            if (CodeUtil.MD5.encrypt(password).equals(pwd)) {
                user = loginService.assembly(user.getUserId());
                SessionUtil.setKey(user.getUserId());// 存入用户唯一登录标识
                SessionUtil.setUser(user);// 存入session对象
                return user;
            }
        }

        // 用户名不存在或者密码错误
        throw new ExamException("E000031","用户名不存在或者密码错误");

    }

    /**
     * 退出登录
     * @return
     */
    @RequestMapping(value = "/logout")
    @ResponseBody
    public Object logout() {
        LoginUser user = SessionUtil.getUser();
        SessionUtil.clearUser(user.getUserId());
        return "SUCCESS";
    }

    /**
     * 校验登录
     * @return
     */
    @RequestMapping(value = "/checkLogin")
    @ResponseBody
    @Rule
    public Object checkLogin() {
        return SessionUtil.getUser();
    }

}
