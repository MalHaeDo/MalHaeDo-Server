package com.backend.malhaedo.domain.reply.entity;

import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.recommend.entity.Song;
import com.backend.malhaedo.global.common.BaseEntity;
import com.backend.malhaedo.global.common.enums.Resident;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private String summary;

    @Enumerated(EnumType.STRING)
    private Resident sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "letter_id")
    private Letter letter;

    @OneToOne(mappedBy = "reply", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Song song;
}
