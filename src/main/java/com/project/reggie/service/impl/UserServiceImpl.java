package com.project.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.reggie.entity.User;
import com.project.reggie.mapper.UserMapper;
import com.project.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author ProgZhou
 * @createTime 2022/06/08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
