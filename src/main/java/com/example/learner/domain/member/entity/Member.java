package com.example.learner.domain.member.entity;

import com.example.learner.domain.BaseEntity;
import com.example.learner.domain.order.entity.OrderDetail;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "member_id")
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String phoneNumber;
    private String email;
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_role",
            joinColumns = @JoinColumn(name = "member_id"))
    private Set<MemberRole> role;
    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();
}


