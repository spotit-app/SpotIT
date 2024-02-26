package com.spotit.backend.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${okta.oauth2.issuer}")
    private String jwtIssuerUri;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/",
            "/*",
            "/index.html",
            "/assets/*",
            "/favicon/*",
            "/actuator/*",
            "/api",
            "/api/techSkillName",
            "/api/softSkillName",
            "/api/foreignLanguageName",
            "/api/portfolio/*",
            "/api/portfolios",
            "/swagger-ui/*",
            "/v3/api-docs",
            "/v3/api-docs/*"
    };

    @Bean
    JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(jwtIssuerUri);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(GET, PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated())
                .cors(withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .build();
    }
}
