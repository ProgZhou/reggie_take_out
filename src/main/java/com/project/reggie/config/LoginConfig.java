package com.project.reggie.config;

import com.project.reggie.filter.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** 配置拦截器
 * @author ProgZhou
 * @createTime 2022/06/04
 */
//@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/employee/login", "/employee/logout"
                , "/backend/**", "/front/**");
    }


}
