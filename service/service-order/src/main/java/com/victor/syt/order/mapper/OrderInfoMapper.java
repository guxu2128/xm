package com.victor.syt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.victor.syt.model.order.OrderInfo;
import com.victor.syt.vo.order.OrderCountQueryVo;
import com.victor.syt.vo.order.OrderCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    List<OrderCountVo> selectOrderCount(@Param("vo") OrderCountQueryVo orderCountQueryVo);
}

