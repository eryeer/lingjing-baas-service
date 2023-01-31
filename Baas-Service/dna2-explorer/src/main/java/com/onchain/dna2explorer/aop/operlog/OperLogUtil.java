package com.onchain.dna2explorer.aop.operlog;

import com.onchain.dna2explorer.utils.CommonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by zhaochen on 17-9-4.
 */
public class OperLogUtil {

    protected static Logger logger = LoggerFactory.getLogger("OperLogUtil");

    public static OperLogInfo getOperLog(HttpServletRequest request, JoinPoint joinPoint, String type, Date beginTime, String hostName, Object rvt) {

        String title = "";
        OperLogAnnotation operLogAnnotation = getOperLogAnnotation(joinPoint);
        try {
            title = operLogAnnotation.description();
        } catch (Exception e) {
            logger.error("getOperLog", e);
        }
//        String methodName = joinPoint.getSignature().getName();
//        //如果上传文件，则不记录入参
//        if (StringUtils.equals(methodName,"uploadContract")||StringUtils.equals(methodName,"handleFileUpload")){
//            return getOperLogBase(request,title, "uploadFile",type,beginTime,loginUser);
//        }

        return getOperLogBase(request, title, Arrays.toString(joinPoint.getArgs()), beginTime, type, hostName, rvt);

    }

    public static OperLogInfo getOperLog(HttpServletRequest request, JoinPoint joinPoint, String type, Date beginTime, String hostName) {


        return getOperLog(request, joinPoint, type, beginTime, hostName, null);

    }

    public static OperLogInfo getOperLogBase(HttpServletRequest request, String title, String parameter, Date beginTime, String type, String hostName, Object rvt) {
        String remoteAddr = request.getRemoteAddr();//请求的IP
        String requestUri = request.getRequestURI();//请求的Uri
        String method = request.getMethod();        //请求的方法类型(post/get)
        long endTime = System.currentTimeMillis();
        InetAddress localHostLANAddress = CommonUtil.getLocalHostLANAddress();
        String hostAddress = "";
        if (localHostLANAddress != null) {
            hostAddress = localHostLANAddress.getHostAddress();
        }

        OperLogInfo log = new OperLogInfo();
        log.setTitle(title);
        log.setOpertype(type);
        log.setRemoteAddr(remoteAddr);
        log.setRequestUri(requestUri);
        log.setMethod(method);
        log.setParams(parameter);
        log.setCreateDate(beginTime);
        log.setEndDate(new Date(endTime));
        log.setHostName(hostName);
        log.setHostAddr(hostAddress);
        if (rvt != null) {
            log.setReturnValue(rvt.toString());
        }
        return log;
    }


    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return description
     */
    private static OperLogAnnotation getOperLogAnnotation(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(OperLogAnnotation.class);
    }


}
