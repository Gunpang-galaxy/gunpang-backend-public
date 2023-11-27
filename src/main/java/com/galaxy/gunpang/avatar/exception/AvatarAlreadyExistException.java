package com.galaxy.gunpang.avatar.exception;

import com.galaxy.gunpang.common.exception.ConflictException;
import com.galaxy.gunpang.common.model.enums.InitCode;

public class AvatarAlreadyExistException extends ConflictException {
    public AvatarAlreadyExistException() {
        super("이미 키우고 있는 아바타가 존재합니다");
    }
    public AvatarAlreadyExistException(InitCode initCode){
        super(initCode.name());
    }

}
