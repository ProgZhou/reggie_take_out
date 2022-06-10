package com.project.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/** 菜品口味实体类
 * @author ProgZhou
 * @createTime 2022/06/06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishFlavor implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
    * 菜品口味id
    * */
    private Long id;

    /*
    * 对应菜品id
    * */
    private Long dishId;

    /*
    * 口味名称
    * */
    private String name;

    /*
    * 口味对应的值
    * */
    private String value;

    /*
     * 口味创建时间
     * */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /*
     * 菜品口味更新时间
     * */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /*
     * 创建菜品口味的用户
     * */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /*
     * 更新菜品口味的用户
     * */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /*
     * 菜品口味是否删除  0表示未删除  1表示已删除
     * */
    @TableField("is_deleted")
    private Integer deleted;

}
