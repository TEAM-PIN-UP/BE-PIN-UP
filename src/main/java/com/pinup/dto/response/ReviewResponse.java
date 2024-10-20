package com.pinup.dto.response;

import com.pinup.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
public class ReviewResponse {

    private Long reviewId;
    private Long writerId;
    private Long placeId;
    private String reviewComment;
    private double reviewRating;
    private List<String> reviewImageUrls;
    private List<String> reviewKeywords;
    private String createdAt;
    private String updatedAt;

    public static ReviewResponse of(Review review,
                                    List<String> reviewImageUrls,
                                    List<String> reviewKeywords) {

        return ReviewResponse.builder()
                .reviewId(review.getId())
                .writerId(review.getMember().getId())
                .placeId(review.getPlace().getId())
                .reviewComment(review.getComment())
                .reviewRating(review.getRating())
                .reviewImageUrls(reviewImageUrls)
                .reviewKeywords(reviewKeywords)
                .createdAt(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .updatedAt(review.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
