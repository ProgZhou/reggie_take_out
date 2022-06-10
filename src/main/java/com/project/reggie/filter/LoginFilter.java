package com.project.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.project.reggie.common.BaseContext;
import com.project.reggie.common.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 拦截请求，如果没登录，则回到登录界面
 * 这里可以使用过滤器，也可以使用拦截器，效果相同
 * @author ProgZhou
 * @createTime 2022/06/04
 */
//拦截所有请求
@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
@Slf4j
public class LoginFilter implements Filter {

    //字符串匹配，支持正则表达式
    public static AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截到请求: {}", request.getRequestURL());
//        log.info("Thread: {}", Thread.currentThread().getId());

        String uri = request.getRequestURI();

        /*
        * 登录登出方法不需要拦截
        * 静态资源不需要拦截
        * */
        String[] urls = {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login"
        };

        //判断请求是否需要处理
        if (check(urls, uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        //4-1 判断员工登录状态   --  后台管理

        if (request.getSession().getAttribute("employee") != null) {
            Long id = (Long) request.getSession().getAttribute("employee");
            //将登录用户的id放入ThreadLocal中，便于之后的使用
            BaseContext.setCurrentUserId(id);
            log.info("当前登录用户id: {}", id);
            filterChain.doFilter(request, response);
            return;
        }

        //4-2 判断用户登录状态  --  移动端管理
        if (request.getSession().getAttribute("user") != null) {
            Long userId = (Long) request.getSession().getAttribute("user");
            //将登录用户的id放入ThreadLocal中，便于之后的使用
            BaseContext.setCurrentUserId(userId);
            log.info("当前登录用户id: {}", userId);
            filterChain.doFilter(request, response);
            return;
        }


        //返回未登录信息
        response.getWriter().write(JSON.toJSONString(CommonResult.error("NOTLOGIN")));

    }

    //检查本次请求是否能够放行
    public boolean check(String[] urls, String uri) {
        for (String url : urls) {
            boolean match = pathMatcher.match(url, uri);
            if(match) {
                return true;
            }
        }
        return false;
    }
}
