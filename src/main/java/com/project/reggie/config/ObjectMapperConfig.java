package com.project.reggie.config;

import com.project.reggie.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author ProgZhou
 * @createTime 2022/06/04
 */
@Configuration
public class ObjectMapperConfig implements WebMvcConfigurer {

    /*
     * 配置消息转换器，主要是将Long类型的数据转换为字符串类型
     * */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器
        converter.setObjectMapper(new JacksonObjectMapper());
        //将新建的转换器追加到mvc框架的转换器中
        converters.add(0, converter);
    }
}
