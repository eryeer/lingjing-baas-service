package com.onchain.dna2explorer.config;

import com.onchain.dna2explorer.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.beans.BeansException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class RedisCache implements Cache {

    private static final Integer cacheSeconds = 60 * 60;

    private String CLASS_NAME = this.getClass().getSimpleName();

    // 读写锁
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    private String id;

    public RedisCache(final String id) {
        log.info("##init ExplorerRedisCache, Cache id:{}##", id);
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        log.info("##{}.{} Id:{}", CLASS_NAME, CommonUtil.currentMethod(), this.id);
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        log.info("##{}.{} key:{}, value:{}##", CLASS_NAME, CommonUtil.currentMethod(), key, value);
        SpringAccessor.getRedisTemplate().opsForValue().set(key.toString(), value, cacheSeconds, TimeUnit.SECONDS);
    }

    @Override
    public Object getObject(Object key) {
        log.info("##{}.{} key:{}##", CLASS_NAME, CommonUtil.currentMethod(), key.toString());
        try {
            return SpringAccessor.getRedisTemplate().opsForValue().get(key.toString());
        } catch (Exception e) {
            log.error("redis error... ", e);
        }
        return null;
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            removeObject(key);
        }
    }

    @Override
    public Object removeObject(Object key) {
        log.info("##{}.{} key:{}##", CLASS_NAME, CommonUtil.currentMethod(), key.toString());
        try {
            SpringAccessor.getRedisTemplate().delete(key.toString());
        } catch (Exception e) {
            log.error("redis error... ", e);
        }
        return null;
    }

    @Override
    public void clear() {
        log.info("##{}.{} this.id:{}", CLASS_NAME, CommonUtil.currentMethod(), this.id);
        try {
            Set<String> keys = SpringAccessor.getRedisTemplate().keys("*:" + this.id + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                log.info("keys:{}", keys);
                SpringAccessor.getRedisTemplate().delete(keys);
            }
        } catch (Exception e) {
            log.error("redis error... ", e);
        }
    }

    @Override
    public int getSize() {
        log.info("##{}.{}", CLASS_NAME, CommonUtil.currentMethod());
        Long size = (Long) SpringAccessor.getRedisTemplate().execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
        return size.intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        log.info("##get Redis Cache ReadWriteLock##");
        return this.readWriteLock;
    }

    // RedisCache is instantiated by MyBatis, however we wish to use a Spring managed RedisTemplate.  To avoid race
    // conditions between Spring context initialization, and MyBatis, use getRedisTemplate() to access this.
    static final class SpringAccessor {
        private static RedisTemplate<String, Object> redisTemplate;

        static RedisTemplate<String, Object> getRedisTemplate() {
            // locally cache the RedisTemplate as that is not expected to change
            if (redisTemplate == null) {
                try {
                    redisTemplate = ApplicationContextProvider.getBean("redisTemplate");
                } catch (BeansException ex) {
                    log.warn("##Spring Redis template is currently not available.");
                }
            }
            return redisTemplate;
        }
    }
}