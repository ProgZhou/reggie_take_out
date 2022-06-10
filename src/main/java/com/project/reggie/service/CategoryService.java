package com.project.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.reggie.entity.Category;

/** CategoryService接口
 * @author ProgZhou
 * @createTime 2022/06/05
 */
public interface CategoryService extends IService<Category> {

    /*
     * 根据id删除分类
     * */
    void remove(Long id);

}
