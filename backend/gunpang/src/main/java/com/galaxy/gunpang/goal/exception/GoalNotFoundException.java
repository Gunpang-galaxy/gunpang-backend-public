package com.galaxy.gunpang.goal.exception;

import com.galaxy.gunpang.common.exception.NotFoundException;
import com.galaxy.gunpang.common.model.enums.InitCode;

public class GoalNotFoundException extends NotFoundException {
    public GoalNotFoundException() {
        super("아바타의 목표가 존재하지 않습니다.");
    }
    public GoalNotFoundException(InitCode initCode){
        super(initCode.name());
    }
}
