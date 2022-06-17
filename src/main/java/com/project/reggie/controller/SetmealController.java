package com.project.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.reggie.common.CommonResult;
import com.project.reggie.dto.SetmealDto;
import com.project.reggie.entity.Category;
import com.project.reggie.entity.Setmeal;
import com.project.reggie.service.CategoryService;
import com.project.reggie.service.SetmealDishService;
import com.project.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** 处理套餐请求的controller
 * @author ProgZhou
 * @createTime 2022/06/07
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;


    /*
    * 新增套餐
    * */
    @PostMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public CommonResult<String> addSetMeal(@RequestBody SetmealDto setmealDto) {
        log.info("接收到数据: {}", setmealDto);

        setmealService.addSetmealWithDish(setmealDto);
        return CommonResult.success("新增套餐成功");
    }


    @GetMapping("/{id}")
    public CommonResult<SetmealDto> getSetmealById(@PathVariable("id") Long id) {
        log.info("待查询的菜品: {}", id);

        SetmealDto dto = setmealService.getSetMealWithDishById(id);

        return CommonResult.success(dto);
    }

    /*
    * 套餐分页查询
    * */
    @GetMapping("/page")
    public CommonResult<Page<SetmealDto>> getPages(int page, int pageSize, String name) {
        Page<SetmealDto> pageInfo = new Page<>();
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Setmeal::getName, name);
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, wrapper);
        BeanUtils.copyProperties(setmealPage, pageInfo, "records");

        List<Setmeal> setmeals = setmealPage.getRecords();

        List<SetmealDto> list = setmeals.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();

            Category category = categoryService.getById(item.getCategoryId());

            if(category != null) {
                BeanUtils.copyProperties(item, setmealDto);
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());

        pageInfo.setRecords(list);

        return CommonResult.success(pageInfo);
    }


    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true)    //当删除某个套餐时，需要删除缓存中的套餐数据
    public CommonResult<String> deleteSetMeals(@RequestParam("ids") String ids) {
        log.info("待删除的套餐id: {}", ids);

        List<Long> setmealIds = new ArrayList<>();
        if(ids.contains(",")) {
            String[] temp = ids.split(",");
            for (String id : temp) {
                setmealIds.add(Long.parseLong(id));
            }
            setmealService.deleteWithDish(setmealIds);
        } else {
            setmealIds.add(Long.parseLong(ids));
            setmealService.deleteWithDish(setmealIds);
        }

        return CommonResult.success("删除成功");
    }

    @PutMapping
    public CommonResult<String> updateSetMeal(@RequestBody SetmealDto setmealDto) {
        log.info("待修改的套餐信息: {}", setmealDto);
        setmealService.updateSetMealWithDish(setmealDto);
        return CommonResult.success("修改成功");
    }


    /*
    * 根据分类查询套餐信息
    * */
    @GetMapping("/list")
    //将方法的返回结果存入redis缓存，返回结果如果是引用数据类型需要实现Serializable接口实现序列化
    @Cacheable(value = "setmealCache", key = "'setMeal' + #setmeal.categoryId + '_' + #setmeal.status")
    public CommonResult<List<Setmeal>> getList(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId, setmeal.getCategoryId());
        wrapper.eq(Setmeal::getStatus, 1);
        wrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> setmealList = setmealService.list(wrapper);

        return CommonResult.success(setmealList);
    }

}
