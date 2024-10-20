package com.pinup.global.jwt;

import com.pinup.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;


import static com.pinup.constants.TestConstants.TEST_EMAIL;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtTokenProviderTests {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("토큰 생성 시 유효한 JWT를 반환해야 함")
    void testCreateToken() {
        String token = jwtTokenProvider.createToken(TEST_EMAIL, Role.ROLE_USER);

        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(TEST_EMAIL, jwtTokenProvider.getEmail(token));
    }

    @Test
    @DisplayName("유효한 토큰 검증 시 true를 반환해야 함")
    void testValidateToken() {
        String token = jwtTokenProvider.createToken(TEST_EMAIL, Role.ROLE_USER);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    @DisplayName("토큰으로부터 유효한 인증 정보를 얻어야 함")
    void testGetAuthentication() {
        String token = jwtTokenProvider.createToken(TEST_EMAIL, Role.ROLE_USER);

        Authentication auth = jwtTokenProvider.getAuthentication(token);
        assertEquals(TEST_EMAIL, auth.getName());
        assertTrue(auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ROLE_USER.toString())));
    }
}