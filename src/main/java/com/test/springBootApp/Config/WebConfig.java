package com.test.springBootApp.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements  WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("helloPage");
        registry.addViewController("/mywebapp").setViewName("helloPage");
        registry.addViewController("/homePage").setViewName("homePage");
        registry.addViewController("/goods").setViewName("goods");
        registry.addViewController("/categories").setViewName("categories");
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler(
                "/webjars/**",
                "/uploads/**",
                "/css/**",
                "/js/**"
        )
                .addResourceLocations(
                        "classpath:/META-INF/resources/webjars/",
                        "classpath:/"+uploadPath,
                        "classpath:/static/css/",
                        "classpath:/static/js/");
    }
}
