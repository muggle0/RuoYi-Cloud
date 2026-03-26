package com.ruoyi.common.tool.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通用装配配置注解
 * <p>
 * 用于配置单个字段的装配规则，作为MultiAssembly的内部注解使用。
 * 支持嵌套对象字段路径，如：crmVisitPlanDetailBaseVO.visitorId
 * </p>
 *
 * @author ruoyi
 * @since 2026-03-26
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyAssembly {

    /**
     * ID字段名
     * <p>
     * 支持嵌套对象字段路径，使用点号分隔，如：crmVisitPlanDetailBaseVO.visitorId
     * </p>
     *
     * @return ID字段名
     */
    String keyField();

    /**
     * 目标字段名
     * <p>
     * 支持嵌套对象字段路径，使用点号分隔，如：crmVisitPlanDetailBaseVO.visitorName
     * </p>
     *
     * @return 目标字段名
     */
    String targetField();
}