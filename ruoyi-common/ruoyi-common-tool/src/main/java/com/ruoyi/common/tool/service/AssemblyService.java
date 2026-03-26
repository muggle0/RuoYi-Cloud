package com.ruoyi.common.tool.service;

import java.util.List;
import java.util.Map;

/**
 * 通用装配服务接口
 * <p>
 * 用于定义获取数据的服务接口，支持批量获取数据。
 * </p>
 *
 * @param <ID> ID类型
 * @param <T> 数据类型
 * @author ruoyi
 * @since 2026-03-26
 */
public interface AssemblyService<ID, T> {

    /**
     * 批量获取数据
     * <p>
     * 根据ID列表批量获取数据，并返回ID与数据的映射关系。
     * </p>
     *
     * @param ids ID列表
     * @return ID与数据的映射关系
     */
    Map<ID, T> batchGetData(List<Object> ids);

    /**
     * 获取目标值
     * <p>
     * 从数据对象中获取需要装配的目标值。
     * </p>
     *
     * @param data 数据对象
     * @return 目标值
     */
    Object getTargetValue(Object data);
}