package com.ruoyi.common.tool.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.tool.annotation.ManyAssembly;
import com.ruoyi.common.tool.annotation.MultiAssembly;
import com.ruoyi.common.tool.service.AssemblyService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通用装配切面
 * <p>
 * 通过AOP切面自动装配数据，支持多字段装配。
 * 自动收集ID并批量查询，避免重复代码，提升开发效率。
 * </p>
 *
 * @author ruoyi
 * @since 2026-03-26
 */
@Aspect
@Component
public class AssemblyAspect {

    @Autowired
    private ApplicationContext applicationContext;

    private final Logger log = LoggerFactory.getLogger(AssemblyAspect.class);



    /**
     * 多字段通用装配切面
     *
     * @param joinPoint         切点
     * @param multiAssembly 多字段装配注解
     * @return 方法执行结果
     * @throws Throwable 方法执行异常
     */
    @Around("@annotation(multiAssembly)")
    public Object around(ProceedingJoinPoint joinPoint, MultiAssembly multiAssembly) throws Throwable {
        // 执行原方法
        Object result = joinPoint.proceed();

        // 处理多字段装配
        if (result != null) {
            assembleData(result, multiAssembly);
        }

        return result;
    }

    /**
     * 数据装配
     *
     * @param result            方法返回结果
     * @param multiAssembly 多字段装配注解配置
     */
    private void assembleData(Object result, MultiAssembly multiAssembly) {
        if (result instanceof Page) {
            Page<?> page = (Page<?>) result;
            assembleDataForList(page.getRecords(), multiAssembly);
        } else if (result instanceof List) {
            assembleDataForList((List<?>) result, multiAssembly);
        } else {
            assembleDataForObject(result, multiAssembly);
        }
    }

    /**
     * 多字段数据装配（列表）
     *
     * @param records           待装配的记录列表
     * @param multiAssembly 多字段装配注解配置
     */
    private void assembleDataForList(List<?> records, MultiAssembly multiAssembly) {
        if (CollUtil.isEmpty(records)) {
            return;
        }

        ManyAssembly[] configs = multiAssembly.value();
        
        // 获取装配服务实例
        AssemblyService<?, ?> assemblyService = getAssemblyService(multiAssembly);
        if (assemblyService == null) {
            log.error("装配服务获取失败");
            return;
        }

        // 一次性收集所有ID，避免重复反射调用
        Set<Object> allIds = CollUtil.newHashSet();
        //     fieldToIdsMap = {
        // "createById" -> {
        //     1001L -> {Record1, Record2},
        //     1002L -> {Record3}
        // }
        Map<String, Map<Object, Set<Object>>> fieldToIdsMap = MapUtil.newHashMap();

        // 初始化映射
        for (ManyAssembly config : configs) {
            fieldToIdsMap.put(config.keyField(), MapUtil.newHashMap());
        }

        // 单次遍历收集所有ID和对应的记录映射
        for (Object record : records) {
            for (ManyAssembly config : configs) {
                Object id = getIdByReflection(record, config.keyField());
                if (id != null) {
                    allIds.add(id);
                    fieldToIdsMap.get(config.keyField())
                            .computeIfAbsent(id, k -> CollUtil.newHashSet())
                            .add(record);
                }
            }
        }

        // 一次性批量查询所有数据
        Map<?, ?> dataMap = CollUtil.isNotEmpty(allIds) ?
                assemblyService.batchGetData(CollUtil.newArrayList(allIds)) : MapUtil.newHashMap();

        // 装配数据
        for (ManyAssembly config : configs) {
            Map<Object, Set<Object>> idToRecordsMap = fieldToIdsMap.get(config.keyField());
            if (CollUtil.isNotEmpty(idToRecordsMap)) {
                assembleDataForListWithConfig(idToRecordsMap, config, dataMap, assemblyService);
            }
        }
    }

    /**
     * 多字段数据装配（单个对象）
     *
     * @param obj               待装配的单个对象
     * @param multiAssembly 多字段装配注解配置
     */
    private void assembleDataForObject(Object obj, MultiAssembly multiAssembly) {
        if (obj == null) {
            return;
        }

        ManyAssembly[] configs = multiAssembly.value();
        
        // 获取装配服务实例
        AssemblyService<?, ?> assemblyService = getAssemblyService(multiAssembly);
        if (assemblyService == null) {
            log.error("装配服务获取失败");
            return;
        }

        // 收集所有ID
        Set<Object> allIds = CollUtil.newHashSet();
        for (ManyAssembly config : configs) {
            Object id = getIdByReflection(obj, config.keyField());
            if (id != null) {
                allIds.add(id);
            }
        }

        if (CollUtil.isNotEmpty(allIds)) {
            // 批量查询数据
            Map<?, ?> dataMap = assemblyService.batchGetData(CollUtil.newArrayList(allIds));

            // 装配数据
            for (ManyAssembly config : configs) {
                Object id = getIdByReflection(obj, config.keyField());
                if (id != null) {
                    Object data = dataMap.get(id);
                    if (data != null) {
                        // 设置目标值
                        if (StrUtil.isNotBlank(config.targetField())) {
                            Object targetValue = assemblyService.getTargetValue(data);
                            setTargetValueByReflection(obj, config.targetField(), targetValue);
                        }
                    }
                }
            }
        }
    }

