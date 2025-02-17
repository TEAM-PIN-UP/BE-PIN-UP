package com.pinup.domain.review.dto.response;


import com.pinup.global.common.Formatter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(title = "리뷰 상세 조회 응답 DTO")
@Data
public class ReviewDetailResponse {

    @Schema(description = "리뷰 고유 ID")
    private Long reviewId;

    @Schema(description = "리뷰 작성자명")
    private String writerName; // 작성자 이름(또는 닉네임)

    @Schema(description = "리뷰 작성자의 총 리뷰 수")
    private int writerTotalReviewCount; // 작성자의 총 리뷰 수

    @Schema(description = "리뷰 작성자가 해당 가게에 부여한 별점")
    private double starRating; // 해당 가게에 부여한 별점

    @Schema(description = "장소 방문 날짜")
    private String visitedDate; // 방문날짜

    @Schema(description = "리뷰 내용")
    private String content; // 리뷰내용

    @Schema(description = "리뷰 작성자의 프로필 사진 URL")
    private String writerProfileImageUrl; // 작성자 프로필 사진

    @Schema(description = "리뷰 작성자가 등록한 리뷰 이미지 URL 리스트")
    private List<String> reviewImageUrls; // 리뷰 이미지 목록

    public ReviewDetailResponse(Long reviewId, String writerName, int writerTotalReviewCount,
                                double starRating, String visitedDate, String content, String writerProfileImageUrl) {
        this.reviewId = reviewId;
        this.writerName = writerName;
        this.writerTotalReviewCount = writerTotalReviewCount;
        this.starRating = Formatter.formatStarRating(starRating);
        this.visitedDate = visitedDate;
        this.content = content;
        this.writerProfileImageUrl = writerProfileImageUrl;
    }
}
