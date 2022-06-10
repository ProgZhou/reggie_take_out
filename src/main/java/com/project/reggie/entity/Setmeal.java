package com.project.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 套餐实体类
 * @author ProgZhou
 * @createTime 2022/06/05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Setmeal implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
    * 套餐id
    * */
    private Long id;

    /*
    * 分类id
    * */
    private Long categoryId;


    /*
    * 套餐名称
    * */
    private String name;


    /*
    * 套餐价格
    * */
    private BigDecimal price;


    /*
    * 套餐状态  0表示停用  1表示启用
    * */
    private Integer status;

    /*
    * 套餐编码
    * */
    private String code;

    /*
    * 套餐描述信息
    * */
    private String description;


    /*
    * 套餐图片
    * */
    private String image;

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
