package com.project.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.reggie.common.CommonResult;
import com.project.reggie.entity.Employee;
import com.project.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/** 专门处理员工发起的请t
 * @author ProgZhou
 * @createTime 2022/06/03
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public CommonResult<Employee> login(@RequestBody Employee employee, HttpServletRequest request) {
        String username = employee.getUsername();
        //进行md5加密
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes(StandardCharsets.UTF_8));

        log.info("get data: {}, {}", username, password);

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, username);

        Employee user = employeeService.getOne(wrapper);

        if(null == user) {
            return CommonResult.error("登陆失败，用户不存在");
        }

        if (!user.getPassword().equals(password)) {
            return CommonResult.error("登录失败，用户名或密码不正确");
        }
        if (user.getStatus() == 0) {
            return CommonResult.error("登录失败，账号已禁用");
        }

        request.getSession().setAttribute("employee", user.getId());

        return CommonResult.success(user);
    }


    /*
    * 退出登录
    * */
    @PostMapping("/logout")
    public CommonResult<String> logout(HttpServletRequest request) {

        //清理Session中保存的id
        log.info("session: {}", request.getSession());
        request.getSession().removeAttribute("employee");

        return CommonResult.success("成功退出");
    }

    /*
    * 新增员工
    * */
    @PostMapping
    public CommonResult<String> addEmp(@RequestBody Employee employee, HttpServletRequest request) {
        log.info("新增员工: {}", employee);

        //设置初始密码，统一为123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        Long userId = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(userId);
//        employee.setUpdateUser(userId);

        employeeService.save(employee);
        return CommonResult.success("添加成功");
    }



    /*
    * 获取分页查询信息
    * */
    @GetMapping("/page")
    public CommonResult<Page<Employee>> getPage(int page, int pageSize, String name) {
        log.info("page: {}, pageSize: {}, name: {}", page, pageSize, name);

        Page<Employee> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        /*
        * 如果name不为空，才会将like name条件添加到sql语句中
        * */
        wrapper.like(!StringUtils.isEmpty(name), Employee::getName, "%" + name + "%");
        wrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo, wrapper);

        return CommonResult.success(pageInfo);
    }

    @PutMapping
    public CommonResult<String> updateEmp(@RequestBody Employee employee, HttpServletRequest request) {
        log.info("empId: {}, statue: {}", employee.getId(), employee.getStatus());
        log.info("Thread: {}", Thread.currentThread().getId());

        Long id = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(id);

        employeeService.updateById(employee);

        return CommonResult.success("信息修改成功");
    }

    /*
    * 根据员工的id查询员工信息，并返回
    * */
    @GetMapping("/{id}")
    public CommonResult<Employee> getEmpById(@PathVariable Long id) {
        log.info("id: {}", id);
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return CommonResult.success(employee);
        } else {
            return CommonResult.error("没有查询到员工信息");
        }
    }

}
