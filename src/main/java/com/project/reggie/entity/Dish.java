package com.project.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 菜品实体类
 * @author ProgZhou
 * @createTime 2022/06/05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dish implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
    * 菜品id
    * */
    private Long id;

    /*
    * 菜品名称
    * */
    private String name;

    /*
    * 菜品分类id
    * */
    private Long categoryId;

    /*
    * 菜品价格
    * */
    private BigDecimal price;

    /*
    * 商品码
    * */
    private String code;

    /*
    * 菜品图片
    * */
    private String image;

    /*
    * 菜品描述
    * */
    private String description;

    /*
    * 菜品状态  0表示停售  1表示售卖中
    * */
    private Integer status;

    /*
    * 菜品顺序
    * */
    private Integer sort;

    /*
     * 菜品创建时间
     * */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /*
     * 菜品更新时间
     * */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /*
     * 创建菜品的用户
     * */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /*
     * 更新菜品的用户
     * */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /*
    * 菜品是否删除  0表示未删除  1表示已删除
    * */
    @TableField("is_deleted")
    private Integer deleted;

}
