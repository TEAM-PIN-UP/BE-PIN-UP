package com.pinup.service;

import com.pinup.dto.request.PlaceRequest;
import com.pinup.dto.request.ReviewRequest;
import com.pinup.dto.response.ReviewResponse;
import com.pinup.entity.*;
import com.pinup.global.exception.PinUpException;
import com.pinup.global.s3.S3Service;
import com.pinup.repository.MemberRepository;
import com.pinup.repository.PlaceRepository;
import com.pinup.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private static final String FILE_TYPE = "reviews";
    private static final int IMAGES_LIMIT = 3;
    private static final int KEYWORDS_LIMIT = 10;
    private static final int KEYWORDS_LENGTH_LIMIT = 10;

    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;

    @Transactional
    public ReviewResponse register(ReviewRequest reviewRequest,
                                   PlaceRequest placeRequest,
                                   List<MultipartFile> images) {

        Member findMember = findMember();
        Place place = findOrCreatePlace(placeRequest);
        List<String> uploadedFileUrls = uploadImages(images);
        List<String> inputKeywords = saveKeywords(reviewRequest);
        Review newReview = createReview(reviewRequest, findMember, place, uploadedFileUrls, inputKeywords);
        Review savedReview = reviewRepository.save(newReview);

        return ReviewResponse.of(savedReview, uploadedFileUrls, inputKeywords);
    }

    /**
     * SecurityContextHolder 에서 현재 로그인 한 사용자 이메일 Get 후 회원 정보 탐색
     */
    private Member findMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> PinUpException.MEMBER_NOT_FOUND);
    }

    /**
     * 카카오맵 ID로 DB에 등록된 업체 정보 조회
     * DB에 업체 미등록 시, 업체 신규 생성
     */
    private Place findOrCreatePlace(PlaceRequest placeRequest) {
        String kakaoPlaceId = placeRequest.getKakaoPlaceId();

        return placeRepository.findByKakaoMapId(kakaoPlaceId)
                .orElseGet(() -> placeRepository.save(placeRequest.toEntity()));
    }

    /**
     * S3에 이미지 업로드
     * 이미지 최대 업로드 가능 갯수: 3장
     */
    private List<String> uploadImages(List<MultipartFile> images) {

        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        if (images.size() > IMAGES_LIMIT) {
            throw PinUpException.IMAGES_LIMIT_EXCEEDED;
        }

        List<String> uploadedFileUrls = new ArrayList<>();

        for (MultipartFile multipartFile : images) {
            String uploadedFileUrl = s3Service.uploadFile(FILE_TYPE, multipartFile);
            uploadedFileUrls.add(uploadedFileUrl);
        }

        return uploadedFileUrls;
    }

    /**
     * 키워드 저장
     * 키워드 최대 10개 등록 가능
     * 각 키워드 글자 수는 최대 10자
     */
    private List<String> saveKeywords(ReviewRequest reviewRequest) {
        List<String> inputKeywords = new ArrayList<>();

        if (reviewRequest.getKeywords() != null && !reviewRequest.getKeywords().isEmpty()) {
            if (reviewRequest.getKeywords().size() > KEYWORDS_LIMIT) {
                throw PinUpException.KEYWORDS_LIMIT_EXCEEDED;
            }

            for (String inputKeyword : reviewRequest.getKeywords()) {
                if (inputKeyword.length() > KEYWORDS_LENGTH_LIMIT) {
                    throw PinUpException.KEYWORDS_LENGTH_LIMIT_EXCEEDED;
                }
                inputKeywords.add(inputKeyword);
            }
        }

        return inputKeywords;
    }

    /**
     * 생성된 리뷰 엔티티에 작성자, 업체, 리뷰 이미지, 키워드 연결
     */
    private Review createReview(ReviewRequest reviewRequest, Member writer, Place place,
                                List<String> uploadedFileUrls, List<String> inputKeywords) {

        Review newReview = reviewRequest.toEntity();
        newReview.attachMember(writer);
        newReview.attachPlace(place);

        // 리뷰 이미지와 키워드를 리뷰 엔티티에 연결
        for (String fileUrl : uploadedFileUrls) {
            ReviewImage reviewImage = new ReviewImage(fileUrl);
            reviewImage.attachReview(newReview);
        }

        for (String inputKeyword : inputKeywords) {
            Keyword keyword = new Keyword(inputKeyword);
            keyword.attachReview(newReview);
        }

        return newReview;
    }
}
