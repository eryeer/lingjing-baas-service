package com.onchain.dna2explorer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Timestamp;
import java.util.Date;

@Configuration
@EnableSwagger2
@Profile({"dev", "uat", "local", "pre", "info"})
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .directModelSubstitute(Date.class, Long.class)//将Date类型全部转为Long类型
                .directModelSubstitute(Timestamp.class, Long.class)//将Timestamp类型全部转为Long类型
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.onchain"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        //api基本信息，展示在页面
        return new ApiInfoBuilder()
                .title("Onchain DNA2.0 Explorer APIs")
                .description("This is Onchain DNA2.0 Explorer")
                .version("0.1")
                .build();
    }
}