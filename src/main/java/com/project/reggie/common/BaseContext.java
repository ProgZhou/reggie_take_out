package com.project.reggie.common;

/** 基于ThreadLocal的封装工具类，用于保存和获取当前登录用户id
 * @author ProgZhou
 * @createTime 2022/06/05
 */
public abstract class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentUserId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentUserId() {
        return threadLocal.get();
    }
}
