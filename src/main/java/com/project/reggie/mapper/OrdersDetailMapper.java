package com.project.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.reggie.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ProgZhou
 * @createTime 2022/06/09
 */
@Mapper
public interface OrdersDetailMapper extends BaseMapper<OrderDetail> {
}
