package com.pinup.domain.review.entity;

import com.pinup.domain.member.entity.Member;
import com.pinup.domain.place.entity.Place;
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
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private double starRating;

    @Column
    private String visitedDate;

    @Enumerated(EnumType.STRING)
    private ReviewType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @OneToMany(mappedBy = "review", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @Builder
    public Review(String content, double starRating, String visitedDate) {
        this.content = content;
        this.starRating = starRating;
        this.visitedDate = visitedDate;
    }

    public void setType(ReviewType type) {
        this.type = type;
    }

    public void attachPlace(Place place){
        this.place = place;
        place.getReviews().add(this);
    }

    public void attachMember(Member member){
        this.member = member;
        member.getReviews().add(this);
    }
}
