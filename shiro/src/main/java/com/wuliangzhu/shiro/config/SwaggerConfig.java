package com.wuliangzhu.shiro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * 404 问题怎么解决？
 * 答案：因为shiro对swagger的url进行了拦截，所以要在shiro配置中过滤掉
 * 如果有 post get put delete 这些请求怎么办？
 * 答案：requet请求指定 post 还是 get
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig /*implements WebMvcConfigurer*/ {

    @Bean
    public Docket statApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Shiro系统接口文档")
                .apiInfo(statApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wuliangzhu.shiro.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo statApiInfo() {
        return new ApiInfoBuilder().title("Shiro系统").description("Shiro管理API服务").termsOfServiceUrl("no terms of service")
                .version("1.0")
                .build();
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
//        // 解决 SWAGGER 404报错
//        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
//
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//
//    }
//
//    @Bean
//    public Docket businessApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("子系统接口文档")
//                .apiInfo(businessApiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.netx.api.controller.shoppingmall"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo businessApiInfo() {
//        return new ApiInfoBuilder().title("子系统").description("子系统API服务").termsOfServiceUrl("no terms of service")
//                .version("1.0")
//                .build();
//    }



}
