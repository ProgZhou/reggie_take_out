package com.project.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.reggie.entity.OrderDetail;
import com.project.reggie.mapper.OrdersDetailMapper;
import com.project.reggie.service.OrdersDetailService;
import org.springframework.stereotype.Service;

/**
 * @author ProgZhou
 * @createTime 2022/06/09
 */
@Service
public class OrdersDetailServiceImpl extends ServiceImpl<OrdersDetailMapper, OrderDetail> implements OrdersDetailService {
}
