package com.example.runner.global.security.service;

import com.example.runner.domain.member.dao.MemberRepository;
import com.example.runner.domain.member.entity.Member;
import com.example.runner.domain.member.entity.MemberRole;
import com.example.runner.domain.member.entity.SocialMember;
import com.example.runner.domain.member.entity.SocialType;
import com.example.runner.global.common.code.ErrorCode;
import com.example.runner.global.exception.BaseExceptionHandler;
import com.example.runner.global.security.user.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final OAuth2RequestProcessorFactory oAuth2RequestProcessorFactory;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public UserSecurityDTO loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2RequestProcessor oAuth2Parser = oAuth2RequestProcessorFactory.createOAuth2Processor(oAuth2UserRequest);

        Map<String, Object> stringObjectMap = oAuth2Parser.getSocialUserAttributes();
        Object platformObj = stringObjectMap.get("platform");
        Object socialIdObj = stringObjectMap.get(oAuth2Parser.loadUserNameAttributeName());
        Object emailObj = stringObjectMap.get("email");
        Object phoneObj = stringObjectMap.get("phone");

        // 소셜 회원정보가 있는지 확인
        if (Objects.isNull(socialIdObj) || Objects.isNull(platformObj) || Objects.isNull(emailObj)) {
            throw new BaseExceptionHandler(ErrorCode.NO_SOCIAL_USER_ATTRIBUTES);
        }
        String platform = (String) platformObj;
        String socialId = (String) socialIdObj;
        String email = (String) emailObj; // 추가 정보

        SocialType socialType = SocialType.fromString(platform);

        // 소셜 플랫폼과 이메일로 회원 확인
        Optional<Member> member = memberRepository.findBySocialTypeAndSocialId(socialType, socialId);
        // 존재한다면 로그인 처리
        if (member.isPresent()) {
            return getUserSecurityDTO(member.get(), stringObjectMap);
        }

        // 존재 하지 않으지만 휴대폰 번호는 존재하는 경우
        // 핸드폰 번호가 있다면 핸드폰 번호를 이용해서 기존 회원 확인
        if (Objects.nonNull(phoneObj)) {
            Optional<Member> phoneMember = memberRepository.findByPhone((String) phoneObj);

            // 핸드폰 정보에 해당하는 유저가 있다면 신규 소셜 로그인 플랫폼 등록
            if (phoneMember.isPresent()) {
                return updateSocialPlatform(phoneMember.get(), socialType, socialId, stringObjectMap);
            }
        }

        // 비회원인 경우 회원 가입
        return getUserSecurityDTO(registerMember(socialType, socialId, email), stringObjectMap);
    }

    private UserSecurityDTO getUserSecurityDTO(Member member, Map<String, Object> stringObjectMap) {
        return UserSecurityDTO.fromSocial()
                .username(member.getId().toString())
                .password(UUID.randomUUID().toString())
                .authorities(grantAuthorities(member))
                .props(stringObjectMap)
                .create();
    }

    // 신규 회원 등록
    public Member registerMember(SocialType socialType, String socialId, String email) {
        // 소셜 정보
        SocialMember socialMember = SocialMember.builder()
                .socialType(socialType)
                .socialId(socialId)
                .email(email)
                .build();

        Member member = Member.builder()
                .build();
        member.getRole().add(MemberRole.USER); // 최초 권한 설정
        socialMember.setMember(member);
        return memberRepository.save(member);
    }

    // 신규 소셜 플랫폼 등록
    public UserSecurityDTO updateSocialPlatform(Member member, SocialType socialType, String socialId, Map<String, Object> stringObjectMap) {
        SocialMember socialMember = SocialMember.builder()
                .socialType(socialType)
                .socialId(socialId)
                .build();
        socialMember.setMember(member);

        return getUserSecurityDTO(memberRepository.save(member), stringObjectMap);
    }

    private Collection<? extends GrantedAuthority> grantAuthorities(Member member) {
        return AuthorityUtils.createAuthorityList(member.getRole().stream().map(Enum::name).toList());
    }
}