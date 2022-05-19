package com.xttc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 实现 WebMvcConfigurer 接口，扩展 MVC 功能
 */
@Configuration
public class MyMVCConfig implements WebMvcConfigurer {
    // 添加视图管理
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 请求 toLoginPage 映射路径或者 login.html 页面都会自动映射到 login.html 页面
        registry.addViewController("/toLoginPage").setViewName("login");
        registry.addViewController("/login.html").setViewName("login");
    }
    //使用@Bean在拦截器初始化之前让类加载
   @Autowired
   private MyInterceptor myInterceptor;
    @Autowired
    private MyInterceptor1 myInterceptor1;

    // 添加拦截器管理
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //管理员拦截器
        registry.addInterceptor(myInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login.html","/register","/test");
        //普通用户拦截器
        registry.addInterceptor(myInterceptor1)
                .addPathPatterns("/**")
                .excludePathPatterns("/login.html","/register","/test","/main");
    }
}