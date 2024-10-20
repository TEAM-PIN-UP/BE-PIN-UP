package com.pinup.controller;

import com.pinup.dto.response.PlaceResponse;
import com.pinup.service.PlaceService;
import com.pinup.global.response.ApiSuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    /**
     * 카카오맵 API - 카테고리로 장소 리스트 조회
     */
    @GetMapping("/search")
    ResponseEntity<ApiSuccessResponse<List<PlaceResponse>>> searchPlacesByCategory(
            @RequestParam("category") String category,
            @RequestParam("longitude") String longitude,
            @RequestParam("latitude") String latitude
            ) {

        List<PlaceResponse> result = placeService.searchPlacesByCategory(category, longitude, latitude);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(result));
    }
}
