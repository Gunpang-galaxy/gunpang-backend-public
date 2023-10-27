package com.galaxy.gunpang.user.service;

import com.galaxy.gunpang.user.model.dto.LogInResDto;
import com.galaxy.gunpang.user.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtUtil jwtUtil;

    @Override
    public LogInResDto createTokens(String googleId) {
        return LogInResDto.builder()
                .googleId(googleId)
                .accessToken(jwtUtil.createAccessToken(googleId))
                .refreshToken(jwtUtil.createRefreshToken(googleId))
                .build();
    }
}
