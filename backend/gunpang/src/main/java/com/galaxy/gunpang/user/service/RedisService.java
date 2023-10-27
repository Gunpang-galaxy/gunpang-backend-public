package com.galaxy.gunpang.user.service;

import com.galaxy.gunpang.user.model.dto.LogInResDto;

public interface RedisService {

    void setTokens(LogInResDto logInResDto);

    void setTokens(String id, String tokens);

    String getTokens(String id);

    void updateTokens(String id, String tokens);
}
