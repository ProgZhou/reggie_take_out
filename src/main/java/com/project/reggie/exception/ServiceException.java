package com.project.reggie.exception;

/** 自定义业务异常类
 * @author ProgZhou
 * @createTime 2022/06/05
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

}
