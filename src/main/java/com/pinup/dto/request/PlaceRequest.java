package com.pinup.dto.request;

import com.pinup.entity.Place;
import com.pinup.enums.PlaceCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DB에 업체 등록하는 DTO
 * 리뷰가 처음 등록되기 전, 카카오맵 ID를 사용하여 업체 사전 등록
 */
@Data
public class PlaceRequest {

    @NotBlank(message = "카카오맵 장소 ID는 필수 입력값입니다.")
    private String kakaoPlaceId;

    private String name; // 장소명
    private String category; // 장소 카테고리
    private String address; // 주소
    private String roadAddress; // 도로명 주소
    private String longitude; // 경도(X)
    private String latitude; // 위도(Y)

    public Place toEntity() {

        PlaceCategory newPlaceCategory = null;

        for (PlaceCategory placeCategory : PlaceCategory.values()) {
            String placeType = placeCategory.name();
            if (placeType.equalsIgnoreCase(category)) {
                newPlaceCategory = placeCategory;
            }
        }

        return Place.builder()
                .kakaoMapId(kakaoPlaceId)
                .name(name)
                .placeCategory(newPlaceCategory)
                .address(address)
                .roadAddress(roadAddress)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }
}
