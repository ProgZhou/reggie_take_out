package com.project.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.reggie.common.BaseContext;
import com.project.reggie.common.CommonResult;
import com.project.reggie.dto.OrdersDto;
import com.project.reggie.entity.AddressBook;
import com.project.reggie.entity.OrderDetail;
import com.project.reggie.entity.Orders;
import com.project.reggie.service.AddressBookService;
import com.project.reggie.service.OrdersDetailService;
import com.project.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ProgZhou
 * @createTime 2022/06/09
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersDetailService ordersDetailService;

    @Autowired
    private AddressBookService addressBookService;

    /*
    * 用户下单
    * */
    @PostMapping("/submit")
    public CommonResult<String> submit(@RequestBody Orders orders) {
        log.info("订单数据: {}", orders);
        ordersService.submit(orders);
        return CommonResult.success("下单成功，马上配送");
    }

    /*
    * 分页查询
    * */
    @GetMapping("/userPage")
    public CommonResult<Page<OrdersDto>> getPage(int page, int pageSize) {

        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, BaseContext.getCurrentUserId());

        ordersService.page(ordersPage, wrapper);

        List<OrdersDto> ordersDtoList = ordersPage.getRecords().stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            LambdaQueryWrapper<OrderDetail> orderDetailWrapper = new LambdaQueryWrapper<>();
            orderDetailWrapper.eq(OrderDetail::getOrderId, item.getNumber());
            //查询订单的详细信息
            List<OrderDetail> orderDetails = ordersDetailService.list(orderDetailWrapper);
            ordersDto.setOrderDetails(orderDetails);
            //查询用户的基本信息
            AddressBook addressBook = addressBookService.getById(item.getAddressBookId());
            ordersDto.setAddress(addressBook.getDetail());
            ordersDto.setConsignee(addressBook.getConsignee());
            ordersDto.setPhone(addressBook.getPhone());
            return ordersDto;
        }).collect(Collectors.toList());

        Page<OrdersDto> pageInfo = new Page<>();
        BeanUtils.copyProperties(ordersPage, pageInfo, "records");
        pageInfo.setRecords(ordersDtoList);
        return CommonResult.success(pageInfo);
    }

}
