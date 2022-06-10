package com.project.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.reggie.entity.Category;
import com.project.reggie.entity.Dish;
import com.project.reggie.entity.Setmeal;
import com.project.reggie.exception.ServiceException;
import com.project.reggie.mapper.CategoryMapper;
import com.project.reggie.mapper.DishMapper;
import com.project.reggie.mapper.SetmealMapper;
import com.project.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 实现类
 * @author ProgZhou
 * @createTime 2022/06/05
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /*
    * 根据id删除分类
    * */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品
        LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();
        dishWrapper.eq(Dish::getCategoryId, id);
        Integer dishCount = dishMapper.selectCount(dishWrapper);
        if(dishCount > 0) {
            //说明已经当前分类已经关联了菜品，抛出异常
            log.info("有{}个菜品关联了此分类", dishCount);
            throw new ServiceException("当前分类向关联了菜品，不能删除");
        }

        //查询当前分类是否关联了套餐
        LambdaQueryWrapper<Setmeal> setMealWrapper = new LambdaQueryWrapper<>();
        setMealWrapper.eq(Setmeal::getCategoryId, id);
        Integer setMealCount = setmealMapper.selectCount(setMealWrapper);
        if(setMealCount > 0) {
            //说明已经关联了套餐，抛出异常
            log.info("有{}个套餐关联了此分类", setMealCount);
            throw new ServiceException("当前分类向关联了套餐，不能删除");
        }

        //如果都没有，则正常删除
        categoryMapper.deleteById(id);

    }
}
