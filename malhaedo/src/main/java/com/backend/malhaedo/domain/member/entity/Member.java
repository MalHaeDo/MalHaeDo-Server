package com.backend.malhaedo.domain.member.entity;

import com.backend.malhaedo.domain.letter.entity.Letter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String islandName;

    private Boolean isGuest; // 게스트면 true, 회원이면 false

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Letter> letterList = new ArrayList<>();
}
