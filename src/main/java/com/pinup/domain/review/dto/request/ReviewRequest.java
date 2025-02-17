package com.pinup.domain.review.dto.request;

import com.pinup.domain.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(title = "리뷰 등록 DTO", description = "리뷰 데이터 등록 (내용, 별점, 방문 날짜)")
public class ReviewRequest {

    @NotBlank(message = "리뷰 내용을 입력하세요")
    @Size(min = 5, max = 400, message = "리뷰 글자 수는 최소 5자, 최대 400자입니다.")
    @Schema(description = "리뷰 내용", example = "음식이 맛있어요!")
    private String content;

    @NotNull(message = "별점을 입력하세요")
    @Schema(description = "별점(최소 0.5 ~ 최대 5.0 / 0.5점 단위)", example = "3.5")
    private double starRating;

    @NotBlank(message = "방문 날짜를 입력해주세요.")
    @Schema(description = "방문 날짜", example = "20241208")
    private String visitedDate;

    public Review toEntity() {
        return Review.builder()
                .content(content)
                .starRating(starRating)
                .visitedDate(visitedDate)
                .build();
    }
}
