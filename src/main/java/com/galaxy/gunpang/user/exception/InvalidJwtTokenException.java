package com.galaxy.gunpang.user.exception;

import com.galaxy.gunpang.common.exception.UnauthorizedException;

public class InvalidJwtTokenException extends UnauthorizedException {
    public InvalidJwtTokenException(String reason) {
        super(reason);
    }
}
