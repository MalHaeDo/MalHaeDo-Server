package com.backend.malhaedo.domain.recommend.entity;

import com.backend.malhaedo.domain.reply.entity.Reply;
import com.backend.malhaedo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Song extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songId;

    private String title;

    private String singer;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String reason;

    @OneToOne
    @JoinColumn(name = "reply_id")
    private Reply reply;
}
