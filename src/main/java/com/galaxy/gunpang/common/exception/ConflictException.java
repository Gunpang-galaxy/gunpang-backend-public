package com.galaxy.gunpang.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends GunpangApiException{

    public ConflictException(String reason) {
        super(HttpStatus.CONFLICT, reason);
    }
}
