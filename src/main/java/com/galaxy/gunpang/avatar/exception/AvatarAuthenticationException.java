package com.galaxy.gunpang.avatar.exception;

import com.galaxy.gunpang.common.exception.AuthenticationException;
import com.galaxy.gunpang.common.model.enums.InitCode;

public class AvatarAuthenticationException extends AuthenticationException {
    public AvatarAuthenticationException() {
        super("권한이 없습니다.");
    }

    public AvatarAuthenticationException(InitCode initCode){
        super(initCode.name());
    }
}
