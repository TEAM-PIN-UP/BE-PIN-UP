package com.pinup.controller;


import com.pinup.global.response.ApiSuccessResponse;
import com.pinup.global.response.TokenResponse;
import com.pinup.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login/google")
    public void googleLogin(HttpServletResponse response) throws IOException {

        String authorizationUrl = authService.getGoogleAuthorizationUrl();
        response.sendRedirect(authorizationUrl);
    }

    @GetMapping("/login/google/callback")
    public ApiSuccessResponse<TokenResponse> googleCallback(@RequestParam("code") String code) {
        TokenResponse tokenResponse = authService.googleLogin(code);
        return ApiSuccessResponse.from(tokenResponse);
    }

    @PostMapping("/refresh")
    public ApiSuccessResponse<TokenResponse> refresh(@RequestHeader("Authorization") String refreshToken) {
        TokenResponse tokenResponse = authService.refresh(refreshToken);
        return ApiSuccessResponse.from(tokenResponse);
    }
    @PostMapping("/logout")
    public ApiSuccessResponse<?> logout(@RequestHeader("Authorization") String accessToken) {
        authService.logout(accessToken);
        return ApiSuccessResponse.NO_DATA_RESPONSE;
    }
}