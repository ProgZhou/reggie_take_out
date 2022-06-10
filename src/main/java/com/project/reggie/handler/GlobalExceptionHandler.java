package com.project.reggie.handler;

import com.project.reggie.common.CommonResult;
import com.project.reggie.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/** 全局异常处理
 * @author ProgZhou
 * @createTime 2022/06/04
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 进行异常处理方法
     * @return 返回异常信息
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public CommonResult<String> handler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());
        return CommonResult.error("请求失败");
    }

    @ExceptionHandler(ServiceException.class)
    public CommonResult<String> serviceHandler(ServiceException sex) {
        log.error(sex.getMessage());
        return CommonResult.error(sex.getMessage());
    }

}
