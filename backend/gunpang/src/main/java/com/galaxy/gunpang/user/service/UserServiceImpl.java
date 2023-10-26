package com.galaxy.gunpang.user.service;

import com.galaxy.gunpang.user.model.User;
import com.galaxy.gunpang.user.model.dto.SignUpReqDto;
import com.galaxy.gunpang.user.model.dto.SignUpResDto;
import com.galaxy.gunpang.user.model.enums.Gender;
import com.galaxy.gunpang.user.repository.UserRepository;
import com.galaxy.gunpang.user.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserUtil userUtil;

    @Override
    public SignUpResDto addUser(SignUpReqDto signUpReqDto) {

        // id 랜덤 값으로 생성 및 중복 확인
        boolean isIdDuplicate = true;
        long id = 0;

        while (isIdDuplicate) {
            id = userUtil.generateRandomId();
            Optional<User> existingUser = userRepository.findById(id);
            isIdDuplicate = existingUser.isPresent();
        }

        // newUser 생성하여 저장
        User newUser = User.builder()
                .id(id)
                .googleId(signUpReqDto.getGoogleId())
                .email(signUpReqDto.getEmail())
                .gender(Gender.valueOf(signUpReqDto.getGender()))
                .birthYear(signUpReqDto.getBirthYear())
                .height(signUpReqDto.getHeight())
                .build();
        userRepository.save(newUser);

        return SignUpResDto.builder()
                .googleId(newUser.getGoogleId())
                .build();
    }
}
