package com.pinup.controller;

import com.pinup.dto.request.PlaceRequest;
import com.pinup.dto.request.ReviewRequest;
import com.pinup.dto.response.ReviewResponse;
import com.pinup.global.response.ApiSuccessResponse;
import com.pinup.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/places")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 리뷰 등록 API
     * 내용, 평점, 사진 최대 3장, 키워드 최대 10개 등록 가능
     * 업체 첫 리뷰 등록 시, DB에 업체 사전 등록 필요
     */
    @PostMapping("/reviews")
    public ResponseEntity<ApiSuccessResponse<ReviewResponse>> register(
            @Valid ReviewRequest reviewRequest,
            @Valid PlaceRequest placeRequest,
            @RequestParam(value = "multipartFiles", required = false)
            List<MultipartFile> multipartFiles) {

        ReviewResponse result = reviewService.register(reviewRequest, placeRequest, multipartFiles);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiSuccessResponse.from(result));
    }

    /**
     * 리뷰 조회 API
     * 가게에 내 친구들이 쓴 리뷰와 내가 쓴 리뷰만 조회
     */
}
