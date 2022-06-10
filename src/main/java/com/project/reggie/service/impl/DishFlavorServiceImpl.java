package com.project.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.reggie.entity.DishFlavor;
import com.project.reggie.mapper.DishFlavorMapper;
import com.project.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @author ProgZhou
 * @createTime 2022/06/06
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
