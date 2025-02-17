package com.pinup.domain.place.dto.request;

import com.pinup.domain.place.entity.Place;
import com.pinup.domain.place.entity.PlaceCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DB에 업체 등록하는 DTO
 * 리뷰가 처음 등록되기 전, 카카오맵 ID를 사용하여 업체 사전 등록
 */
@Data
@Schema(title = "장소(업체) 등록 DTO", description = "리뷰가 처음 등록되기 전, 카카오맵 ID를 사용하여 업체 사전 등록")
public class PlaceRequest {

    @NotBlank(message = "카카오맵 장소 ID를 입력하세요.")
    @Schema(description = "카카오맵에서 부여한 장소 고유 ID", example = "480323354")
    private String kakaoPlaceId;

    @Schema(description = "장소명", example = "설빙 서울마곡나루점")
    @NotBlank(message = "장소명을 입력하세요.")
    private String name; // 장소명

    @Schema(description = "카테고리", example = "CAFE")
    @NotBlank(message = "카테고리를 입력하세요.")
    private String category; // 장소 카테고리

    @Schema(description = "주소", example = "서울 강서구 마곡동 760")
    private String address; // 주소

    @Schema(description = "도로명 주소", example = "서울 강서구 마곡중앙5로 6")
    private String roadAddress; // 도로명 주소

    @Schema(description = "위도", example = "37.56706784998933")
    @NotNull(message = "위도를 입력하세요")
    private Double latitude; // 위도(Y)

    @Schema(description = "경도", example = "126.82759102697081")
    @NotNull(message = "경도를 입력하세요")
    private Double longitude; // 경도(X)

    public Place toEntity() {
        return Place.builder()
                .kakaoPlaceId(kakaoPlaceId)
                .name(name)
                .placeCategory(PlaceCategory.getCategory(category))
                .address(address)
                .roadAddress(roadAddress)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
