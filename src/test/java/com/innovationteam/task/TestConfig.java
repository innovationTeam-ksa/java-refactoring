package com.innovationteam.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static com.innovationteam.task.configs.SecurityConfig.EXPOSED_HEADERS;

@Configuration
public class TestConfig {

    @Bean
    public WebTestClient webTestClient() {
        return WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .build();
    }

    @Bean
    @Primary
    public SecurityWebFilterChain securityWebFilterTestChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(c -> {
                    c.anyExchange().permitAll();
                })
                .cors(c -> c.configurationSource(req -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedOriginPatterns(List.of("*"));
                    configuration.setAllowedMethods(List.of("*"));
                    configuration.setAllowedHeaders(List.of("*"));
                    configuration.setExposedHeaders(EXPOSED_HEADERS);
                    return configuration;
                }))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(c -> {
                    c.authenticationEntryPoint(new HttpBasicServerAuthenticationEntryPoint());
                })
                .exceptionHandling(c -> c.authenticationEntryPoint(new HttpBasicServerAuthenticationEntryPoint()))
                .build();
    }

}
