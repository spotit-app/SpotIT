package com.spotit.backend.config.security;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${okta.oauth2.issuer}")
    private String jwtIssuerUri;

    private static final String[] API_PUBLIC_ENDPOINTS = {
            "/api/educationLevel",
            "/api/techSkillName",
            "/api/softSkillName",
            "/api/foreignLanguageName",
            "/api/workMode",
            "/api/workExperience",
            "/api/portfolio/*",
            "/api/portfolios",
            "/api/jobOffer",
            "/api/company/*",
            "/api/jobOffer/*",
            "/api/company/*/jobOffer"
    };

    private static final String[] API_ADMIN_ENDPOINTS = {
            "/api/techSkillName",
            "/api/techSkillName/*",
            "/api/softSkillName",
            "/api/softSkillName/*",
            "/api/foreignLanguageName",
            "/api/foreignLanguageName/*",
            "/api/educationLevel",
            "/api/educationLevel/*"
    };

    @Bean
    JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(jwtIssuerUri);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(POST, API_ADMIN_ENDPOINTS).hasRole("ADMIN")
                        .requestMatchers(PUT, API_ADMIN_ENDPOINTS).hasRole("ADMIN")
                        .requestMatchers(DELETE, API_ADMIN_ENDPOINTS).hasRole("ADMIN")
                        .requestMatchers(GET, API_PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers("/api").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll())
                .cors(withDefaults())
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(
                                jwt -> jwt.jwtAuthenticationConverter(
                                        permissionsConverter())))
                .build();
    }

    @Bean
    JwtAuthenticationConverter permissionsConverter() {
        JwtGrantedAuthoritiesConverter jwtAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        jwtAuthoritiesConverter.setAuthoritiesClaimName("spotit/roles");
        jwtAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthConverter = new JwtAuthenticationConverter();

        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(jwtAuthoritiesConverter);

        return jwtAuthConverter;
    }
}
