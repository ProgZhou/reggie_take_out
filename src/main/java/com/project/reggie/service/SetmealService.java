package com.project.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.reggie.dto.SetmealDto;
import com.project.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author ProgZhou
 * @createTime 2022/06/05
 */
public interface SetmealService extends IService<Setmeal> {

    /*
    * 新增套餐，同时保存套餐中的菜品信息
    * */
    void addSetmealWithDish(SetmealDto setmealDto);

    /*
    * 按照id删除套餐，同时删除套餐和菜品的关联数据
    * */
    void deleteWithDish(List<Long> ids);

    /*
    * 根据id获取
    * */
    SetmealDto getSetMealWithDishById(Long id);

    /*
    * 更新套餐表，同时更新套餐-菜品联系表
    * */
    void updateSetMealWithDish(SetmealDto setmealDto);

}
