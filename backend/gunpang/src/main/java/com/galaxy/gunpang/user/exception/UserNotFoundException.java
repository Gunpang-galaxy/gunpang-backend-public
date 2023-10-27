package com.galaxy.gunpang.user.exception;

import com.galaxy.gunpang.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String googleId) {
        super("사용자가 존재하지 않습니다 : " + googleId);
    }
}
