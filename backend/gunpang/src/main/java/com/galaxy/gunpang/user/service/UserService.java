package com.galaxy.gunpang.user.service;

import com.galaxy.gunpang.user.model.dto.*;

public interface UserService {

    SignUpResDto addUser(SignUpReqDto signUpReqDto);

    LogInResDto getTokenByGoogleId(String googleId);

    UserExistenceResDto existsByGoogleId(String googleId);

    UserIdResDto getIdByGoogleId(String googleId);

    UserIdResDto getIdByToken(String accessToken);
}
