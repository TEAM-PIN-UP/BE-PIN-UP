package com.pinup.global.jwt;

import com.pinup.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import static com.pinup.constants.TestConstants.TEST_EMAIL;
import static com.pinup.constants.TestConstants.TEST_INVALID_TOKEN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtTokenFilterTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("유효한 토큰으로 접근 시 성공해야 함")
    void testValidToken() throws Exception {
        String token = jwtTokenProvider.createToken(TEST_EMAIL, Role.ROLE_USER);

        mockMvc.perform(get("/")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 접근 시 실패해야 함")
    void testInvalidToken() throws Exception {
        mockMvc.perform(get("/")
                        .header("Authorization", "Bearer " + TEST_INVALID_TOKEN))
                .andExpect(status().isBadRequest());
    }
}