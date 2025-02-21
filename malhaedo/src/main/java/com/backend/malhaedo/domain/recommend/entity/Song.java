package com.backend.malhaedo.domain.recommend.entity;

import com.backend.malhaedo.domain.reply.entity.Reply;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Song {

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
