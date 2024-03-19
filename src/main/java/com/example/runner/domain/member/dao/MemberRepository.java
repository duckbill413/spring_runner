package com.example.runner.domain.member.dao;

import com.example.runner.domain.member.entity.Member;
import com.example.runner.domain.member.entity.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    @Query("""
            SELECT m
            FROM Member m
            JOIN m.socialMembers sm
            WHERE sm.socialType = :socialType AND sm.socialId = :socialId
            """)
    Optional<Member> findBySocialTypeAndSocialId(@Param("socialType") SocialType socialType, @Param("socialId") String socialId);

    Optional<Member> findByPhone(String phone);
}
