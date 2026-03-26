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
package com.ruoyi.common.tool.query.processor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.tool.annotation.query.ConditionProcessor;
import com.ruoyi.common.tool.annotation.query.Eq;
import com.ruoyi.common.tool.query.query.AbstractQuery;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link Eq} 注解处理器
 *
 * @param <QUERY>  自定义查询 Query
 * @param <ENTITY> 查询想对应的实体类型
 * @author WcJun
 * @date 2019/05/12
 */
@ConditionProcessor(targetAnnotation = Eq.class)
public class EqProcessor<QUERY extends AbstractQuery, ENTITY>
        extends CriteriaAnnotationProcessorAdaptor<Eq, QUERY, QueryWrapper<ENTITY>, ENTITY> {

    @Override
    public boolean process(QueryWrapper<ENTITY> queryWrapper, Field field, QUERY query, Eq criteriaAnnotation) {

        final Object value = this.columnValue(field, query);
        if (this.isNullOrEmpty(value)) {
            // 属性值为 Null OR Empty 跳过
            return true;
        }

        String columnName = criteriaAnnotation.alias();
        if (StringUtils.isEmpty(columnName)) {
            columnName = this.columnName(field, criteriaAnnotation.naming());
        }
        assert columnName != null;

        // 当 splitByComma=true 且字段值为 String 类型时，去除换行符后按英文逗号拆分为元素集合，退化为 IN 条件
        if (criteriaAnnotation.splitByComma() && value instanceof String) {
            String strValue = (String) value;
            // 去除换行符（\r 和 \n），再按英文逗号分割，过滤空白元素
            List<String> splitList = Arrays.stream(strValue.replaceAll("[\r\n]", "").split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            if (splitList.isEmpty()) {
                return true;
            }
            queryWrapper.in(columnName, splitList);
            return true;
        }

        queryWrapper.eq(null != value, columnName, value);

        return true;
    }
}