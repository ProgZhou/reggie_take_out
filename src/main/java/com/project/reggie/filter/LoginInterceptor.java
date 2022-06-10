package com.project.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.project.reggie.common.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** 未登录拦截  --- 拦截器版
 * @author ProgZhou
 * @createTime 2022/06/04
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //执行登录检查
        HttpSession session = request.getSession();

        log.info("拦截到请求: {}", request.getRequestURL());

        Object employee = session.getAttribute("employee");

        //如果已登录，则放行
        if(employee != null) {
            log.info("用户已登陆");
            return true;
        }

        //如果未登录则拦截
        log.info("用户未登录" );
        response.getWriter().write(JSON.toJSONString(CommonResult.error("NOTLOGIN")));

        return false;
    }
}
