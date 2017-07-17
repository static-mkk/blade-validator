package com.blade.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 最大值验证
 * 当注解值大于value时
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface Max {

	String message() default "Beyond the maximum";

	double value();
}