    /**
     * 使用配置装配数据
     *
     * @param idToRecordsMap ID与记录集合的映射关系
     * @param config         装配配置
     * @param dataMap        ID与数据的映射关系
     * @param assemblyService 装配服务
     */
    private void assembleDataForListWithConfig(Map<Object, Set<Object>> idToRecordsMap,
                                               ManyAssembly config,
                                               Map<?, ?> dataMap,
                                               AssemblyService<?, ?> assemblyService) {
        for (Map.Entry<Object, Set<Object>> entry : idToRecordsMap.entrySet()) {
            Object id = entry.getKey();
            Set<Object> records = entry.getValue();

            Object data = dataMap.get(id);
            if (data != null && StrUtil.isNotBlank(config.targetField())) {
                // 批量设置相同ID的记录
                Object targetValue = assemblyService.getTargetValue(data);
                for (Object record : records) {
                    setTargetValueByReflection(record, config.targetField(), targetValue);
                }
            }
        }
    }

    /**
     * 获取装配服务实例
     *
     * @param multiAssembly 多字段装配注解配置
     * @return 装配服务实例
     */
    private AssemblyService<?, ?> getAssemblyService(MultiAssembly multiAssembly) {
        String beanName = multiAssembly.beanName();
        Class<? extends AssemblyService<?, ?>> serviceClass = multiAssembly.service();

        // 优先使用beanName获取
        if (StrUtil.isNotBlank(beanName)) {
            try {
                return applicationContext.getBean(beanName, AssemblyService.class);
            } catch (Exception e) {
                log.error("通过beanName获取装配服务失败: {}", beanName, e);
            }
        }

        // 如果beanName未指定或获取失败，使用serviceClass获取
        if (serviceClass != null && serviceClass != AssemblyService.class) {
            try {
                return applicationContext.getBean(serviceClass);
            } catch (Exception e) {
                log.error("通过serviceClass获取装配服务失败: {}", serviceClass.getName(), e);
            }
        }

        return null;
    }

    /**
     * 通过反射获取ID
     *
     * @param obj       目标对象
     * @param fieldName 字段名称，支持嵌套路径如：crmVisitPlanDetailBaseVO.visitorId
     * @return ID，获取失败时返回 null
     */
    private Object getIdByReflection(Object obj, String fieldName) {
        try {
            return getFieldValueByPath(obj, fieldName);
        } catch (Exception e) {
            log.error("获取ID失败: {}.{}", obj.getClass().getSimpleName(), fieldName, e);
            return null;
        }
    }

    /**
     * 通过路径获取字段值，支持嵌套对象
     *
     * @param obj  目标对象
     * @param path 字段路径，如：crmVisitPlanDetailBaseVO.visitorId
     * @return 字段值
     */
    private Object getFieldValueByPath(Object obj, String path) {
        if (obj == null || StrUtil.isBlank(path)) {
            return null;
        }

        String[] fieldNames = path.split("\\.");
        Object currentObj = obj;

        for (String fieldName : fieldNames) {
            if (currentObj == null) {
                return null;
            }
            currentObj = ReflectUtil.getFieldValue(currentObj, fieldName);
        }

        return currentObj;
    }

    /**
     * 通过反射设置目标值
     *
     * @param obj       目标对象
     * @param fieldName 字段名称，支持嵌套路径如：crmVisitPlanDetailBaseVO.visitorName
     * @param value     要设置的值
     */
    private void setTargetValueByReflection(Object obj, String fieldName, Object value) {
        try {
            setFieldValueByPath(obj, fieldName, value);
        } catch (Exception e) {
            log.error("设置目标值失败: {}.{}", obj.getClass().getSimpleName(), fieldName, e);
        }
    }

    /**
     * 通过路径设置字段值，支持嵌套对象
     *
     * @param obj   目标对象
     * @param path  字段路径，如：crmVisitPlanDetailBaseVO.visitorName
     * @param value 要设置的值
     */
    private void setFieldValueByPath(Object obj, String path, Object value) {
        if (obj == null || StrUtil.isBlank(path)) {
            return;
        }

        String[] fieldNames = path.split("\\.");

        // 如果只有一个字段名，直接设置
        if (fieldNames.length == 1) {
            ReflectUtil.setFieldValue(obj, fieldNames[0], value);
            return;
        }

        // 获取到最后一个字段的父对象
        Object currentObj = obj;
        for (int i = 0; i < fieldNames.length - 1; i++) {
            if (currentObj == null) {
                return;
            }
            currentObj = ReflectUtil.getFieldValue(currentObj, fieldNames[i]);
        }

        // 设置最后一个字段的值
        if (currentObj != null) {
            ReflectUtil.setFieldValue(currentObj, fieldNames[fieldNames.length - 1], value);
        }
    }
}