package com.onchain.lock;

import com.onchain.exception.DistributedLockException;
import com.onchain.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class DistributedLockInterceptor {

    private static final ThreadLocal<String> locknameThreadLocal =
            new NamedThreadLocal<>("ThreadLocal lock name");
    private static final ThreadLocal<String> lockvalueThreadLocal =
            new NamedThreadLocal<>("ThreadLocal lock value");

    public static final String LOCK_PREFIX = "distributed-lock-";

    private static final String UNLOCK_SCRIPT = "if redis.call('get',KEYS[1]) == ARGV[1] " +
            " then return redis.call('del',KEYS[1]) " +
            " else return 0 end";

    @Autowired
    RedisService redisService;

    /**
     * Controller层切点 注解拦截
     */
    @Pointcut("@annotation(com.onchain.lock.DistributedLockAnnotation)")
    public void controllerAspect() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作的开始时间
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException, DistributedLockException {
        DistributedLockAnnotation distributedLockAnnotation = getZkLockAnnotation(joinPoint);

        String name = distributedLockAnnotation.value();
        if (StringUtils.isBlank(name)) {
            throw new DistributedLockException("lock name cannot be blank");
        }
        locknameThreadLocal.set(name);
        String uuid = UUID.randomUUID().toString();
        lockvalueThreadLocal.set(uuid);
        while (true) {
            Boolean res = redisService.setValueIfAbsent(LOCK_PREFIX + name, uuid, 600L);
            if (res != null && res) {
                break;
            }
            Thread.sleep(1000);
            log.info("wait mutex {}", name);
        }
        //获得了锁, 进行业务流程
        log.info("enter mutex {}", name);
    }

    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning(returning = "rvt", pointcut = "controllerAspect()")
    public void doAfter(JoinPoint joinPoint, Object rvt) throws Exception {
        String name = locknameThreadLocal.get();
        String uuid = lockvalueThreadLocal.get();
        unlock(LOCK_PREFIX + name, uuid);
        log.info("mutex {} release", name);
    }

    private void unlock(String key, String value) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        //用于解锁的lua脚本位置
        redisScript.setScriptText(UNLOCK_SCRIPT);
        redisScript.setResultType(Long.class);
        //没有指定序列化方式，默认使用上面配置的
        Long result = redisService.execute(redisScript, Collections.singletonList(key), value);
    }

    /**
     * 异常通知 记录操作报错日志
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) throws Exception {
        String name = locknameThreadLocal.get();
        String uuid = lockvalueThreadLocal.get();
        unlock(LOCK_PREFIX + name, uuid);
        log.info("mutex {} release", name);
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return description
     */
    private DistributedLockAnnotation getZkLockAnnotation(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(DistributedLockAnnotation.class);
    }
}
