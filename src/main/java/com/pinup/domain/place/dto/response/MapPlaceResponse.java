package com.pinup.domain.place.dto.response;

import com.pinup.domain.place.entity.PlaceCategory;
import com.pinup.global.common.Formatter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(title = "지도 내 장소 목록 조회 응답 DTO", description = "친구 리뷰가 있는 장소만 조회")
public class MapPlaceResponse {

    @Schema(description = "카카오맵 장소 고유 ID")
    private String kakaoPlaceId;

    @Schema(description = "장소명")
    private String name;

    @Schema(description = "평균 별점")
    private double averageStarRating;

    @Schema(description = "리뷰 수")
    private long reviewCount;

    @Schema(description = "현재 위치에서 해당 장소까지 떨어진 거리(단위: km)")
    private String distance;

    @Schema(description = "장소의 위도")
    private double latitude;

    @Schema(description = "장소의 경도")
    private double longitude;

    @Schema(description = "북마크 여부")
    private boolean isBookmark;

    @Schema(description = "장소 카테고리")
    private PlaceCategory placeCategory;

    @Schema(description = "리뷰 이미지 URL 리스트 (가장 먼저 등록된 리뷰 이미지 순서로 최대 3장)")
    private List<String> reviewImageUrls;

    @Schema(description = "리뷰 작성자 프로필 이미지 URL 리스트 (가장 최근에 리뷰 작성한 유저 순서로 최대 3장)")
    private List<String> reviewerProfileImageUrls;

    public MapPlaceResponse(
            String kakaoPlaceId, String name, double averageStarRating,
            Long reviewCount, double distance, double latitude,
            double longitude, PlaceCategory placeCategory
    ) {
        this.kakaoPlaceId = kakaoPlaceId;
        this.name = name;
        this.averageStarRating = Formatter.formatStarRating(averageStarRating);
        this.reviewCount = reviewCount;
        this.distance = Formatter.formatDistance(distance);
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeCategory = placeCategory;
    }
}