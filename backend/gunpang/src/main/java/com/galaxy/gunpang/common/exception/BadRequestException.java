package com.galaxy.gunpang.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public abstract class BadRequestException extends GunpangApiException {

    public BadRequestException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}