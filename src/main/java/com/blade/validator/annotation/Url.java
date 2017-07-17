package com.blade.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证是否是URL
 * 当注解值不是一个url
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface Url {

	String message() default "Wrong URL format";

}
