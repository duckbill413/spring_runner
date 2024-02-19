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
    private final String[] URL_WHITE_LIST;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.trace("Request URI: {}", request.getRequestURI());
        log.trace("Request Method: {}", request.getMethod());
        log.trace("Request Params: {}", request.getParameterMap());
        log.trace("Access-token: {}", request.getHeader("Authorization"));

        // WHITE LIST의 패스에 대한 JWT 토큰 검증 패스
        if (PatternMatchUtils.simpleMatch(URL_WHITE_LIST, request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            log.trace("유저의 토큰을 검증합니다.");
            Authentication authentication = jwtService.authenticateAccessToken(request);

            log.trace("유저의 토큰이 검증되었습니다. 유저를 SecurityContextHolder에 저장합니다.");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (AccessTokenException accessTokenException) {
            accessTokenException.sendResponseError(response);
        }
    }
}