package com.spotit.backend.config.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.spotit.backend.abstraction.UnauthorizedUserException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserAuth0IdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        Optional<String> auth0IdFromRequest = getAuth0IdFromUri(request.getRequestURI());

        if (auth0IdFromRequest.isEmpty()) {
            return true;
        }

        if (areAuth0IdsMatching(auth0IdFromRequest.get())) {
            return true;
        }

        throw new UnauthorizedUserException();
    }

    private static boolean areAuth0IdsMatching(String auth0IdFromRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null
                && authentication.getPrincipal() instanceof Jwt token
                && auth0IdFromRequest.equals(token.getSubject());
    }

    private Optional<String> getAuth0IdFromUri(String requestUri) {
        String[] uriParts = requestUri.split("/");

        return uriParts.length >= 4 ? Optional.of(formatAuth0Id(uriParts[3])) : Optional.empty();
    }

    private String formatAuth0Id(String auth0Id) {
        return auth0Id.replace("%7C", "|");
    }
}
