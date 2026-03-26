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
package com.ruoyi.common.tool.query.time;

import java.time.ZonedDateTime;

/**
 * 带时区的时间处理器
 *
 * @author WcJun
 * @date 2020/02/15
 */
public class ZonedDateTimeProcessor implements TimeProcessor {
    @Override
    public <T> T processTime(T time) {
        return time;
    }

    @Override
    public boolean support(Class<?> timeType) {
        return ZonedDateTime.class.isAssignableFrom(timeType);
    }

    @Override
    public boolean supports(Class<?> timeType) {
        return support(timeType);
    }

    @Override
    public <T> T handleTime(Long timeStamp, Class<?> clazz) {
        return (T) java.time.ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(timeStamp), java.time.ZoneId.systemDefault());
    }
}