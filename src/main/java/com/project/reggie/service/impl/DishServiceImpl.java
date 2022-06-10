package com.project.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.reggie.dto.DishDto;
import com.project.reggie.entity.Dish;
import com.project.reggie.entity.DishFlavor;
import com.project.reggie.exception.ServiceException;
import com.project.reggie.mapper.DishFlavorMapper;
import com.project.reggie.mapper.DishMapper;
import com.project.reggie.service.DishFlavorService;
import com.project.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ProgZhou
 * @createTime 2022/06/05
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /*
    * 新增菜品，同时保存菜品口味数据
    * */
    @Override
    @Transactional
    public void addWithFlavor(DishDto dishDto) {
        //将菜品数据保存到菜品表
        dishMapper.insert(dishDto);

        //为封装在dishDto中的DishFlavor对象添加dishId
        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().peek((item) -> item.setDishId(dishId)).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }

    /*
    * 根据id获取菜品信息，同时获取口味信息
    * */
    @Override
    public DishDto getDishByIdWithFlavor(Long id) {
        //查询菜品基本信息
        Dish dish = dishMapper.selectById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dish.getId());

        List<DishFlavor> dishFlavors = dishFlavorService.list(wrapper);
        dishDto.setFlavors(dishFlavors);

        return dishDto;
    }

    @Override
    public void updateDishWithFlavor(DishDto dishDto) {
        //更新dish表
        dishMapper.updateById(dishDto);
        //更新口味表
        //1. 清理当前菜品对应的口味数据
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorMapper.delete(wrapper);
        //2. 添加当前提交过来的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().peek((item) -> item.setDishId(dishDto.getId())).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void deleteDishWithFlavor(List<Long> ids) {

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();

        wrapper.in(Dish::getId, ids);
        wrapper.eq(Dish::getStatus, 1);

        Integer count = dishMapper.selectCount(wrapper);
        if(count > 0) {
            //如果查询到有正在售卖中的菜品
            throw new ServiceException("菜品正在售卖中，不能删除");
        }

        //删除菜品表中的信息
        dishMapper.deleteBatchIds(ids);

        //删除菜品_口味表中的信息
        LambdaQueryWrapper<DishFlavor> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.in(DishFlavor::getDishId, ids);

        dishFlavorMapper.delete(deleteWrapper);

        //直接删除菜品口味表中的口味信息
//        dishFlavorMapper.deleteBatchIds(ids);
//        dishMapper.deleteBatchIds(ids);
    }
}
