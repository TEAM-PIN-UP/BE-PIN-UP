package com.pinup.entity;

import com.pinup.enums.PlaceCategory;
import com.pinup.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String kakaoMapId; // 카카오맵에서 부여된 장소 ID

    private String name; // 장소명
    private String address; // 주소
    private String roadAddress; // 도로명 주소
    private String defaultImgUrl; // 기본 이미지

    private String longitude; // 경도(X)
    private String latitude; // 위도(Y)

    private Double averageRating = 0.0; // 평균별점
    private String status; // 상태

    @Enumerated(EnumType.STRING)
    private PlaceCategory placeCategory;

    @OneToMany(mappedBy = "place", fetch = FetchType.EAGER)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Place(String kakaoMapId, String name, String address, String roadAddress,
                 String longitude, String latitude, PlaceCategory placeCategory) {
        this.kakaoMapId = kakaoMapId;
        this.name = name;
        this.address = address;
        this.roadAddress = roadAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = "Y";
        this.placeCategory = placeCategory;
    }

    public void updateDefaultImgUrl(String defaultImgUrl) {
        this.defaultImgUrl = defaultImgUrl;
    }

    // 평균 평점 업데이트 (전체 유저)
    public void updateAverageRating() {

        double totalRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .sum();

        double calculatedAverage = totalRating / reviews.size();

        // 소수점 둘째 자리까지 반올림
        this.averageRating = Math.round(calculatedAverage * 100) / 100.0;
    }

    // TODO: 내 친구들이 해당 가게에 부여한 평점의 평균 계산 로직 구현예정.


}
