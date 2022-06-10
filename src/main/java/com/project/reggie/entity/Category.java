package com.project.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/** 分类实体类
 * @author ProgZhou
 * @createTime 2022/06/05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;
    /*
    * 菜品分类id
    * */
    private Long id;

    /*
    * 类型  1表示菜品分类  2表示套餐分类
    * */
    private Integer type;

    /*
    * 分类名称，唯一
    * */
    private String name;

    /*
    * 分类显示顺序
    * */
    private Integer sort;

    /*
    * 分类创建时间
    * */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /*
    * 分类更新时间
    * */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /*
    * 创建分类的用户
    * */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /*
    * 更新分类的用户
    * */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
