package com.example.learner.global.security;

import com.example.learner.domain.member.dao.MemberRepository;
import com.example.learner.global.common.code.ErrorCode;
import com.example.learner.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var member = memberRepository.findById(UUID.fromString(username)).orElseThrow(
                () -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER)
        );


        return null;
    }

}
