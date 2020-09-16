package com.gomson.tryangle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan(basePackages = {"com.gomson.tryangle"})
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final int MAX_SIZE = 10 * 1024 * 1024;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/META-INF/resources", "classpath:/images/");
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(MAX_SIZE); // 10MB
        multipartResolver.setMaxUploadSizePerFile(MAX_SIZE); // 10MB
        multipartResolver.setMaxInMemorySize(0);
        return multipartResolver;
    }

    @Bean
    public String mlServerUrl() {
        return "localhost:8000/";
    }
}
