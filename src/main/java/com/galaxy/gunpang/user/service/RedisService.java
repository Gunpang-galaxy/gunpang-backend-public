package com.galaxy.gunpang.user.service;

import com.galaxy.gunpang.user.model.dto.AccessTokenResDto;
import com.galaxy.gunpang.user.model.dto.GoogleIdResDto;
import com.galaxy.gunpang.user.model.dto.LogInResDto;

public interface RedisService {

    void setToken(LogInResDto logInResDto);

    void setToken(String id, String token);

    String getToken(String id);

    void updateToken(LogInResDto logInResDto);

    void updateToken(String id, String token);

    AccessTokenResDto updateToken(String accessToken);

    void deleteToken(String id);

    void deleteToken(GoogleIdResDto googleIdResDto);

}
