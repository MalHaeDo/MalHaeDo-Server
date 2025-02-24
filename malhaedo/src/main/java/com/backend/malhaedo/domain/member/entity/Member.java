package com.backend.malhaedo.domain.member.entity;

import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String nickName;

    private String islandName;

    private Boolean isGuest; // 게스트면 true, 회원이면 false

    @Column(name = "provider", nullable = true, length = 10)
    private String provider;

    @Column(name = "provider_id", nullable = true, length = 60)
    private String providerId;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Letter> letterList = new ArrayList<>();
}
