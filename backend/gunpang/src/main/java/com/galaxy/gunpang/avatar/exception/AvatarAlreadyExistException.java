package com.galaxy.gunpang.avatar.exception;

import com.galaxy.gunpang.common.exception.ConflictException;

public class AvatarAlreadyExistException extends ConflictException {
    public AvatarAlreadyExistException(Long userId) {
        super("이미 키우고 있는 아바타가 존재합니다 : " + userId);
    }
}
