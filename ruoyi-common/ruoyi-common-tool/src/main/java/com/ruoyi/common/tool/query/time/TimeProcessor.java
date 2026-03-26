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

/**
 * 时间处理器
 *
 * @author WcJun
 * @date 2020/02/15
 */
public interface TimeProcessor {
    /**
     * 处理时间
     *
     * @param time 时间
     * @return 处理后的时间
     */
    <T> T processTime(T time);

    /**
     * 支持的时间类型
     *
     * @param timeType 时间类型
     * @return boolean
     */
    boolean support(Class<?> timeType);

    /**
     * 支持的时间类型
     *
     * @param timeType 时间类型
     * @return boolean
     */
    boolean supports(Class<?> timeType);

    /**
     * 处理时间戳
     *
     * @param timeStamp 时间戳
     * @param clazz 时间类型
     * @return 处理后的时间
     */
    <T> T handleTime(Long timeStamp, Class<?> clazz);
}