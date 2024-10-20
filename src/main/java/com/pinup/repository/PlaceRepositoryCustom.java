package com.pinup.repository;

import com.pinup.dto.response.PlaceReviewInfoDto;
import com.pinup.entity.Member;
import com.pinup.enums.PlaceCategory;

public interface PlaceRepositoryCustom {
    PlaceReviewInfoDto findPlaceReviewInfo(
            String kakaoMapId,
            PlaceCategory placeCategory,
            Member currentMember
    );
}
