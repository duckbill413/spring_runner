package com.example.learner.global.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final OAuth2RequestProcessorFactory oAuth2RequestProcessorFactory;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2RequestProcessor oAuth2Parser = oAuth2RequestProcessorFactory.createOAuth2Processor(oAuth2UserRequest);

        return new DefaultOAuth2User(
                AuthorityUtils.NO_AUTHORITIES,
                oAuth2Parser.makeUserAttributes(),
                oAuth2Parser.loadUserNameAttributeName()
        );
    }

}