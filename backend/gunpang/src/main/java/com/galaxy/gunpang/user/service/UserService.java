package com.galaxy.gunpang.user.service;

import com.galaxy.gunpang.user.model.dto.SignUpReqDto;
import com.galaxy.gunpang.user.model.dto.SignUpResDto;

public interface UserService {

    SignUpResDto addUser(SignUpReqDto signUpReqDto);

}
