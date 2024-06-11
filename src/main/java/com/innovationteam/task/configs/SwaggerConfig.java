package com.innovationteam.task.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .pathsToMatch("/**")
                .group("Movies Rental info slip")
                .build();
    }

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Basic",
                                new SecurityScheme()
                                        .name("Basic")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                        ))
                .info(new Info().title("Movies Rental info slip")
                        .description("The code creates an information slip about movie rentals.")
                        .version("1.0"));
    }
}
