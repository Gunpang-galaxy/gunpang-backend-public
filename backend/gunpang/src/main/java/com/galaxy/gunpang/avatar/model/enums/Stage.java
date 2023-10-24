package com.galaxy.gunpang.avatar.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Stage {
    SEA("바다"),
    LAND("땅"),
    SKY("하늘"),
    SPACE("우주");

    private final String description;
}
