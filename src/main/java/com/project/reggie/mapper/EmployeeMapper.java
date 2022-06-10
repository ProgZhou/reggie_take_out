package com.project.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/** EmployeeMapper操作employee表
 * @author ProgZhou
 * @createTime 2022/06/03
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
