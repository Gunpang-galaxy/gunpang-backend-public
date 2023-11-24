package com.galaxy.gunpang.user.service;

import com.galaxy.gunpang.user.model.dto.GoogleIdResDto;
import com.galaxy.gunpang.user.model.dto.LogInResDto;
import com.galaxy.gunpang.user.model.dto.TokenValidationResDto;

public interface JwtService {
    LogInResDto createTokens(String googleId);

    GoogleIdResDto getGoogleId(String accessToken);

    TokenValidationResDto validateToken(String accessToken);

}
