package com.yz.network.examination.croe.handler;

import com.yz.constants.GlobalConstants;
import com.yz.exception.ExceptionCode;
import com.yz.network.examination.croe.annotation.Rule;
import com.yz.network.examination.croe.exception.ExamException;
import com.yz.network.examination.croe.util.RequestUtil;
import com.yz.network.examination.croe.util.SessionUtil;
import com.yz.network.examination.vo.LoginUser;
import com.yz.network.examination.vo.ReturnInfo;
import com.yz.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @ Author     ：林建彬.
 * @ Date       ：Created in 9:42 2018/8/9
 * @ Description：简单登录aop拦截
 */
@Aspect
@Component
public class ControllerAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    // Controller层切点
    @Pointcut("execution(* com.yz.network.examination.controller..*.*(..))")
    public void controllerAspect() {
    }

    @Around("controllerAspect()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

        try {
            Rule rule = (Rule) getMethodAnnotation(Rule.class, pjp);

            if (rule != null) {
                if (rule.checkLogin()) {
                    String sessionId = RequestUtil.getSessionId();
                    logger.debug("------------------------- 当前sessionId ： " + sessionId);
                    LoginUser user = SessionUtil.getUser();
                    if (user != null) {
                        logger.debug("------------------------- 当前用户 ： " + user.getUserName() + "[" + user.getUserId()
                                + "]");
                        String key = SessionUtil.getKey(user.getUserId());
                        if (!StringUtil.hasValue(key)) {
                            throw new ExamException("E000034","登录超时或尚未登录");// 登录超时或尚未登录
                        } else if (!sessionId.equals(key)) {
                            throw new ExamException("E000035","异地登录，被挤下线");// 异地登录，被挤下线
                        }
                    } else {
                        throw new ExamException("E000034","登录超时或尚未登录");// 登录超时或尚未登录
                    }

                    SessionUtil.freshTime(sessionId, user.getUserId());// 更新session生存时间

                }
            }
            Object result = pjp.proceed();

            return new ReturnInfo(GlobalConstants.SUCCESS_CODE, "", result);
        } catch (Exception ex) {
            if (ex instanceof ExamException) {
                ReturnInfo rtInfo = processMyException(ex);
                return rtInfo;
            }
            return processLocalException(ex);
        }

    }



    /**
     * 获取方法注解
     *
     * @param an
     * @param pjp
     * @return
     */
    private Annotation getMethodAnnotation(Class<? extends Annotation> an, ProceedingJoinPoint pjp) {
        // 获取执行的方法
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();

        return method.getAnnotation(an);
    }

    /**
     * 处理自定义异常
     *
     * @param ex
     * @return
     */
    private ReturnInfo processMyException(Exception ex) {
        String systemErrorMsg = ex == null ? "未知异常" :((ExamException)ex).getErrMessage();

        logger.error(systemErrorMsg, ex);

        String appErrorMsg = systemErrorMsg;

        return new ReturnInfo(((ExamException)ex).getErrCode(), appErrorMsg);
    }

    /**
     * 处理本地异常
     *
     * @param ex
     * @return
     */
    private ReturnInfo processLocalException(Exception ex) {
        String systemErrorMsg = ex == null ? "未知异常" : ex.getMessage();

        logger.error(systemErrorMsg, ex);

        String appErrorMsg = systemErrorMsg;// ExceptionCode.ERROR_MESSAGE.get(ExceptionCode.SYSYTEM_ERROR);开发用
        
        if(StringUtil.isEmpty(appErrorMsg)) {
        	appErrorMsg="网络繁忙，请重试！";
        }
        return new ReturnInfo(ExceptionCode.SYSYTEM_ERROR.getCode(), appErrorMsg);
    }


}

