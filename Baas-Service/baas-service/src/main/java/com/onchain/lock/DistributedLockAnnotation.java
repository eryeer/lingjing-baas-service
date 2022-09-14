package com.onchain.lock;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLockAnnotation {

    /**
     * 锁名称-必填
     */
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

}

