package com.galaxy.gunpang.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public abstract class GunpangApiException extends ResponseStatusException {

    public GunpangApiException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
