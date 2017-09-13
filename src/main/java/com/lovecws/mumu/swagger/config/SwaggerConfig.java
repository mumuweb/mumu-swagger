/*
 * *
 *  <p>Title: ${file_name}</p>
 *  <p>Description: </p>
 *  <p>Copyright: Copyright (c) 2017</p>
 *  <p>Company: www.chuasi.com</p>
 *  @author babymm
 *  @date ${date}
 *  @version 1.0
 * /
 */

package com.lovecws.mumu.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@EnableSwagger2
@ComponentScan(basePackages = {"com.lovecws.mumu.swagger.controller"})
@Configuration
public class SwaggerConfig {

    /**
     * Create rest api docket.
     *
     * @return the docket
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //.groupName("用户管理")
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lovecws.mumu.swagger"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring 中使用Swagger2构建RESTful APIs")
                .description("更多Spring Boot相关文章请关注：http://www.babymm.com/")
                .termsOfServiceUrl("http://www.babymm.com/")
                .contact("baby慕慕")
                .version("1.0")
                .build();
    }
}