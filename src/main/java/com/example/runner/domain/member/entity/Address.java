package com.example.runner.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {
    private String city; // 시
    private String district; // 구
    @Column(name = "address_detail")
    private String detail; // 상세주소
    private String zipcode; // 우편번호
}