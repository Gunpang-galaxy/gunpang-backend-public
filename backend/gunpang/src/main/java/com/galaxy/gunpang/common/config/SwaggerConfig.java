package com.galaxy.gunpang.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi group(){
        return GroupedOpenApi.builder()
                .group("main")
                .packagesToScan("com.galaxy.gunpang")
                .build();
    }

    @Bean
    public OpenAPI springOpenApi(){
        return new OpenAPI().components(new Components()).info(new Info()
                .title("Gunpang Spring Boot REST API")
                .version("1.0.0")
                .description("Gunpang의 swagger api 입니다."));
    }

}
