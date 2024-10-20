package com.pinup.dto.request;

import com.pinup.entity.Review;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class ReviewRequest {

    @NotBlank(message = "리뷰 후기는 필수 입력값입니다.")
    @Size(min = 5, max = 400, message = "리뷰 글자 수 범위를 벗어납니다.")
    private String comment;

    @NotNull(message = "평점은 필수 입력값입니다,")
    @Min(value = 1, message = "최소 평점은 1입니다.")
    @Max(value = 5, message = "최대 평점은 5입니다.")
    private Double rating;

    @Size(max = 10, message = "등록 가능한 키워드 갯수를 초과했습니다. - 최대 10개")
    private List<String> keywords;

    public Review toEntity() {
        return new Review(comment, rating);
    }
}
