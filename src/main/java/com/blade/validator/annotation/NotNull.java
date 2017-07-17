package com.blade.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证是否为NULL
 * 当注解值为null
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface NotNull {

	String message() default "Object not is null";

}
