package com.project.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 套餐-菜品关系实体类
 * @author ProgZhou
 * @createTime 2022/06/07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetmealDish implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
    * 套餐-菜品关系id
    * */
    private Long id;

    /*
    * 套餐id
    * */
    private Long setmealId;

    /*
    * 菜品id
    * */
    private Long dishId;

    /*
    * 菜品名称
    * */
    private String name;

    /*
    * 菜品原价
    * */
    private BigDecimal price;

    /*
    * 套餐份数
    * */
    private Integer copies;

    /*
    * 套餐排序
    * */
    private Integer sort;

    /*
     * 套餐创建时间
     * */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /*
     * 套餐更新时间
     * */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /*
     * 套餐创建用户
     * */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /*
     * 更新套餐用户
     * */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /*
     * 套餐是否删除
     * */
    @TableField("is_deleted")
    private Integer deleted;

}
