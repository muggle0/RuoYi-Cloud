package com.ruoyi.common.tool.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多字段用户信息装配注解
 * <p>
 * 用于标注需要装配多个用户字段的方法，通过UserAssemblyConfig数组配置多个字段映射。
 * 适用于需要同时装配多个用户字段的场景，如同时装配创建人、更新人、审批人等。
 * </p>
 *
 * @author 146464
 * @since 2026-03-04
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiAssembly {

    /**
     * 用户装配配置数组
     * <p>
     * 配置多个用户字段的装配规则，每个配置项指定一个用户ID字段和对应的用户名字段。
     * </p>
     *
     * @return 用户装配配置数组
     */
    ManyAssembly[] value();
}
