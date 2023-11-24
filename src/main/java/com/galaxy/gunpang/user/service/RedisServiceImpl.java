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
    public void setToken(String id, String token) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        log.info("Redis에 Refresh Token을 등록합니다. : " + token);
        values.set(id, token, Duration.ofDays(jwtUtil.getRefreshTokenValidTimeAsDay()));
    }

    @Override
    public void setToken(LogInResDto logInResDto) {
        String token = logInResDto.getRefreshToken();
        setToken(logInResDto.getGoogleId(), token);
    }

    @Override
    public String getToken(String id) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(id);
    }

    @Override
    public void deleteToken(String id) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        log.info("id가 " + id + "인 Token 정보를 삭제합니다.");
        values.getOperations().delete(id);
    }

    @Override
    public void deleteToken(GoogleIdResDto googleIdResDto) {
        deleteToken(googleIdResDto.getGoogleId());
    }

    @Override
    public void updateToken(String id, String token) {
        deleteToken(id);
        setToken(id, token);
    }

    @Override
    public void updateToken(LogInResDto logInResDto) {
        String googleId = logInResDto.getGoogleId();
        String token = logInResDto.getRefreshToken();
        updateToken(googleId, token);
    }

    @Override
    public AccessTokenResDto updateToken(String accessToken) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();

        String googleId = jwtUtil.getGoogleIdFromToken(accessToken);
        String refreshToken = getToken(googleId);
        String newAccessToken = jwtUtil.recreateAccessToken(refreshToken);
        String token = refreshToken;

        updateToken(googleId, token);

        return AccessTokenResDto.builder()
                .accessToken(newAccessToken)
                .build();
    }

}
