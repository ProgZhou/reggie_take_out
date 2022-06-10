package com.project.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/** 后台管理员映射类
 * @author ProgZhou
 * @createTime 2022/06/03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
    * 用户id
    * */
    private Long id;

    /*
    * 用户真实姓名
    * */
    private String name;

    /*
    * 用户登录用户名
    * */
    private String username;

    /*
    * 用户登录密码
    * */
    private String password;

    /*
    * 用户手机号
    * */
    private String phone;

    /*
    * 用户性别
    * */
    private String sex;

    /*
    * 用户身份证号
    * */
    private String idNumber;

    /*
    * 用户状态，1表示正常；0表示禁用
    * */
    private Integer status;

    /*
    * 用户创建时间
    * */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /*
    * 用户信息更新时间
    * */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
