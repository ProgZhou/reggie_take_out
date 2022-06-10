package com.project.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/** 操作category表的Mapper接口
 * @author ProgZhou
 * @createTime 2022/06/05
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
