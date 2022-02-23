//package com.wangjf.server.config.swagger;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.RequestHandler;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//
///**
// * @description:
// * @author: Joker
// * @time: 2022/1/8 10:59
// */
//@Configuration
//@EnableSwagger2  //开启Swagger2
//public class TestSwagger2Config {
//
//    //配置了Swagger2的Docket的bean实例
//    @Bean
//    public Docket docket() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .groupName("Joker的文档")//自定义分组
//                .enable(true)
//                .select()
//                //扫描指定包下的类
//                .apis(RequestHandlerSelectors.basePackage("com.wangjf.controller"))
//                //过滤什么路径
//                //.paths(PathSelectors.ant(""))
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        //作者信息
//        Contact contact = new Contact("Joker", "", "2431975848@qq.com");
//        return new ApiInfo(
//                "Joker的Api文档",
//                "忠诚不绝对就是绝对不忠诚",
//                "v1.0", "urn:tos",
//                contact, "Apache 2.0",
//                "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList());
//    }
//
//    @Bean
//    public Docket docket2() {
//        return new Docket(DocumentationType.SWAGGER_2).groupName("A");
//
//    }
//
//    @Bean
//    public Docket docket3() {
//        return new Docket(DocumentationType.SWAGGER_2).groupName("B");
//
//    }
//}
