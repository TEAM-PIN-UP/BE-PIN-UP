package com.pinup.global.oauth2.handler;

import com.pinup.global.jwt.JwtTokenProvider;
import com.pinup.global.oauth2.CustomOAuth2User;
import com.pinup.repository.MemberRepository;
import com.pinup.service.RedisService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final String PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RedisService redisService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        try {
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            loginSuccess(response, customOAuth2User);

        } catch (Exception e) {
            log.error("소셜 로그인 실패: {}", e.getMessage());
        }

    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtTokenProvider.createToken(oAuth2User.getEmail(), oAuth2User.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(oAuth2User.getEmail());
        response.addHeader(jwtTokenProvider.getAccessHeader(), PREFIX + accessToken);
        response.addHeader(jwtTokenProvider.getRefreshHeader(), PREFIX + refreshToken);

        jwtTokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        redisService.setValues(oAuth2User.getEmail(), refreshToken);
    }
}
