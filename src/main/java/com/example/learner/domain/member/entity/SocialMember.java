package com.example.learner.domain.member.entity;

import com.example.learner.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SocialMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_member_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    private String email;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    public void setMember(Member member) {
        this.member = member;
        member.getSocialMembers().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocialMember that = (SocialMember) o;
        return socialType == that.socialType && Objects.equals(email, that.email) && Objects.equals(member, that.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socialType, email, member);
    }
}
