package com.pinup.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceCategory {

    RESTAURANT("음식점", "FD6"),
    CAFE("카페", "CE7"),

    ;

    private final String description;
    private final String code;
}
