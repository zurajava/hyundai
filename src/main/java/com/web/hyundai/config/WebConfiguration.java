package com.web.hyundai.config;

import com.web.hyundai.model.Path;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.web.Swagger2Controller;

@Configuration
@EnableSwagger2
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println(Path.folderPath());
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///" + Path.folderPath() )
                .addResourceLocations("classpath:/static/");

        registry
                .addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }




// swagger


@Bean
public Docket swaggerApi() {
    return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            //.paths(PathSelectors.regex("\\/api\\/.*?"))
            .paths(PathSelectors.any())
            .build()
            //.host("api.myborbali.ge")
            .apiInfo(swaggerApiInfo())
            .useDefaultResponseMessages(false);
}

    @Bean
    ApiInfo swaggerApiInfo() {
        final ApiInfoBuilder builder = new ApiInfoBuilder();
        return builder.title("Hyundai api/admin").version("0.1").build();
    }

    @Bean
    UiConfiguration swaggerUiConfiguration() {
        return UiConfigurationBuilder.builder()
                .defaultModelRendering(ModelRendering.MODEL)
                .defaultModelsExpandDepth(1)
                .docExpansion(DocExpansion.LIST)
                .build();
    }

}
