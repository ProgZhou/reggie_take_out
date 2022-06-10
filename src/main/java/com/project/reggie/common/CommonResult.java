package com.project.reggie.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/** 通用返回类
 * @author ProgZhou
 * @createTime 2022/06/03
 */
@Data
@NoArgsConstructor
public class CommonResult<T>{

    /*
    * 响应状态码
    * */
    private Integer code;

    /*
    * 响应信息
    * */
    private String msg;

    /*
    * 响应数据
    * */
    private T data;

    /*
    * 数据存储
    * */
    private Map<String, Object> map = new HashMap<>();

    //响应成功返回数据
    public static <T> CommonResult<T> success(T object) {
        CommonResult<T> result = new CommonResult<>();
        result.data = object;
        result.code = 1;
        return result;
    }

    //响应失败返回数据
    public static <T> CommonResult<T> error(String msg) {
        CommonResult<T> result = new CommonResult<>();
        result.msg = msg;
        result.code = 0;
        return result;
    }

    public CommonResult<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
