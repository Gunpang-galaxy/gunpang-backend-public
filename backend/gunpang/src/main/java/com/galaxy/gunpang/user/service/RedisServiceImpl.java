package com.galaxy.gunpang.user.service;

import com.galaxy.gunpang.user.model.dto.AccessTokenResDto;
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
    public void setTokens(LogInResDto logInResDto) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String tokens = logInResDto.getAccessToken() + "," + logInResDto.getRefreshToken();
        log.info("Redis에 Access Token과 Refresh Token을 등록합니다. : " + tokens);
        values.set(logInResDto.getGoogleId(), tokens, Duration.ofDays(jwtUtil.getRefreshTokenValidTimeAsDay()));
    }

    @Override
    public void setTokens(String id, String tokens) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(id, tokens, Duration.ofDays(jwtUtil.getRefreshTokenValidTimeAsDay()));
        log.info("Redis에 Access Token과 Refresh Token을 등록합니다. : " + tokens);
    }

    @Override
    public String getTokens(String id) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();

        return values.get(id);
    }

    @Override
    public void updateTokens(String id, String tokens) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();

        values.getOperations().delete(id);
        log.info("id가 " + id + "인 Token 정보를 삭제합니다.");
        setTokens(id, tokens);
        log.info("Redis에 새로운 Access Token과 Refresh Token을 등록합니다. : " + tokens);

    }

    @Override
    public AccessTokenResDto updateTokens(String accessToken) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();

        String googleId = jwtUtil.getGoogleIdFromToken(accessToken);
        String refreshToken = (values.get(googleId)).split(",")[1];
        String newAccessToken = jwtUtil.recreateAccessToken(refreshToken);
        String tokens = newAccessToken + "," + refreshToken;

        values.getOperations().delete(googleId);
        log.info("id가 " + googleId + "인 Token 정보를 삭제합니다.");

        setTokens(googleId, tokens);
        log.info("Redis에 새로운 Access Token과 Refresh Token을 등록합니다. : " + tokens);

        return AccessTokenResDto.builder()
                .accessToken(newAccessToken)
                .build();
    }
}
