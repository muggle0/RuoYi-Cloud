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

import java.lang.annotation.*;

/**
 * 等于(=) 条件注解
 * <p>
 * 支持对 String 类型字段按英文逗号分割后生成 IN 条件，
 * 当 splitByComma = true 时，会先去除字段值中的换行符，再按英文逗号拆分为多个元素。
 *
 * @author WcJun
 * @date 2019/05/12
 */
@Documented
@CriteriaQuery
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Eq {
    /**
     * 自定义的属性值
     *
     * @return
     */
    String alias() default "";

    /**
     * 默认下划线
     *
     * @return ColumnNamingStrategy
     */
    ColumnNamingStrategy naming() default ColumnNamingStrategy.LOWER_CASE_UNDER_LINE;

    /**
     * 是否将字符串字段按英文逗号分割为多个元素后生成 IN 条件
     * <p>
     * 当字段类型为 String 且该属性为 true 时，处理器会：
     * 1. 去除字段值中的换行符（\r、\n）
     * 2. 按英文逗号（,）分割字符串
     * 3. 过滤掉空白元素
     * 4. 用分割后的元素列表生成 IN 查询条件（退化为 IN 语义）
     *
     * @return 是否启用逗号分割模式，默认 false
     */
    boolean splitByComma() default false;
}