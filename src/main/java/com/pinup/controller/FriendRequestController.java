package com.pinup.controller;

import com.pinup.dto.request.SendFriendRequest;
import com.pinup.dto.response.FriendRequestResponse;
import com.pinup.global.response.ApiSuccessResponse;
import com.pinup.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend-requests")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @PostMapping("/send")
    public ResponseEntity<ApiSuccessResponse<FriendRequestResponse>> sendFriendRequest(
            @RequestBody SendFriendRequest sendFriendRequest) {
        FriendRequestResponse result = friendRequestService.sendFriendRequest(sendFriendRequest.getReceiverId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(result));
    }

    @PatchMapping("/{friendRequestId}/accept")
    public ResponseEntity<ApiSuccessResponse<FriendRequestResponse>> acceptFriendRequest(
            @PathVariable("friendRequestId") Long friendRequestId) {
        FriendRequestResponse result = friendRequestService.acceptFriendRequest(friendRequestId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(result));
    }

    @PatchMapping("/{friendRequestId}/reject")
    public ResponseEntity<ApiSuccessResponse<FriendRequestResponse>> rejectFriendRequest(
            @PathVariable("friendRequestId") Long friendRequestId) {
        FriendRequestResponse result = friendRequestService.rejectFriendRequest(friendRequestId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(result));
    }

    @GetMapping("/received")
    public ResponseEntity<ApiSuccessResponse<List<FriendRequestResponse>>> getReceivedFriendRequests() {
        List<FriendRequestResponse> results = friendRequestService.getReceivedFriendRequests();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(results));
    }
}
