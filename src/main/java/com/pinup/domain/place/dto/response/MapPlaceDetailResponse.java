package com.pinup.domain.place.dto.response;

import com.pinup.domain.review.dto.response.ReviewDetailResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Schema(title = "장소 상세 조회 응답 DTO")
@Getter
@Builder
public class MapPlaceDetailResponse {

    @Schema(description = "장소 기본 정보")
    private MapPlaceResponse mapPlaceResponse;

    @Schema(description = "리뷰 통계 그래프")
    private Map<Integer, Integer> ratingGraph;

    @Schema(description = "리뷰 상세 정보 리스트")
    private List<ReviewDetailResponse> reviews;

    public static MapPlaceDetailResponse from(
            MapPlaceResponse mapPlaceResponse,
            Map<Integer, Integer> ratingGraph,
            List<ReviewDetailResponse> reviews
    ) {
        return MapPlaceDetailResponse.builder()
                .mapPlaceResponse(mapPlaceResponse)
                .ratingGraph(ratingGraph)
                .reviews(reviews)
                .build();
    }
}
