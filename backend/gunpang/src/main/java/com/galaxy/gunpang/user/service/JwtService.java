package com.galaxy.gunpang.user.service;

import com.galaxy.gunpang.user.model.dto.LogInResDto;

public interface JwtService {
    LogInResDto createTokens(String googleId);
}
