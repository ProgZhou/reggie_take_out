package com.project.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ProgZhou
 * @createTime 2022/06/05
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
