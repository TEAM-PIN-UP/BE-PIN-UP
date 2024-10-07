package com.pinup.controller;

import com.pinup.dto.response.MemberResponse;
import com.pinup.global.response.ApiSuccessResponse;
import com.pinup.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/search")
    public ResponseEntity<ApiSuccessResponse<MemberResponse>> searchMembers(@RequestParam("query") String query) {
        MemberResponse searchResult = memberService.searchUsers(query);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(searchResult));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiSuccessResponse<MemberResponse>> getCurrentMember() {
        MemberResponse currentMember = memberService.getCurrentMember();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(currentMember));
    }
}
