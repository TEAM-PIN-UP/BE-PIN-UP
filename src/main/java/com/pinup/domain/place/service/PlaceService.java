package com.pinup.domain.place.service;

import com.pinup.domain.member.entity.Member;
import com.pinup.domain.place.dto.response.MapPlaceDetailResponse;
import com.pinup.domain.place.dto.response.MapPlaceResponse;
import com.pinup.domain.place.dto.response.EntirePlaceResponse;
import com.pinup.domain.place.entity.PlaceCategory;
import com.pinup.domain.place.entity.SortType;
import com.pinup.domain.place.repository.PlaceRepository;
import com.pinup.domain.review.dto.response.ReviewDetailResponse;
import com.pinup.global.common.AuthUtil;
import com.pinup.global.config.kakao.KakaoMapModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final AuthUtil authUtil;
    private final KakaoMapModule kakaoMapModule;
    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public List<MapPlaceResponse> getMapPlaces(
            String query, String category, String sort,
            double swLat, double swLon, double neLat,
            double neLon, double currLat, double currLon
    ) {
        Member loginMember = authUtil.getLoginMember();
        PlaceCategory placeCategory = PlaceCategory.getCategory(category);
        SortType sortType = SortType.getSortType(sort);

        return placeRepository.findMapPlaces(
                loginMember.getId(), query, placeCategory, sortType,
                swLat, swLon, neLat,
                neLon, currLat, currLon
        );
    }

    @Transactional(readOnly = true)
    public MapPlaceDetailResponse getMapPlaceDetail(String kakaoPlaceId, double currentLatitude, double currentLongitude) {
        Member loginMember = authUtil.getLoginMember();
        MapPlaceResponse mapPlaceResponse = placeRepository.findMapPlaceDetail(
                loginMember.getId(), kakaoPlaceId, currentLatitude, currentLongitude
        );
        List<ReviewDetailResponse> reviewDetails = placeRepository.findAllTargetMemberReviews(
                loginMember.getId(), kakaoPlaceId
        );
        Map<Integer, Integer> ratingGraph = new HashMap<>();
        for (ReviewDetailResponse reviewDetail : reviewDetails) {
            int range = (int) Math.floor(reviewDetail.getStarRating());
            ratingGraph.put(range, ratingGraph.getOrDefault(range, 0) + 1);
        }

        return MapPlaceDetailResponse.from(mapPlaceResponse, ratingGraph, reviewDetails);
    }

    @Transactional(readOnly = true)
    public List<EntirePlaceResponse> getEntirePlaces(String keyword) {
        Member loginMember = authUtil.getLoginMember();

        return kakaoMapModule.search(loginMember.getId(), keyword);
    }
}
