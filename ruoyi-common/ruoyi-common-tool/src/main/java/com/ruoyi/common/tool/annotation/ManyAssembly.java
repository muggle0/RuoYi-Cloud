package com.ruoyi.common.tool.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户装配配置注解
 * <p>
 * 用于配置单个用户字段的装配规则，作为MultiUserAssembly的内部注解使用。
 * 支持嵌套对象字段路径，如：crmVisitPlanDetailBaseVO.visitorId
 * </p>
 *
 * @author 146464
 * @since 2026-03-04
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyAssembly {

    /**
     * 用户ID字段名
     * <p>
     * 支持嵌套对象字段路径，使用点号分隔，如：crmVisitPlanDetailBaseVO.visitorId
     * </p>
     *
     * @return 用户ID字段名
     */
    String userIdField();

    /**
     * 用户名字段名
     * <p>
     * 支持嵌套对象字段路径，使用点号分隔，如：crmVisitPlanDetailBaseVO.visitorName
     * </p>
     *
     * @return 用户名字段名
     */
    String userNameField();
}
