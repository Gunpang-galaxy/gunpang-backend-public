package com.galaxy.gunpang.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public abstract class NotFoundException extends GunpangApiException {

    public NotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}