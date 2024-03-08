package com.example.learner.global.security.filter;

import com.example.learner.global.security.exception.AccessTokenException;
import com.example.learner.global.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticateFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.trace("Request URI: {}", request.getRequestURI());
        log.trace("Request Method: {}", request.getMethod());
        log.trace("Request Params: {}", request.getParameterMap());
        log.trace("Access-token: {}", request.getHeader("Authorization"));

        // WHITE LIST의 패스에 대한 JWT 토큰 검증 패스
        // if (PatternMatchUtils.simpleMatch(URL_WHITE_LIST, request.getRequestURI())) {
        //     filterChain.doFilter(request, response);
        //     return;
        // }

        try {
            Authentication authentication = jwtService.authenticateAccessToken(request);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (AccessTokenException accessTokenException) {
            accessTokenException.addResponseError(response);
        }
    }
}