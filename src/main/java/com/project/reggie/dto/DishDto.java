package com.project.reggie.dto;

import com.project.reggie.entity.Dish;
import com.project.reggie.entity.DishFlavor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/** 菜品类的数据传输对象
 * @author ProgZhou
 * @createTime 2022/06/06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DishDto extends Dish {

    /*
    * 口味列表
    * */
    private List<DishFlavor> flavors = new ArrayList<>();

    /*
    * 分类名称
    * */
    private String categoryName;

    /*
    *
    * */
    private Integer copies;
}
