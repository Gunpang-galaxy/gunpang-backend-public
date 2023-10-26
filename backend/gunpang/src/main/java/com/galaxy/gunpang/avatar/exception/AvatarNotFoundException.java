package com.galaxy.gunpang.avatar.exception;

import com.galaxy.gunpang.common.exception.NotFoundException;

public class AvatarNotFoundException extends NotFoundException {
    public AvatarNotFoundException(Long avatarId) {
        super("아바타가 존재하지 않습니다 : " + avatarId);
    }
}
