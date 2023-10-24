package com.galaxy.gunpang.avatar.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    ALIVE("생존"),
    DEAD("죽음"),
    GRADUATED("졸업");

    private final String description;
}
