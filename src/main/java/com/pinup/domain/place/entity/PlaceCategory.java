package com.pinup.domain.place.entity;

import com.pinup.global.exception.EntityNotFoundException;
import com.pinup.global.response.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceCategory {

    ALL("전체", "ALL"),
    RESTAURANT("음식점", "FD6"),
    CAFE("카페", "CE7"),
    MART("대형마트", "MT1"),
    STORE("편의점", "CS2"),
    KINDERGARTEN("어린이집, 유치원", "PS3"),
    SCHOOL("학교", "SC4"),
    ACADEMY("학원", "AC5"),
    PARKING_LOT("주차장", "PK6"),
    GAS_STATION("주유소, 충전소", "OL7"),
    SUBWAY("지하철역", "SW8"),
    BANK("은행", "BK9"),
    CULTURAL_FACILITY("문화시설", "CT1"),
    BROKERAGE("중개업소", "AG2"),
    PUBLIC_OFFICE("공공기관", "PO3"),
    ATTRACTION("관광명소", "AT4"),
    ACCOMMODATION("숙박", "AD5"),
    HOSPITAL("병원", "HP8"),
    PHARMACY("약국", "PM9"),
    ETC("기타시설", "ETC")
    ;

    private final String description;
    private final String code;

    public static PlaceCategory getCategory(String category) {
        for (PlaceCategory placeCategory : PlaceCategory.values()) {
            if (placeCategory.name().equalsIgnoreCase(category)) {
                return placeCategory;
            }
        }
        throw new EntityNotFoundException(ErrorCode.PLACE_CATEGORY_NOT_FOUND);
    }

    public static PlaceCategory getCategoryByCode(String categoryCode) {
        if (categoryCode.isEmpty()) {
            return PlaceCategory.ETC;
        }

        for (PlaceCategory placeCategory : PlaceCategory.values()) {
            if (placeCategory.code.equalsIgnoreCase(categoryCode)) {
                return placeCategory;
            }
        }
        throw new EntityNotFoundException(ErrorCode.PLACE_CATEGORY_NOT_FOUND);
    }
}
