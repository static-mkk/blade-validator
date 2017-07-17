package com.blade.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 最小值验证
 * 当注解数值小于value时
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface Min {

    String message() default "Below the minimum";

    double value();

}
