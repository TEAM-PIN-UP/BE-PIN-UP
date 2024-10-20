package com.pinup.global.maps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinup.dto.response.PlaceReviewInfoDto;
import com.pinup.entity.Member;
import com.pinup.enums.PlaceCategory;
import com.pinup.repository.PlaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class KakaoMapModule {

    private static final String KAKAO_URI = "https://dapi.kakao.com";
    private static final String PATH = "/v2/local/search";
    private static final String HEADER_KEY = "Authorization";
    private static final String HEADER_VALUE = "KakaoAK ";

    private final PlaceRepository placeRepository;

    @Value("${kakao.key}")
    private String apiKey;

    @Autowired
    public KakaoMapModule(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Map<String, Object>> searchPlacesByCategory(
            Member currentMember,
            PlaceCategory placeCategory,
            String longitude,
            String latitude
    ) {

        List<Map<String, Object>> placeInfoList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        String method = "/category.json";
        int radius = 20000;
        String sort = "distance";

        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(KAKAO_URI)
                    .path(PATH + method)
                    .queryParam("category_group_code", placeCategory.getCode())
                    .queryParam("x", longitude)
                    .queryParam("y", latitude)
                    .queryParam("radius", radius)
                    .queryParam("sort", sort)
                    .encode()
                    .build()
                    .toUri();

            RequestEntity<Void> request = RequestEntity
                                        .get(uri)
                                        .header(HEADER_KEY, HEADER_VALUE + apiKey)
                                        .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode documentsNode = jsonNode.path("documents");

            for (JsonNode documentNode : documentsNode) {

                Map<String, Object> placeInfo = new HashMap<>();
                String kakaoMapId = documentNode.path("id").asText();

                // DB에서 가져온 데이터
                PlaceReviewInfoDto placeReviewInfoDto = placeRepository
                        .findPlaceReviewInfo(kakaoMapId, placeCategory, currentMember);

                // 카카오맵 API에서 가져온 데이터
                placeInfo.put("kakaoMapId", kakaoMapId);
                placeInfo.put("name", documentNode.path("place_name").asText());
                placeInfo.put("category", documentNode.path("category_group_name").asText());
                placeInfo.put("phone", documentNode.path("phone").asText());
                placeInfo.put("address", documentNode.path("address_name").asText());
                placeInfo.put("roadAddress", documentNode.path("road_address_name").asText());
                placeInfo.put("longitudeX", documentNode.path("x").asText());
                placeInfo.put("latitudeY", documentNode.path("y").asText());
                placeInfo.put("distance", documentNode.path("distance").asText());
                placeInfo.put("reviewData", placeReviewInfoDto);

                placeInfoList.add(placeInfo);
            }

            return placeInfoList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return placeInfoList;
    }
}
