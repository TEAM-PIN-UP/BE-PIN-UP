package com.pinup.domain.member.repository;

import com.pinup.domain.member.entity.Member;
import com.pinup.domain.member.entity.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    List<Member> findByNicknameContaining(String nickname);
}