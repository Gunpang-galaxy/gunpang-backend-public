package com.galaxy.gunpang.goal.exception;

import com.galaxy.gunpang.common.exception.NotFoundException;

public class GoalNotFoundException extends NotFoundException {
    public GoalNotFoundException(Long avatarId) {
        super("아바타의 목표가 존재하지 않습니다 : " + avatarId);
    }
}
