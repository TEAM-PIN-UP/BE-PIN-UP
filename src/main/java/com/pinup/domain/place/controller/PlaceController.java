package com.pinup.domain.place.controller;

import com.pinup.domain.place.dto.response.MapPlaceDetailResponse;
import com.pinup.domain.place.dto.response.EntirePlaceResponse;
import com.pinup.domain.place.dto.response.MapPlaceResponse;
import com.pinup.global.response.ResultResponse;
import com.pinup.domain.place.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pinup.global.response.ResultCode.GET_PLACES_SUCCESS;
import static com.pinup.global.response.ResultCode.GET_PLACE_DETAIL_SUCCESS;

@RestController
@RequestMapping("/api/places")
@Tag(name = "장소 API", description = "장소 목록 조회(리뷰있는 가게만), 장소 목록 조회(전체) 장소 상세 조회")
public class PlaceController {

    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    @Operation(
            summary = "리뷰 있는 장소 목록만 조회 API",
            description = "리뷰 있는 장소 목록만 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "장소 목록 조회에 성공하였습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = MapPlaceResponse.class))
                    }
            )
    })
    public ResponseEntity<ResultResponse> getPlaces(
            @Schema(description = "키워드", example = "스타벅스")
            @RequestParam(defaultValue = "", value = "query", required = false) String query,

            @Schema(description = "카테고리", example = "ALL")
            @RequestParam(defaultValue = "ALL", value = "category", required = false) String category,

            @Schema(description = "정렬조건", example = "NEAR")
            @RequestParam(defaultValue = "NEAR", value = "sort", required = false) String sort,

            @Schema(description = "SW 위도", example = "37.548608")
            @RequestParam(value = "swLatitude") double swLatitude,

            @Schema(description = "SW 경도", example = "126.795968")
            @RequestParam(value = "swLongitude") double swLongitude,

            @Schema(description = "NE 위도", example = "37.569940")
            @RequestParam(value = "neLatitude") double neLatitude,

            @Schema(description = "NE 경도", example = "126.844764")
            @RequestParam(value = "neLongitude") double neLongitude,

            @Schema(description = "현 위치 위도", example = "37.562651")
            @RequestParam(value = "currentLatitude") double currentLatitude,

            @Schema(description = "현 위치 경도", example = "126.826539")
            @RequestParam(value = "currentLongitude") double currentLongitude
    ) {
        List<MapPlaceResponse> result = placeService.getMapPlaces(
                query, category, sort, swLatitude, swLongitude, neLatitude, neLongitude, currentLatitude, currentLongitude
        );
        return ResponseEntity.ok(ResultResponse.of(GET_PLACES_SUCCESS, result));
    }

    @GetMapping("/{kakaoPlaceId}")
    @Operation(summary = "장소 상세 조회 API", description = "카카오맵에서 부여한 고유 ID로 장소 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "장소 상세 조회에 성공하였습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = MapPlaceDetailResponse.class))
                    }
            )
    })
    public ResponseEntity<ResultResponse> getMapPlaceDetail(
            @Schema(description = "카카오맵 장소 고유 ID", example = "1997608947")
            @PathVariable("kakaoPlaceId") String kakaoPlaceId,
            @Schema(description = "현 위치 위도", example = "37.562651")
            @RequestParam(value = "currentLatitude") double currentLatitude,
            @Schema(description = "현 위치 경도", example = "126.826539")
            @RequestParam(value = "currentLongitude") double currentLongitude
    ) {
        MapPlaceDetailResponse result = placeService.getMapPlaceDetail(kakaoPlaceId, currentLatitude, currentLongitude);
        return ResponseEntity.ok(ResultResponse.of(GET_PLACE_DETAIL_SUCCESS, result));
    }

    @GetMapping("/keyword")
    @Operation(summary = "전체 장소 목록 조회 API", description = "리뷰 작성할 장소 조회 시 사용 / 카카오맵 API 호출")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "장소 목록 조회에 성공하였습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = EntirePlaceResponse.class))
                    }
            )
    })
    public ResponseEntity<ResultResponse> getEntirePlaces(
            @Schema(description = "검색어", example = "하루카페") @RequestParam(value = "query") String query
    ) {
        List<EntirePlaceResponse> result = placeService.getEntirePlaces(query);
        return ResponseEntity.ok(ResultResponse.of(GET_PLACES_SUCCESS, result));
    }
}
