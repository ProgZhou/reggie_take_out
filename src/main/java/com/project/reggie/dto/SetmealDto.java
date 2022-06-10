package com.project.reggie.dto;

import com.project.reggie.entity.Setmeal;
import com.project.reggie.entity.SetmealDish;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/** 套餐值传输对象
 * @author ProgZhou
 * @createTime 2022/06/07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;

    /*
    * 套餐分类名称
    * */
    private String categoryName;
}
