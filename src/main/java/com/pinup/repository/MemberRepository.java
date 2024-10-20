package com.pinup.repository;

import com.pinup.entity.Member;
import com.pinup.enums.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByLoginTypeAndSocialId(LoginType loginType, String socialId);
}