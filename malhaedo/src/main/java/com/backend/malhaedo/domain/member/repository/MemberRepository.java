package com.backend.malhaedo.domain.member.repository;

import com.backend.malhaedo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByProviderAndProviderId(String provider, String providerId);
    boolean existsByNickName(String nickName);
    boolean existsByIslandName(String islandName);
}
