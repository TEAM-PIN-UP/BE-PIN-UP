package com.pinup.entity;

import com.pinup.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    private String comment;

    @Column(nullable = false)
    private Double rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @OneToMany(mappedBy = "review", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @OneToMany(mappedBy = "review", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Keyword> keywords = new ArrayList<>();

    public Review(String comment, Double rating) {
        this.comment = comment;
        this.rating = rating;

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
