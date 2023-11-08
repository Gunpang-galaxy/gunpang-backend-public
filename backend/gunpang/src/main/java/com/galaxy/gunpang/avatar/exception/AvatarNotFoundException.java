package com.galaxy.gunpang.avatar.exception;

import com.galaxy.gunpang.common.exception.NotFoundException;
import com.galaxy.gunpang.common.model.enums.InitCode;

public class AvatarNotFoundException extends NotFoundException {
    public AvatarNotFoundException() {
        super("아바타가 존재하지 않습니다.");
    }
    public AvatarNotFoundException(InitCode initCode){
        super(initCode.name());
    }
}
