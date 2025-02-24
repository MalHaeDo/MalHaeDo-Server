package com.backend.malhaedo.domain.member.service;

import com.backend.malhaedo.domain.member.dto.MemberResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    @Override
    public MemberResponseDTO.LoginSuccessDTO joinMember() {
        return null;
    }
}
