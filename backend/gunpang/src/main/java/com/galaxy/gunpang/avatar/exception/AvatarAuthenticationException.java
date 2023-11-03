package com.galaxy.gunpang.avatar.exception;

import com.galaxy.gunpang.common.exception.AuthenticationException;

public class AvatarAuthenticationException extends AuthenticationException {
    public AvatarAuthenticationException(Long avatarId, Long userId) {
        super("권한이 없습니다 : " + avatarId + " - " + userId);
    }
}
