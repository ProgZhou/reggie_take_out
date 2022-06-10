package com.project.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.reggie.common.BaseContext;
import com.project.reggie.entity.*;
import com.project.reggie.exception.ServiceException;
import com.project.reggie.mapper.*;
import com.project.reggie.service.OrdersDetailService;
import com.project.reggie.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author ProgZhou
 * @createTime 2022/06/09
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private OrdersDetailService ordersDetailService;

    @Override
    @Transactional
    public void submit(Orders orders) {
        //获得当前用户
        Long userId = BaseContext.getCurrentUserId();

        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> shoppingCartWrapper = new LambdaQueryWrapper<>();
        shoppingCartWrapper.eq(ShoppingCart::getUserId, userId);

        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectList(shoppingCartWrapper);
        if(shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new ServiceException("当前购物车为空，不能下单");
        }

        //查询用户数据
        User currentUser = userMapper.selectById(userId);
        //查询当前用户地址信息
        AddressBook addressBook = addressBookMapper.selectById(orders.getAddressBookId());
        if(addressBook == null) {
            throw new ServiceException("地址信息有误，请补全之后再下单");
        }

        AtomicInteger amount = new AtomicInteger();

        Long orderId = IdWorker.getId();

        //计算总金额
        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        //向订单表插入数据
        orders.setNumber(String.valueOf(orderId));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setUserName(currentUser.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));


        ordersMapper.insert(orders);

        //向订单明细表插入数据
        ordersDetailService.saveBatch(orderDetails);
        //下单完成之后清空购物车数据
        shoppingCartMapper.delete(shoppingCartWrapper);
    }
}
