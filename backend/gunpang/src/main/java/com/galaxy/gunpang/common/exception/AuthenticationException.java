package com.galaxy.gunpang.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public abstract class AuthenticationException extends GunpangApiException {

    public AuthenticationException(String reason) {
        super(HttpStatus.UNAUTHORIZED, reason);
    }
}