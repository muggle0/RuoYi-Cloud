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
package com.ruoyi.common.tool.query.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.tool.query.advisor.CriteriaAnnotationProcessorAdvisor;

import java.util.List;

/**
 * 抽象查询对象
 *
 * @author WcJun
 * @date 2019/05/12
 */
public abstract class AbstractQuery<T> extends PaginationQuery {

    protected List<OrderItem> orders;

    // WRAPPER

    /**
     * 通过注解自动包装查询 Wrapper
     *
     * @return
     * @see {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    public QueryWrapper<T> autoWrapper() {
        return CriteriaAnnotationProcessorAdvisor.advise(this, new QueryWrapper<T>());
    }

    // PAGE

    /**
     * 如果需要分页的话
     * 获取分页对象
     *
     * @return IPage
     * @see {@link com.baomidou.mybatisplus.core.metadata.IPage}
     */
    public IPage populatePage() {
        return new Page(this.pageNo, this.pageSize);
    }

    public List<OrderItem> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderItem> orders) {
        this.orders = orders;
    }
}