package com.yu.openapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验
 *Target(ElementType.METHOD)  该注解可以用在方法上
 * Retention(RetentionPolicy.RUNTIME) 表示该注解在运行时保留，可以通过反射获取到
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 必须有某个角色
     *
     * @return
     */
    String mustRole() default "";

}

