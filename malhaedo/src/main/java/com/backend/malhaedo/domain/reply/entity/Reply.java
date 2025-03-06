package com.backend.malhaedo.domain.reply.entity;

import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.global.common.BaseEntity;
import com.backend.malhaedo.global.common.enums.Resident;
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

    @OneToOne
    @JoinColumn(name = "letter_id")
    private Letter letter;
}
