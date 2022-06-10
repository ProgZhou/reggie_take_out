package com.project.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.reggie.common.CommonResult;
import com.project.reggie.dto.DishDto;
import com.project.reggie.entity.Category;
import com.project.reggie.entity.Dish;
import com.project.reggie.entity.DishFlavor;
import com.project.reggie.service.CategoryService;
import com.project.reggie.service.DishFlavorService;
import com.project.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/** 处理菜品的controller
 * @author ProgZhou
 * @createTime 2022/06/06
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @PostMapping
    public CommonResult<String> addDish(@RequestBody DishDto dishDto) {
        log.info("接收到的数据: {}", dishDto);

        dishService.addWithFlavor(dishDto);
        String key = "Dish_" + dishDto.getCategoryId() + "_status_" + dishDto.getStatus();
        redisTemplate.delete(key);
        return CommonResult.success("新增菜品成功");
    }


    /*
    * 菜品信息分页查询
    * */
    @GetMapping("/page")
    public CommonResult<Page<DishDto>> getPages(int page, int pageSize, String name) {

        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> pageInfo = new Page<>();

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(name != null, Dish::getName, name);
        wrapper.orderByDesc(Dish::getUpdateTime);
        wrapper.eq(Dish::getDeleted, 0);

        dishService.page(dishPage, wrapper);

        //对象拷贝，将Dish的查询结果dishPage中的属性拷贝给pageInfo
        BeanUtils.copyProperties(dishPage, pageInfo, "records");

        List<Dish> dishes = dishPage.getRecords();

        List<DishDto> list = dishes.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            //将item中的属性拷贝到dishDto中
            BeanUtils.copyProperties(item, dishDto);
            //根据id查询分类对象
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            //设置分类名称
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());

        pageInfo.setRecords(list);
        return CommonResult.success(pageInfo);
    }

    /*
    * 根据id查询菜品信息
    * */
    @GetMapping("/{id}")
    public CommonResult<DishDto> getDish(@PathVariable("id") Long id) {
        log.info("查询菜品: {}", id);
        DishDto dish = dishService.getDishByIdWithFlavor(id);

        return CommonResult.success(dish);
    }


    @PutMapping
    public CommonResult<String> updateDish(@RequestBody DishDto dishDto) {
        log.info("待修改的菜品信息: {}", dishDto);
        dishService.updateDishWithFlavor(dishDto);
        //清除redis中的缓存数据，这里清除修改过的分类下的数据
        String key = "Dish_" + dishDto.getCategoryId() + "_status_" + dishDto.getStatus();
        redisTemplate.delete(key);
        return CommonResult.success("修改菜品信息成功");
    }


    /*
    * 菜品的启用，停用
    * 支持批量操作
    * */
    @PostMapping("/status/{status}")
    public CommonResult<String> updateStatus(@PathVariable("status") Integer status, @RequestParam("ids") String ids) {
        log.info("待修改菜品: {}, 修改status至: {}", ids, status);
        //如果输入的参数包含","说明是批量操作
        if(ids.contains(",")) {
            String[] strings = ids.split(",");
            List<Long> list = new ArrayList<>();
            for (String id : strings) {
                list.add(Long.parseLong(id));
            }
            LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Dish::getId, list);
            List<Dish> dishList = dishService.list(wrapper);
            dishList = dishList.stream().peek((item) -> item.setStatus(status)).collect(Collectors.toList());
            dishService.updateBatchById(dishList);
        } else {
            Dish dish = dishService.getById(Long.parseLong(ids));
            dish.setStatus(status);
            dishService.updateById(dish);
        }


        return CommonResult.success("操作成功");
    }

    /*
    * 删除菜品
    * 支持批量删除
    * */
    @DeleteMapping
    public CommonResult<String> delete(@RequestParam("ids") String ids) {
        log.info("待删除的菜品id: {}", ids);

        //假删
//        if(ids.contains(",")) {
//            String[] strings = ids.split(",");
//            List<Long> list = new ArrayList<>();
//            for (String id : strings) {
//                list.add(Long.parseLong(id));
//            }
            //直接删除菜品口味表中的口味信息
//            dishFlavorService.removeByIds(list);
//            LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
//            wrapper.in(Dish::getId, list);
//            List<Dish> dishList = dishService.list(wrapper);
//            dishList = dishList.stream().peek((item) -> item.setDeleted(1)).collect(Collectors.toList());
//            dishService.updateBatchById(dishList);
//        } else {
//            Dish dish = dishService.getById(Long.parseLong(ids));
//            dish.setDeleted(1);
//            dishService.updateById(dish);
//        }
        //真删
        if (ids.contains(",")) {
            String[] strings = ids.split(",");
            List<Long> list = new ArrayList<>();
            for (String id : strings) {
                list.add(Long.parseLong(id));
            }
            dishService.deleteDishWithFlavor(list);
        } else {
            List<Long> id = new ArrayList<>();
            id.add(Long.parseLong(ids));
            dishService.deleteDishWithFlavor(id);
        }
        return CommonResult.success("删除成功");
    }

    /*
    * 根据分类id查询菜品
    * */
//    @GetMapping("/list")
//    public CommonResult<List<Dish>> getDishByCategoryId(Dish dish) {
//        log.info("得到categoryId: {}", dish.getCategoryId());
//
//        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//        //只查询正在售卖的菜品
//        wrapper.eq(Dish::getStatus, 1);
//
//        wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> dishList = dishService.list(wrapper);
//
//        return CommonResult.success(dishList);
//    }

    /*
    * 根据分类id查询菜品信息，并且返回DishDto对象
    * */
    @GetMapping("/list")
    public CommonResult<List<DishDto>> getDishByCategoryId(Dish dish) {
        log.info("得到categoryId: {}", dish.getCategoryId());

        List<DishDto> dishDtoList = null;

        //1. 尝试从redis中获取数据
        //拼接key
        String dishKey = "Dish_" + dish.getCategoryId() + "_status_" + dish.getStatus();
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(dishKey);
        if(dishDtoList != null) {
            //2. 如果存在，则直接返回
            return CommonResult.success(dishDtoList);
        }

        //3. 如果不存在，则需要查询数据库
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //只查询正在售卖的菜品
        wrapper.eq(Dish::getStatus, 1);

        wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = dishService.list(wrapper);

        dishDtoList = dishList.stream().map((item) -> {
            DishDto dto = new DishDto();
            BeanUtils.copyProperties(item, dto);
            //根据id查询分类对象
            Long categoryId = item.getCategoryId();
            if(categoryId != null) {
                Category category = categoryService.getById(categoryId);
                //设置分类名称
                dto.setCategoryName(category.getName());
            }
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
            dto.setFlavors(dishFlavors);
            return dto;
        }).collect(Collectors.toList());

        //将查询出来的数据缓存到redis中
        redisTemplate.opsForValue().set(dishKey, dishDtoList, 30, TimeUnit.MINUTES);

        return CommonResult.success(dishDtoList);
    }

}
