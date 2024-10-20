package com.pinup.global.config;

import com.pinup.global.jwt.JwtTokenFilter;
import com.pinup.global.jwt.JwtTokenProvider;
import com.pinup.global.oauth2.handler.OAuth2LoginFailureHandler;
import com.pinup.global.oauth2.handler.OAuth2LoginSuccessHandler;
import com.pinup.global.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    private static final String[] PERMITTED_API_URL = {
            "/api/auth",
            "/api/auth/**",
            "/chats",
            "/api/places",
            "/api/places/**"
    };

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                // 쿠키와 세션을 사용하는 MVC패턴에서 csrf 공격에 취약하나, REST API는 덜 취약하여 비활성화
                // csrf: 사용자가 자신의 의지와는 무관하게 공격자가 의도한 행위(데이터 수정, 삭제, 등록 등) 을 특정 웹사이트에 요청하게 하는 공격
                .csrf(AbstractHttpConfigurer::disable)

                // CORS 활성화(특정 도메인만 접속할 수 있도록 허용)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)

                // 기본 인증 로그인 사용 안하고 JWT 로그인 사용하므로 disable
                .httpBasic(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers("/").permitAll()
                                .requestMatchers(PERMITTED_API_URL).permitAll()
                                .anyRequest().authenticated()
                )

                // 세션을 사용하지 않는 설정 추가
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
//                .oauth2Login(oauth2 -> oauth2
//                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
//                                .userService(customOAuth2UserService))
//                        .successHandler(oAuth2LoginSuccessHandler)
//                        .failureHandler(oAuth2LoginFailureHandler)
//                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
        ;

        return httpSecurity.build();
    }
}
