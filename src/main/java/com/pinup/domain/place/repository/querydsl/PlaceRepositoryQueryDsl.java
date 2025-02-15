package com.pinup.domain.place.repository.querydsl;

import com.pinup.domain.place.dto.response.PlaceDetailResponse;
import com.pinup.domain.place.dto.response.PlaceResponseWithFriendReview;
import com.pinup.domain.member.entity.Member;
import com.pinup.domain.place.entity.Place;
import com.pinup.domain.place.entity.PlaceCategory;
import com.pinup.domain.place.entity.SortType;

import java.util.List;

public interface PlaceRepositoryQueryDsl {

    List<Place> findAllByMemberAndLocation(
            List<Long> allowedMemberIds,
            String query,
            PlaceCategory category,
            SortType sortType,
            double swLatitude,
            double swLongitude,
            double neLatitude,
            double neLongitude,
            double currentLatitude,
            double currentLongitude
    );

    PlaceDetailResponse findByKakaoPlaceIdAndMember(
            Member loginMember,
            String kakaoPlaceId,
            double currentLatitude,
            double currentLongitude
    );

    Long getReviewCount(Member loginMember, String kakaoPlaceId);
    Double getAverageStarRating(Member loginMember, String kakaoPlaceId);
}
