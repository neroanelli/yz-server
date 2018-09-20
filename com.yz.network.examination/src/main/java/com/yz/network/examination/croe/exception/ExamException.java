package com.yz.network.examination.croe.exception;

/**
 * @ Author     ：林建彬.
 * @ Date       ：Created in 15:15 2018/8/9
 * @ Description：简单自定义异常
 */
public class ExamException extends RuntimeException {

    private String errCode; //异常编码
    private String errMessage; //异常信息

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public ExamException(String errCode, String errMessage) {
        super(errMessage);
        this.errCode = errCode;
        this.errMessage = errMessage;
    }
}
