package com.backend.malhaedo.domain.letter.entity;

import com.backend.malhaedo.domain.member.entity.Member;
import com.backend.malhaedo.domain.reply.entity.Reply;
import com.backend.malhaedo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Letter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long letterId;

    private String content;

    private String summary;

    private Boolean isReplyAllowed;

    private int sentCount; // 보낸 편지 개수

    private int repliedCount; // 답장 개수

    @OneToMany(mappedBy = "letter", cascade = CascadeType.ALL)
    private List<Reply> replyList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
