package com.innovationteam.task.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final List<String> EXPOSED_HEADERS = Arrays.asList("Content-Type", "Authorization", "Content-Length", "Accept", "Origin", "Location");

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(c -> {
                    c.pathMatchers("/api/**").authenticated();
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

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User
                .withUsername("user")
                .password("{bcrypt}$2a$10$GJZ7TOr66noG/8l1D5eBPuY/yQByCgeK1JB37.0HYPBTZp18W6t8q") //= `password`
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

}
