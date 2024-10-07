package com.pinup.service;

import com.pinup.dto.response.MemberResponse;
import com.pinup.entity.Member;
import com.pinup.global.exception.PinUpException;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponse searchUsers(String query) {
        Member member = memberRepository.findByNickname(query)
                .orElseThrow(() -> PinUpException.MEMBER_NOT_FOUND);

        return MemberResponse.from(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse getCurrentMember(){
        String currentMemberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member currentMember = memberRepository.findByEmail(currentMemberEmail)
                .orElseThrow(() -> PinUpException.MEMBER_NOT_FOUND);

        return MemberResponse.from(currentMember);
    }
}
