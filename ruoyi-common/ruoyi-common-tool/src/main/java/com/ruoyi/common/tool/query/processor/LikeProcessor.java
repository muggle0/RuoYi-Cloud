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
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.ruoyi.common.tool.annotation.query.ConditionProcessor;
import com.ruoyi.common.tool.annotation.query.Like;
import com.ruoyi.common.tool.query.query.AbstractQuery;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @param <QUERY>  自定义查询 Query
 * @param <ENTITY> 查询想对应的实体类型
 * @author WcJun
 * @date 2019/05/12
 */
@ConditionProcessor(targetAnnotation = Like.class)
public class LikeProcessor<QUERY extends AbstractQuery, ENTITY>
        extends CriteriaAnnotationProcessorAdaptor<Like, QUERY, QueryWrapper<ENTITY>, ENTITY> {

    @Override
    public boolean process(QueryWrapper<ENTITY> queryWrapper, Field field, QUERY query, Like criteriaAnnotation) {

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
        SqlLike like = criteriaAnnotation.like();

        // 当 splitByComma=true 且字段值为 String 类型时，去除换行符后按英文逗号拆分，每个元素单独生成 LIKE 条件并用 OR 连接
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
            // 将多个分割元素包裹在 and(…) 中，保证与外部条件正确的 AND 优先级关系
            final String finalColumnName = columnName;
            queryWrapper.and(wrapper -> {
                for (int i = 0; i < splitList.size(); i++) {
                    String item = splitList.get(i);
                    if (i > 0) {
                        wrapper.or();
                    }
                    switch (like) {
                        case LEFT:
                            wrapper.likeLeft(finalColumnName, item);
                            break;
                        case RIGHT:
                            wrapper.likeRight(finalColumnName, item);
                            break;
                        case DEFAULT:
                        default:
                            wrapper.like(finalColumnName, item);
                            break;
                    }
                }
            });
            return true;
        }

        switch (like) {
            case LEFT:
                queryWrapper.likeLeft(null != value, columnName, value);
                break;
            case RIGHT:
                queryWrapper.likeRight(null != value, columnName, value);
                break;
            case DEFAULT:
                queryWrapper.like(null != value, columnName, value);
                break;
            default:
                break;
        }

        return true;
    }
}