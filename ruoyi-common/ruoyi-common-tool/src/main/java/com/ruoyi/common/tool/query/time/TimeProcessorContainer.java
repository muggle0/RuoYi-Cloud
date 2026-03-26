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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 时间处理器容器
 *
 * @author WcJun
 * @date 2020/02/15
 */
public class TimeProcessorContainer {

    private static final Logger log = LoggerFactory.getLogger(TimeProcessorContainer.class);

    /**
     * 时间处理器集合
     */
    private static final List<TimeProcessor> TIME_PROCESSORS = new ArrayList<>();

    static {
        // 本地时间
        TIME_PROCESSORS.add(new LocalDateProcessor());
        // 本地时间时间
        TIME_PROCESSORS.add(new LocalDateTimeProcessor());
        // 本地时间
        TIME_PROCESSORS.add(new LocalTimeProcessor());
        // 带时区的时间
        TIME_PROCESSORS.add(new ZonedDateTimeProcessor());
    }

    public static <T> T processTime(T time) {
        if (time == null) {
            return null;
        }

        final Class<?> timeClass = time.getClass();
        for (TimeProcessor processor : TIME_PROCESSORS) {
            if (processor.support(timeClass)) {
                return (T) processor.processTime(time);
            }
        }

        // 默认处理器
        TimeProcessor defaultTimeProcessor = new DefaultTimeProcessor();
        if (defaultTimeProcessor.support(timeClass)) {
            return (T) defaultTimeProcessor.processTime(time);
        }

        return time;
    }

    public static java.util.Collection<TimeProcessor> timeProcessors() {
        return TIME_PROCESSORS;
    }

    public static void destroyProcessorCache() {
        TIME_PROCESSORS.clear();
    }
}