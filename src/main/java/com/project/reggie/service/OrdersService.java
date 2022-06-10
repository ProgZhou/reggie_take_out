package com.project.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.reggie.entity.Orders;

/**
 * @author ProgZhou
 * @createTime 2022/06/09
 */
public interface OrdersService extends IService<Orders> {

    /*
    * 用户下单方法
    * */
    void submit(Orders orders);

}
