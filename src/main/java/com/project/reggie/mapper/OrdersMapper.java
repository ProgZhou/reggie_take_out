package com.project.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ProgZhou
 * @createTime 2022/06/09
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
