package com.backend.malhaedo.domain.letter.entity;

import com.backend.malhaedo.domain.member.entity.Member;
import com.backend.malhaedo.domain.recommend.entity.Song;
import com.backend.malhaedo.domain.reply.entity.Reply;
import com.backend.malhaedo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
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

    @OneToOne(mappedBy = "letter", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Reply reply;

    @OneToOne(mappedBy = "letter", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Song song;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void increaseSentCount() {
        this.sentCount += 1;
    }
    public void increaseRepliedCount() {
        this.repliedCount += 1;
    }

    public void decreaseRepliedCount() {
        this.repliedCount -= 1;
    }
}
