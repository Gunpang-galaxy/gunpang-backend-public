package com.galaxy.gunpang.bodyComposition.exception;

import com.galaxy.gunpang.common.exception.NotFoundException;

public class BodyCompositionNotFoundException extends NotFoundException {
    public BodyCompositionNotFoundException() {
        super("해당 유저의 체성분 기록이 존재하지 않습니다.");
    }

}
