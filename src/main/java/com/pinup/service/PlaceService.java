package com.pinup.service;

import com.pinup.dto.response.PlaceResponse;
import com.pinup.entity.Member;
import com.pinup.enums.PlaceCategory;
import com.pinup.global.exception.PinUpException;
import com.pinup.global.maps.KakaoMapModule;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final MemberRepository memberRepository;
    private final KakaoMapModule kakaoMapModule;


    public List<PlaceResponse> searchPlacesByCategory(String category, String longitude, String latitude) {

        Member currentMember = findMember();

        List<Map<String, Object>> kakaoMapPlaceInfoList = new ArrayList<>();
        // Enum에서 category와 일치하는 장소의 code 찾기
        for (PlaceCategory placeCategory : PlaceCategory.values()) {
            String placeType = placeCategory.name();
            if (placeType.equalsIgnoreCase(category)) {
                 kakaoMapPlaceInfoList = kakaoMapModule.searchPlacesByCategory(
                         currentMember,
                         placeCategory,
                         longitude,
                         latitude
                 );
            }
        }

        List<PlaceResponse> placeResponseList = new ArrayList<>();

        if (!kakaoMapPlaceInfoList.isEmpty()) {
            for (Map<String, Object> placeInfo : kakaoMapPlaceInfoList) {
                PlaceResponse placeResponse = PlaceResponse.from(placeInfo);
                placeResponseList.add(placeResponse);
            }
        }

        return placeResponseList;
    }

    private Member findMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByEmail(email).orElseThrow(() -> PinUpException.MEMBER_NOT_FOUND);
    }
}
