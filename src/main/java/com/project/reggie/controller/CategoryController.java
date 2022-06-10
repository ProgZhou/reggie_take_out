package com.project.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.reggie.common.CommonResult;
import com.project.reggie.entity.Category;
import com.project.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 专门接收对于类型请求的Controller
 * @author ProgZhou
 * @createTime 2022/06/05
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    /*
    * 新增分类
    * */
    @PostMapping
    public CommonResult<String> addCate(@RequestBody Category category) {
        log.info("接收到分类信息: {}", category);
        categoryService.save(category);
        return CommonResult.success("添加成功");
    }


    /*
    * 分页查询分类信息
    * */
    @GetMapping("/page")
    public CommonResult<Page<Category>> getPage(int page, int pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();

        wrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo, wrapper);
        return CommonResult.success(pageInfo);
    }

    /*
    * 根据id删除分类信息
    * */
    @DeleteMapping
    public CommonResult<String> delete(@RequestParam("ids") Long id) {
        log.info("待分类的id为: {}", id);
//        categoryService.removeById(id);
        categoryService.remove(id);
        return CommonResult.success("分类删除成功");
    }

    /*
    * 修改分类
    * */
    @PutMapping
    public CommonResult<String> updateCategory(@RequestBody Category category) {
        log.info("修改分类信息: {}", category);
        categoryService.updateById(category);
        return CommonResult.success("修改成功");
    }

    /*
    * 根据查询分类数据
    * */
    @GetMapping("/list")
    public CommonResult<List<Category>> getList(Category category) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getType() != null, Category::getType, category.getType());
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> categoryList = categoryService.list(wrapper);
        return CommonResult.success(categoryList);
    }

}
