/*
 * Copyright © 2019 photowey (photowey@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ruoyi.common.tool.annotation.query;


import com.ruoyi.common.tool.query.enums.ColumnNamingStrategy;
import com.ruoyi.common.tool.query.enums.HandleOrderByEnum;
import com.ruoyi.common.tool.query.enums.OrderByEnum;

import java.lang.annotation.*;

/**
 * 排序(ORDER BY) 条件注解
 *
 * @author WcJun
 * @date 2019/05/12
 */
@Documented
@CriteriaQuery
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderBy {
    /**
     * 自定义的属性值
     *
     * @return
     */
    String alias() default "";

    /**
     * 排序
     * 说明: 由于解析器在解析注解的
     *
     */
    OrderByEnum orderBy() default OrderByEnum.ASC;

    /**
     * 默认下划线
     *
     */
    ColumnNamingStrategy naming() default ColumnNamingStrategy.LOWER_CASE_UNDER_LINE;

    /**
     * 排序字段的类型
     * STATIC: 静态排序 - 只要包含了 {@link OrderBy} 注解的字段均参与排序
     * DYNAMIC: 动态排序 - 包含了 {@link OrderBy} 注解的字段 且前端传了值的字段将参与排序
     *
     */
    HandleOrderByEnum handleType() default HandleOrderByEnum.STATIC;
}