package com.pinup.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PlaceReviewInfoDto {

    private int reviewCount; // 리뷰 수
    private double averageReviewRating; // 리뷰 평균 별점 (나 + 친구들)
    private List<ReviewInfoForUserDto> reviewDataForMember;

    @Getter
    @Builder
    public static class ReviewInfoForUserDto {
        private String nickname; // 리뷰 작성자 닉네임
        private String content; // 리뷰 내용
        private Double rating; // 유저가 부여한 리뷰 별점
        private String registerDate; // 리뷰 등록일자
        private String profileImageUrl; // 리뷰 등록 유저 프로필 사진
        private List<String> reviewImageUrls; // 리뷰 사진 (최대 3장)
    }
}
