package com.pinup.global.config.kakao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinup.domain.place.dto.response.EntirePlaceResponse;
import com.pinup.domain.place.entity.PlaceCategory;
import com.pinup.domain.place.repository.PlaceRepository;
import com.pinup.global.common.Formatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoMapModule {

    private static final String KAKAO_MAP_API_URI = "https://dapi.kakao.com/v2/local/search";
    private static final String KEYWORD_FORMAT = "/keyword.json";
    private static final String HEADER_KEY = "Authorization";
    private static final String HEADER_VALUE = "KakaoAK ";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PlaceRepository placeRepository;

    @Value("${kakao.key}")
    private String apiKey;

    public List<EntirePlaceResponse> search(Long memberId, String keyword) {
        URI uri = buildUri(keyword);
        return executeSearchRequest(uri, memberId);
    }

    private URI buildUri(String keyword) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(KAKAO_MAP_API_URI)
                .path(KEYWORD_FORMAT)
                .queryParam("query", keyword);

        return uriBuilder.encode().build().toUri();
    }

    private List<EntirePlaceResponse> executeSearchRequest(URI uri, Long memberId) {
        List<EntirePlaceResponse> placeInfoList = new ArrayList<>();
        try {
            RequestEntity<Void> apiRequest = RequestEntity
                    .get(uri)
                    .header(HEADER_KEY, HEADER_VALUE + apiKey)
                    .build();
            ResponseEntity<String> apiResponse = restTemplate.exchange(apiRequest, String.class);
            JsonNode jsonNode = objectMapper.readTree(apiResponse.getBody());
            JsonNode documentsNode = jsonNode.path("documents");
            for (JsonNode documentNode : documentsNode) {
                EntirePlaceResponse placeInfo = extractPlaceInfo(documentNode, memberId);
                placeInfoList.add(placeInfo);
            }
        } catch (Exception e) {
            log.error("카카오맵 API 요청 간 에러 발생!", e);
        }

        return placeInfoList;
    }

    private EntirePlaceResponse extractPlaceInfo(JsonNode documentNode, Long memberId) {
        String kakaoPlaceId = documentNode.path("id").asText();
        String categoryGroupCode = documentNode.path("category_group_code").asText();
        PlaceCategory placeCategory = PlaceCategory.getCategoryByCode(categoryGroupCode);
        Long reviewCount = placeRepository.getReviewCount(memberId, kakaoPlaceId);
        Double averageStarRating = placeRepository.getAverageStarRating(memberId, kakaoPlaceId);

        return EntirePlaceResponse.builder()
                .kakaoPlaceId(kakaoPlaceId)
                .name(documentNode.path("place_name").asText())
                .placeCategory(placeCategory)
                .description(placeCategory.getDescription())
                .categoryCode(placeCategory.getCode())
                .address(documentNode.path("address_name").asText())
                .roadAddress(documentNode.path("road_address_name").asText())
                .latitude(documentNode.path("y").asText())
                .longitude(documentNode.path("x").asText())
                .reviewCount(reviewCount.intValue())
                .averageStarRating(averageStarRating != null ? Formatter.formatStarRating(averageStarRating) : 0.0)
                .build();
    }
}
