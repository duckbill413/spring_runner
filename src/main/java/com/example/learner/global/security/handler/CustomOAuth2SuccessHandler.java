package com.example.learner.global.security.handler;

import com.example.learner.global.security.service.CustomUserDetailsService;
import com.example.learner.global.security.service.JwtService;
import com.example.learner.global.security.user.UserSecurityDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Value("${client.redirect-url.success}")
    private String REDIRECT_URI_SUCCESS;

    @Value("${client.redirect-url.anonymous}")
    private String REDIRECT_URI_ANONYMOUS;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");
        UserSecurityDTO userDetails = userDetailsService.loadUserByUsername(email);

        log.trace("6. 유저에게 Access Token과 Refresh Token을 발급합니다.");
        String accessToken = jwtService.createAccessToken(userDetails);
        String refreshToken = jwtService.createRefreshToken(userDetails);

        String redirectURI = UriComponentsBuilder.fromUriString(REDIRECT_URI_SUCCESS)
                .queryParam("access-token", accessToken)
                .queryParam("refresh-token", refreshToken)
                .toUriString();
        response.sendRedirect(redirectURI);
    }
}