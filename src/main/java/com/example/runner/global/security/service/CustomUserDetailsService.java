package com.example.runner.global.security.service;

import com.example.runner.domain.member.dao.MemberRepository;
import com.example.runner.domain.member.entity.Member;
import com.example.runner.domain.member.entity.MemberRole;
import com.example.runner.global.common.code.ErrorCode;
import com.example.runner.global.exception.BaseExceptionHandler;
import com.example.runner.global.security.user.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public UserSecurityDTO loadUserByUsername(String id) throws UsernameNotFoundException {
        // 저장된 정보가 없을 경우 회원 가입 처리
        var member = memberRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER)
        );

        return UserSecurityDTO.fromSocial()
                .username(member.getId().toString())
                .password(UUID.randomUUID().toString())
                .authorities(getMemberAuthorities(member))
                .create();
    }

    private static Collection<? extends GrantedAuthority> getMemberAuthorities(Member member) {
        return AuthorityUtils.createAuthorityList(member.getRole().stream().map(Enum::name).toList());
    }
}
