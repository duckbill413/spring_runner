package com.example.learner.domain.member.entity;

import com.example.learner.domain.BaseEntity;
import com.example.learner.domain.order.entity.OrderDetail;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.*;

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
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "member_id")
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(unique = true)
    private String phone;
    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SocialMember> socialMembers = new HashSet<>();
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "home_city")),
            @AttributeOverride(name = "district", column = @Column(name = "home_district")),
            @AttributeOverride(name = "detail", column = @Column(name = "home_address_detail")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "home_zip_code"))
    })
    @Embedded
    private Address address;
    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_role",
            joinColumns = @JoinColumn(name = "member_id"))
    private Set<MemberRole> role = new HashSet<>();
    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<OrderDetail> orderDetails = new ArrayList<>();
}


