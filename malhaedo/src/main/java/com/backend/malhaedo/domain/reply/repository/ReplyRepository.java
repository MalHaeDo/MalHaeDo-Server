package com.backend.malhaedo.domain.reply.repository;

import com.backend.malhaedo.domain.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
