package com.backend.malhaedo.domain.reply.repository;

import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findAllByLetter_Member_MemberId(Long memberId);
    Optional<Reply> findByLetter(Letter letter);
}
