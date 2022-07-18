package com.victor.syt.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.victor.syt.model.order.OrderInfo;
import com.victor.syt.vo.order.OrderQueryVo;

import java.util.Map;

public interface OrderService extends IService<OrderInfo> {
    //保存订单
    Long saveOrder(String scheduleId, Long patientId);
    /**
     * 分页列表
     */
    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);
    /**
     * 获取订单详情
     */
    OrderInfo getOrderInfo(String orderId);
    /**
     * 订单详情
     * @param orderId
     * @return
     */
    Map<String,Object> show(Long orderId);

    void patientTips();

}
