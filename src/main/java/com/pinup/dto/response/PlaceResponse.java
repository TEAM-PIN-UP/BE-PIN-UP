package com.pinup.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class PlaceResponse {

    private String kakaoMapId;
    private String name;
    private String category;
    private String phone;
    private String address;
    private String roadAddress;
    private String longitudeX;
    private String latitudeY;
    private String distance;
    private PlaceReviewInfoDto reviewData;

    public static PlaceResponse from(Map<String, Object> placeInfo) {
        return PlaceResponse.builder()
                .kakaoMapId(placeInfo.get("kakaoMapId").toString())
                .name(placeInfo.get("name").toString())
                .category(placeInfo.get("category").toString())
                .phone(placeInfo.get("phone").toString())
                .address(placeInfo.get("address").toString())
                .roadAddress(placeInfo.get("roadAddress").toString())
                .longitudeX(placeInfo.get("longitudeX").toString())
                .latitudeY(placeInfo.get("latitudeY").toString())
                .distance(placeInfo.get("distance").toString())
                .reviewData((PlaceReviewInfoDto) placeInfo.get("reviewData"))
                .build();
    }
}
