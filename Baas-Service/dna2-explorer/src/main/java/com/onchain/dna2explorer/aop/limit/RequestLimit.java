package com.onchain.dna2explorer.aop.limit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RequestLimit {

    // 默认每秒放入桶中的token
    double limitNum() default 20;

    String name() default "";

}
