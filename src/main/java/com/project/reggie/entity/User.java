package com.project.reggie.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/** 用户实体类
 * @author ProgZhou
 * @createTime 2022/06/08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
    * 用户id
    * */
    private Long id;

    /*
    * 用户姓名
    * */
    private String name;

    /*
    * 用户收集号
    * */
    private String phone;

    /*
    * 用户性别 0表示女  1表示男
    * */
    private String sex;

    /*
    * 用户身份证
    * */
    private String idNumber;

    /*
    * 用户头像
    * */
    private String avatar;

    /*
    * 用户账号状态  0表示禁用  1表示启用
    * */
    private Integer status;

}
