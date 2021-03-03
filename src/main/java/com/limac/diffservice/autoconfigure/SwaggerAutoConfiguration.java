package com.limac.diffservice.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.util.Collections;

/**
 * Swagger Auto Configuration.
 */
@Configuration
@PropertySource("classpath:swagger.properties")
@EnableSwagger2
public class SwaggerAutoConfiguration {

    private static final String EMPTY_STRING = "";

    private final String basePackage;
    private final ApiInfo info;

    /**
     * Construct a SwaggerAutoConfiguration instance.
     *
     * @param title       read from property swagger.api.title, default to empty string
     * @param description read from property swagger.api.description, default to empty string
     * @param version     read from property swagger.api.version, required configuration
     * @param basePackage read from property swagger.api.basePackage, required configuration
     */
    public SwaggerAutoConfiguration(@Value("${swagger.api.title:}") String title,
                                    @Value("${swagger.api.description}") String description,
                                    @Value("${swagger.api.version}") String version,
                                    @Value("${swagger.api.basePackage}") String basePackage) {
        this.basePackage = basePackage;
        this.info = new ApiInfo(title, description, version, EMPTY_STRING, new Contact("limac", "https://github.com/lima-caio", "caio.sza@gmail.com"),
            EMPTY_STRING, EMPTY_STRING, Collections.emptyList());
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(info)
            .select()
            .apis(RequestHandlerSelectors.basePackage(basePackage))
            .paths(PathSelectors.any())
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .ignoredParameterTypes(ResponseEntity.class)
            .useDefaultResponseMessages(false);
    }
}
