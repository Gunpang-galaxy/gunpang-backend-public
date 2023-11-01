package com.galaxy.gunpang.goal.exception;

import com.galaxy.gunpang.common.exception.ConflictException;

public class GoalAlreadyExistException extends ConflictException {
    public GoalAlreadyExistException(Long avatarId) {
        super("이미 목표가 존재하는 아바타 입니다 : " + avatarId);
    }
}
