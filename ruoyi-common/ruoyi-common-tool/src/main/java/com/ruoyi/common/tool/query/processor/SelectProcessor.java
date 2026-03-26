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
import com.ruoyi.common.tool.annotation.query.Select;
import com.ruoyi.common.tool.query.query.AbstractQuery;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;

/**
 * {@code SelectProcessor}
 *
 * @author photowey
 * @date 2022/10/23
 * @since 1.0.0
 */
@ConditionProcessor(targetAnnotation = Select.class)
public class SelectProcessor<QUERY extends AbstractQuery<ENTITY>, ENTITY>
        extends CriteriaAnnotationProcessorAdaptor<Select, QUERY, QueryWrapper<ENTITY>, ENTITY> {

    @Override
    public boolean process(QueryWrapper<ENTITY> queryWrapper, Field field, QUERY query, Select criteriaAnnotation) {
        String[] fields = criteriaAnnotation.value();
        if (ObjectUtils.isEmpty(fields)) {
            return true;
        }

        queryWrapper.select(fields);

        return true;
    }
}