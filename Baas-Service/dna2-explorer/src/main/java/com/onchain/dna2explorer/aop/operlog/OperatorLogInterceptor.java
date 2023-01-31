package com.onchain.dna2explorer.aop.operlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onchain.dna2explorer.constants.CommonConst;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Aspect
@Component
public class OperatorLogInterceptor {
    private static final ThreadLocal<Date> beginTimeThreadLocal =
            new NamedThreadLocal<Date>("ThreadLocal beginTime");
    private ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger();

    @Autowired(required = false)
    private HttpServletRequest request;

    @Value(CommonConst.SPRING_APPLICATION_NAME)
    private String hostName;

    /**
     * Controller层切点 注解拦截
     */
    @Pointcut("@annotation(com.onchain.dna2explorer.aop.operlog.OperLogAnnotation)")
    public void controllerAspect() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作的开始时间
     *
     * @param joinPoint 切点
     * @throws InterruptedException
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException {
        Date beginTime = new Date();

        beginTimeThreadLocal.set(beginTime);//线程绑定变量（该数据只有当前请求的线程可见）
    }

    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @SuppressWarnings("unchecked")
    @AfterReturning(returning = "rvt", pointcut = "controllerAspect()")
    public void doAfter(JoinPoint joinPoint, Object rvt) {

        OperLogInfo log = OperLogUtil.getOperLog(request, joinPoint, OperLogInfo.LOG_TYPE_INFO, beginTimeThreadLocal.get(), hostName, rvt);
        try {
            logger.log(Level.getLevel("OPER"), mapper.writeValueAsString(log));
        } catch (Exception e) {
            logger.error("doAfter", e);
        }

    }

    /**
     * 异常通知 记录操作报错日志
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        OperLogInfo log = OperLogUtil.getOperLog(request, joinPoint, OperLogInfo.LOG_TYPE_ERROR, beginTimeThreadLocal
                .get(), hostName);
        log.setDetails(e.getMessage());
        try {
            logger.log(Level.getLevel("OPER"), mapper.writeValueAsString(log));
        } catch (Exception ex) {
            logger.error("doAfterThrowing", ex);
        }
    }


}
