package com.project.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.reggie.dto.SetmealDto;
import com.project.reggie.entity.Setmeal;
import com.project.reggie.entity.SetmealDish;
import com.project.reggie.exception.ServiceException;
import com.project.reggie.mapper.SetmealDishMapper;
import com.project.reggie.mapper.SetmealMapper;
import com.project.reggie.service.SetmealDishService;
import com.project.reggie.service.SetmealService;
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
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private SetmealDishService setmealDishService;

    /*
    * 新增套餐，同时保存套餐中的菜品信息
    * */
    @Override
    @Transactional
    public void addSetmealWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息
        setmealMapper.insert(setmealDto);

        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();

        setmealDishList = setmealDishList.stream().peek((item) -> item.setSetmealId(setmealDto.getId())).collect(Collectors.toList());

        //保存套餐和菜品的关联信息
        setmealDishService.saveBatch(setmealDishList);
    }

    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        //查询套餐状态，确定是否能够删除套餐
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId, ids);
        wrapper.eq(Setmeal::getStatus, 1);
        Integer count = setmealMapper.selectCount(wrapper);
        if(count > 0) {
            //如果count > 0说明待删除的套餐中有正在售卖的，则需要抛出异常
            throw new ServiceException("套餐正在售卖中，不能删除");
        }

        //删除套餐表中的数据
        setmealMapper.deleteBatchIds(ids);

        //删除菜品的关联数据
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getSetmealId, ids);

        setmealDishMapper.delete(queryWrapper);



    }

    /*
    * 根据id获取套餐信息，包括套餐包含的菜品信息
    * */
    @Override
    public SetmealDto getSetMealWithDishById(Long id) {

        //查询套餐的基本信息
        Setmeal setmeal = setmealMapper.selectById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, setmeal.getId());

        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(wrapper);

        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;
    }

    /*
     * 更新套餐表，同时更新套餐-菜品联系表
     * */
    @Override
    public void updateSetMealWithDish(SetmealDto setmealDto) {
        //更新套餐表
        setmealMapper.updateById(setmealDto);

        //1. 清楚当前套餐中的所有菜品
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishMapper.delete(wrapper);

        //2. 添加当前提交过来的菜品
        List<SetmealDish> dtoSetmealDishes = setmealDto.getSetmealDishes();
        dtoSetmealDishes = dtoSetmealDishes.stream().peek((item) -> item.setSetmealId(setmealDto.getId())).collect(Collectors.toList());
        setmealDishService.saveBatch(dtoSetmealDishes);
    }
}
