package com.spotit.backend.abstraction;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

public interface GeneralUtils {

    static String getEntityNotFoundMessage(Object id) {
        return "Entity with ID '" + id + "' not found!";
    }

    static String getErrorCreatingEntityMessage() {
        return "Error when creating new entity!";
    }

    static Map<String, Integer> getDeletedEntityResponse(int id) {
        return Map.of("id", id);
    }

    static String getInvalidUserMessage() {
        return "User account lacks necessary data!";
    }

    static JwtRequestPostProcessor createMockJwt(String userAuth0Id) {
        return jwt().jwt(jwt -> jwt.claim("sub", userAuth0Id));
    }

    static JwtRequestPostProcessor createAdminMockJwt() {
        return jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
