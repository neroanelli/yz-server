package com.yz.model.exam;

import java.io.Serializable;

/**
 * @ Author     ：林建彬.
 * @ Date       ：Created in 16:29 2018/9/2
 * @ Description：TODO
 */
public class BdStudentSceneRegister implements Serializable {



    private static final long serialVersionUID = 1L;
    String registerId;//ID
    String learnId;
    String username;//预报名号
    String password;//预报名密码


    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public String getLearnId() {
        return learnId;
    }

    public void setLearnId(String learnId) {
        this.learnId = learnId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
