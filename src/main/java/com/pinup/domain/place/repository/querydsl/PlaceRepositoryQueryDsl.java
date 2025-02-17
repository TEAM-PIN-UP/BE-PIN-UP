package com.pinup.domain.place.repository.querydsl;

import com.pinup.domain.place.dto.response.MapPlaceResponse;
import com.pinup.domain.place.entity.PlaceCategory;
import com.pinup.domain.place.entity.SortType;
import com.pinup.domain.review.dto.response.ReviewDetailResponse;

import java.util.List;

public interface PlaceRepositoryQueryDsl {

    List<MapPlaceResponse> findMapPlaces(
            Long memberId, String query, PlaceCategory category, SortType sortType,
            double swLatitude, double swLongitude, double neLatitude, double neLongitude,
            double currentLatitude, double currentLongitude
    );
    MapPlaceResponse findMapPlaceDetail(
            Long memberId, String kakaoPlaceId, double currentLatitude, double currentLongitude
    );
    List<ReviewDetailResponse> findAllTargetMemberReviews(Long memberId, String kakaoPlaceId);
    Long getReviewCount(Long memberId, String kakaoPlaceId);
    Double getAverageStarRating(Long memberId, String kakaoPlaceId);
}
