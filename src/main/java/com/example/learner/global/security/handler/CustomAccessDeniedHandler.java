package com.example.learner.global.security.handler;

import com.example.learner.global.security.service.CustomUserDetailsService;
import com.example.learner.global.security.service.JwtService;
import com.example.learner.global.security.user.UserSecurityDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof UserSecurityDTO userSecurityDTO) {
            UserSecurityDTO user = userDetailsService.loadUserByUsername(userSecurityDTO.getId().toString());

            if (Objects.nonNull(user)) {
                Collection<GrantedAuthority> authorities = user.getAuthorities();
                for (GrantedAuthority authority : authorities) {
                    if (request.isUserInRole(authority.getAuthority())) {
                        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));

                        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

                        response.setHeader("Authorization", jwtService.createAccessToken(user));
                        response.setHeader("refreshToken", jwtService.createRefreshToken(user));
                        response.setHeader("roles", String.join(" ", roles));
                        return;
                    }
                }
            }
        }

        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
