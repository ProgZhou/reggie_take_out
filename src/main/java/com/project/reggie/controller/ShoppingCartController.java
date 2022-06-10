package com.project.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.reggie.common.BaseContext;
import com.project.reggie.common.CommonResult;
import com.project.reggie.entity.ShoppingCart;
import com.project.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ProgZhou
 * @createTime 2022/06/08
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /*
    * 将菜品或者套餐添加到购物车
    * */
    @PostMapping("/add")
    public CommonResult<ShoppingCart> addShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        log.info("待添加的商品: {}", shoppingCart);
        //设置用户id
        Long userId = BaseContext.getCurrentUserId();
        shoppingCart.setUserId(userId);
        //查询当前表中是否有此商品
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        if(dishId != null) {
            //添加的是菜品
            wrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
            //由于前端在添加第二份时不能选择口味，这个条件就不成立，如果有这个需求可以添加
            // wrapper.eq(ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor());
        } else {
            //添加的是套餐
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart cart = shoppingCartService.getOne(wrapper);
        if(cart != null) {
            //如果已经存在，就在原来数量的基础上 + 1
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartService.updateById(cart);
        } else {
            //如果不存在，就直接添加到数据表中
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
        }
        return CommonResult.success(shoppingCart);
    }


    @PostMapping("/sub")
    public CommonResult<ShoppingCart> subShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        Long userId = BaseContext.getCurrentUserId();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        if(shoppingCart.getDishId() != null) {
            wrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart cart = shoppingCartService.getOne(wrapper);
        if(cart.getNumber() > 1) {
            cart.setNumber(cart.getNumber() - 1);
            shoppingCartService.updateById(cart);
        } else {
            shoppingCartService.remove(wrapper);
        }

        return CommonResult.success(cart);
    }

    @GetMapping("/list")
    public CommonResult<List<ShoppingCart>> getList() {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentUserId());
        wrapper.orderByDesc(ShoppingCart::getCreateTime);

        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);

        return CommonResult.success(shoppingCarts);
    }

    /*
    * 清空购物车
    * */
    @DeleteMapping("/clean")
    public CommonResult<String> cleanShoppingCart() {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentUserId());

        shoppingCartService.remove(wrapper);
        return CommonResult.success("清空购物车成功");
    }

}
