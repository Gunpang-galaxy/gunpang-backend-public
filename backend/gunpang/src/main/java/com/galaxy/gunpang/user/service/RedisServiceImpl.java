package com.galaxy.gunpang.user.service;

import com.galaxy.gunpang.user.model.dto.AccessTokenResDto;
import com.galaxy.gunpang.user.model.dto.GoogleIdResDto;
import com.galaxy.gunpang.user.model.dto.LogInResDto;
import com.galaxy.gunpang.user.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    @Override
    public void setTokens(String id, String tokens) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        log.info("Redis에 Access Token과 Refresh Token을 등록합니다. : " + tokens);
        values.set(id, tokens, Duration.ofDays(jwtUtil.getRefreshTokenValidTimeAsDay()));
    }

    @Override
    public void setTokens(LogInResDto logInResDto) {
        String tokens = logInResDto.getAccessToken() + "," + logInResDto.getRefreshToken();
        setTokens(logInResDto.getGoogleId(), tokens);
    }

    @Override
    public String getTokens(String id) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(id);
    }

    @Override
    public void deleteTokens(String id) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        log.info("id가 " + id + "인 Token 정보를 삭제합니다.");
        values.getOperations().delete(id);
    }

    @Override
    public void deleteTokens(GoogleIdResDto googleIdResDto) {
        deleteTokens(googleIdResDto.getGoogleId());
    }

    @Override
    public void updateTokens(String id, String tokens) {
        deleteTokens(id);
        setTokens(id, tokens);
    }

    @Override
    public void updateTokens(LogInResDto logInResDto) {
        String googleId = logInResDto.getGoogleId();
        String tokens = logInResDto.getAccessToken() + "," + logInResDto.getRefreshToken();
        updateTokens(googleId, tokens);
    }

    @Override
    public AccessTokenResDto updateTokens(String accessToken) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();

        String googleId = jwtUtil.getGoogleIdFromToken(accessToken);
        String refreshToken = (getTokens(googleId)).split(",")[1];
        String newAccessToken = jwtUtil.recreateAccessToken(refreshToken);
        String tokens = newAccessToken + "," + refreshToken;

        updateTokens(googleId, tokens);

        return AccessTokenResDto.builder()
                .accessToken(newAccessToken)
                .build();
    }

}
