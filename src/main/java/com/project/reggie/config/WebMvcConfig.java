package com.project.reggie.config;

import com.project.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/** 配置类
 * @author ProgZhou
 * @createTime 2022/06/03
 */
@Slf4j
//@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /*
    * 配置静态资源映射
    * 如果静态资源没有放在springboot默认的目录static或者templates下，则可以通过这个配置类进行资源映射
    * */
//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        log.info("静态资源映射");
//        //addResourceHandler()表示资源访问路径/**表示某目录下的所有文件
//        //addResourceLocation()表示资源在服务器中的位置
//        registry.addResourceHandler("/backend/**")
//                .addResourceLocations("classpath:/backend/");
//    }

    /*
     * 配置消息转换器，主要是将Long类型的数据转换为字符串类型
     * */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器
        converter.setObjectMapper(new JacksonObjectMapper());
        //将新建的转换器追加到mvc框架的转换器中
        converters.add(0, converter);
    }
}
