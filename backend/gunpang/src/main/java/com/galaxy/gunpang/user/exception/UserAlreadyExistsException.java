package com.galaxy.gunpang.user.exception;

import com.galaxy.gunpang.common.exception.ConflictException;

public class UserAlreadyExistsException extends ConflictException {
    public UserAlreadyExistsException(String googleId) {
        super("이미 존재하는 이메일입니다. : " + googleId);}
}
