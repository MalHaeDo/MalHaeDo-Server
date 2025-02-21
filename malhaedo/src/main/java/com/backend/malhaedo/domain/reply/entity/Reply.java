package com.backend.malhaedo.domain.reply.entity;

import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.recommend.entity.Song;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    private String content;

    private String summary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "letter_id")
    private Letter letter;

    @OneToOne(mappedBy = "reply", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Song song;
}
