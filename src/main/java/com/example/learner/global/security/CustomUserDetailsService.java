package com.example.learner.global.security;

import com.example.learner.domain.member.dao.MemberRepository;
import com.example.learner.domain.member.entity.Member;
import com.example.learner.domain.member.entity.MemberRole;
import com.example.learner.domain.member.entity.SocialMember;
import com.example.learner.domain.member.entity.SocialType;
import com.example.learner.global.common.code.ErrorCode;
import com.example.learner.global.exception.BaseExceptionHandler;
import com.example.learner.global.security.user.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 저장된 정보가 없을 경우 회원 가입 처리
        var member = memberRepository.findById(UUID.fromString(username)).orElseThrow(
                () -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER)
        );

        return UserSecurityDTO.builder()
                .username(member.getId().toString())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .authorities(mapPrivilegeToAuthorities(member))
                .build();
    }

    @Transactional
    public Member registerMember(SocialType socialType, String email) {
        // email을 이용해서 기존 회원인지 파악
        var member = memberRepository.findBySocialTypeAndEmail(socialType, email);
        // 기 소셜 유저의 이메일 추가
        if (member.isPresent()) {
            SocialMember socialMember = SocialMember.builder()
                    .socialType(socialType)
                    .email(email)
                    .build();
            socialMember.setMember(member.get());
            return memberRepository.save(member.get());
        }

        // 소셜 정보
        SocialMember socialMember = SocialMember.builder()
                .socialType(socialType)
                .email(email)
                .build();

        Member newMember = Member.builder()
                .build();
        newMember.getRole().add(MemberRole.USER); // 최초 권한 설정
        newMember.getSocialMembers().add(socialMember);
        return memberRepository.save(newMember);
    }

    private Collection<? extends GrantedAuthority> mapPrivilegeToAuthorities(Member member) {
        return AuthorityUtils.createAuthorityList(member.getRole().stream().map(Enum::name).toList());
    }
}
