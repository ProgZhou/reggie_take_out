package com.project.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ProgZhou
 * @createTime 2022/06/08
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
