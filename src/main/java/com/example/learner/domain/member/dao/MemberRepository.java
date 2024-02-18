package com.example.learner.domain.member.dao;

import com.example.learner.domain.member.entity.Member;
import com.example.learner.domain.member.entity.SocialType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    @Query("""
            SELECT m
            FROM Member m
            JOIN FETCH m.socialMembers sm
            ON (sm.socialType = :socialType AND sm.email= :email)
            """)
    Optional<Member> findBySocialTypeAndEmail(@Param("socialType")SocialType socialType, @Param("email") String email);
}
