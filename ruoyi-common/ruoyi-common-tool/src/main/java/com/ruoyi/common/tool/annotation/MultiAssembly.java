package com.ruoyi.common.tool.annotation;

import com.ruoyi.common.tool.service.AssemblyService;
import com.ruoyi.common.tool.service.DefaultAssemblyService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多字段通用装配注解
 * <p>
 * 用于标注需要装配多个字段的方法，通过ManyAssembly数组配置多个字段映射。
 * 适用于需要同时装配多个字段的场景，如同时装配创建人、更新人、审批人等。
 * </p>
 *
 * @author ruoyi
 * @since 2026-03-26
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiAssembly {

    /**
     * 装配配置数组
     * <p>
     * 配置多个字段的装配规则，每个配置项指定一个ID字段和对应的目标字段。
     * </p>
     *
     * @return 装配配置数组
     */
    ManyAssembly[] value();

    /**
     * 装配服务类
     * <p>
     * 指定用于获取数据的服务类，该类必须实现AssemblyService接口。
     * 与beanName二选一，优先使用beanName。
     * </p>
     *
     * @return 装配服务类
     */
    Class<? extends AssemblyService<?, ?>> service() default DefaultAssemblyService.class;

    /**
     * 装配服务bean名称
     * <p>
     * 指定从Spring容器中获取的装配服务bean名称。
     * 与service二选一，优先使用beanName。
     * </p>
     *
     * @return 装配服务bean名称
     */
    String beanName() default "";
}