package com.galaxy.gunpang.user.service;

import com.galaxy.gunpang.user.model.dto.AccessTokenResDto;
import com.galaxy.gunpang.user.model.dto.GoogleIdResDto;
import com.galaxy.gunpang.user.model.dto.LogInResDto;

public interface RedisService {

    void setTokens(LogInResDto logInResDto);

    void setTokens(String id, String tokens);

    String getTokens(String id);

    void updateTokens(LogInResDto logInResDto);

    void updateTokens(String id, String tokens);

    AccessTokenResDto updateTokens(String accessToken);

    void deleteTokens(String id);

    void deleteTokens(GoogleIdResDto googleIdResDto);

}
