package com.onchain.aop;

import com.onchain.config.ParamsConfig;
import com.onchain.constants.ReturnCode;
import com.onchain.exception.CommonException;
import com.onchain.untils.Helper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
@AllArgsConstructor
public class RequestLimitAspect {

    private final ParamsConfig paramsConfig;
    private final RedisTemplate<Object, Object> redisTemplate;

    @Before("execution(public * com.onchain.controller.*.*(..)) && @annotation(limit)")
    public void requestLimit(JoinPoint joinpoint, RequestLimit limit) throws CommonException {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String ip = Helper.getHttpReqRealIp(request);
        String url = request.getRequestURL().toString();
        String queryStr = request.getQueryString();
        if (StringUtils.isBlank(queryStr)) {
            queryStr = "";
        }
        String key = "req_limit_".concat(url).concat(queryStr).concat("_").concat(ip);

        //加1后看看值
        long count = redisTemplate.opsForValue().increment(key, 1);
        //刚创建
        if (count == 1) {
            //设置1分钟过期
            redisTemplate.expire(key, limit.time(), TimeUnit.MILLISECONDS);
        }
        if (count > limit.count()) {
            log.warn("用户IP[" + ip + "]访问地址[" + url + "?" + queryStr + "]超过了限定的次数[" + limit.count() + "]");
            throw new CommonException(ReturnCode.REQ_LIMIT_ERROR);
        }
    }
}
