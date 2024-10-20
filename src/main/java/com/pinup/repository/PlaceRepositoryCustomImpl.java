package com.pinup.repository;

import com.pinup.dto.response.PlaceReviewInfoDto;
import com.pinup.dto.response.PlaceReviewInfoDto.ReviewInfoForUserDto;
import com.pinup.entity.Member;
import com.pinup.enums.PlaceCategory;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.pinup.entity.QFriendShip.friendShip;
import static com.pinup.entity.QMember.member;
import static com.pinup.entity.QPlace.place;
import static com.pinup.entity.QReview.review;
import static com.pinup.entity.QReviewImage.reviewImage;

@Repository
public class PlaceRepositoryCustomImpl implements PlaceRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @Autowired
    public PlaceRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public PlaceReviewInfoDto findPlaceReviewInfo(String kakaoMapId, PlaceCategory placeCategory, Member currentMember) {

        List<Tuple> result = fetchPlaceReviewInfoFromDB(kakaoMapId, placeCategory, currentMember);

        List<ReviewInfoForUserDto> reviewInfoForUserDtoList = result.stream().map(
                tuple -> ReviewInfoForUserDto.builder()
                        .nickname(tuple.get(member.name))
                        .content(tuple.get(review.comment))
                        .rating(tuple.get(review.rating))
                        .registerDate(tuple.get(review.createdAt).format(formatter))
                        .profileImageUrl(tuple.get(member.profileImageUrl))
                        .reviewImageUrls(fetchReviewImageUrls(tuple.get(review.id)))
                        .build()).toList();

        double averageReviewRating = calculateAverageRating(result);
        int reviewCount = result.size();

        return PlaceReviewInfoDto.builder()
                .reviewDataForMember(reviewInfoForUserDtoList)
                .averageReviewRating(averageReviewRating)
                .reviewCount(reviewCount)
                .build();
    }

    private List<Tuple> fetchPlaceReviewInfoFromDB(String kakaoMapId,
                                                   PlaceCategory placeCategory,
                                                   Member currentMember) {
        return queryFactory
                .select(review.id,
                        member.name,
                        review.comment,
                        review.rating,
                        review.createdAt,
                        member.profileImageUrl
                )
                .from(review)
                .join(review.member, member)
                .join(review.place, place)
                .leftJoin(friendShip).on(friendShip.friend.eq(member))
                .where(place.kakaoMapId.eq(kakaoMapId)
                        .and(place.placeCategory.eq(placeCategory))
                        .and(friendShip.member.eq(currentMember) // 내 친구들이 쓴 리뷰
                                .or(review.member.eq(currentMember)))) // 내가 쓴 리뷰
                .orderBy(review.createdAt.asc())
                .fetch();
    }

    private List<String> fetchReviewImageUrls(Long reviewId) {

        return queryFactory
                .select(reviewImage.url)
                .from(reviewImage)
                .where(reviewImage.review.id.eq(reviewId))
                .fetch();
    }

    private double calculateAverageRating(List<Tuple> result) {
        return result.stream()
                .mapToDouble(tuple -> tuple.get(review.rating))
                .average()
                .orElse(0.0);
    }
}
