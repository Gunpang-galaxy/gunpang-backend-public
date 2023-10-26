package com.galaxy.gunpang.avatar.exception;

import com.galaxy.gunpang.common.exception.NotFoundException;

public class DeathCauseNotFoundException extends NotFoundException {
    public DeathCauseNotFoundException(Long avatarId) {
        super("사망 원인이 존재하지 않습니다 : " + avatarId);
    }
}
