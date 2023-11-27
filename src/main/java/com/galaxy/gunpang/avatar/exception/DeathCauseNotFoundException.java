package com.galaxy.gunpang.avatar.exception;

import com.galaxy.gunpang.common.exception.NotFoundException;
import com.galaxy.gunpang.common.model.enums.InitCode;

public class DeathCauseNotFoundException extends NotFoundException {
    public DeathCauseNotFoundException() {
        super("사망 원인이 존재하지 않습니다.");
    }
    public DeathCauseNotFoundException(InitCode initCode){
        super(initCode.name());
    }
}
