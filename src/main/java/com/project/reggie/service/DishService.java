package com.project.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.reggie.dto.DishDto;
import com.project.reggie.entity.Dish;

import java.util.List;

/**
 * @author ProgZhou
 * @createTime 2022/06/05
 */
public interface DishService extends IService<Dish> {

    /*
    * 新增菜品，同时插入菜品对应的口味数据
    * */
    void addWithFlavor(DishDto dishDto);

    /*
    * 根据菜品id查询菜品信息以及口味信息
    * */
    DishDto getDishByIdWithFlavor(Long id);

    /*
    * 更新菜品信息，同时更新相应的口味信息
    * */
    void updateDishWithFlavor(DishDto dishDto);

    /*
    * 删除菜品信息，同时删除对应的口味信息
    * */
    void deleteDishWithFlavor(List<Long> ids);

}
