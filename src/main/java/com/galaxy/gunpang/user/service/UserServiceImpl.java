package com.galaxy.gunpang.user.service;

import com.galaxy.gunpang.user.exception.UserNotFoundException;
import com.galaxy.gunpang.user.model.User;
import com.galaxy.gunpang.user.model.dto.*;
import com.galaxy.gunpang.user.model.enums.Gender;
import com.galaxy.gunpang.user.repository.UserRepository;
import com.galaxy.gunpang.user.utils.JwtUtil;
import com.galaxy.gunpang.user.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserUtil userUtil;
    private final JwtUtil jwtUtil;

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

    @Override
    public LogInResDto getTokenByGoogleId(String googleId) {
        userRepository.getIdByGoogleId(googleId);

        return LogInResDto.builder()
                .googleId(googleId)
                .accessToken(jwtUtil.createAccessToken(googleId))
                .refreshToken(jwtUtil.createRefreshToken(googleId))
                .build();
    }

    @Override
    public UserExistenceResDto existsByGoogleId(String googleId) {
        return UserExistenceResDto.builder()
                .isUser(userRepository.getIdByGoogleId(googleId) != null)
                .build();
    }

    @Override
    public UserIdResDto getIdByGoogleId(String googleId) {
        return UserIdResDto.builder()
                .id(userRepository.getIdByGoogleId(googleId))
                .build();
    }

    @Override
    public UserIdResDto getIdByToken(String accessToken) {
        // 1. accessToken에서 googleId 추출
        String googleId = jwtUtil.getGoogleIdFromToken(jwtUtil.removeBearer(accessToken));
        // 2. googleId로 userId 찾기
        return UserIdResDto.builder()
                .id(userRepository.getIdByGoogleId(googleId))
                .build();
    }

    @Override
    public UserInfoResDto getUserInfo(long userId) {
        //이메일, 성별, 출생, 키 반환
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        UserInfoResDto userInfoResDto = UserInfoResDto.builder().email(user.getEmail())
                .gender(user.getGender())
                .height(user.getHeight())
                .birthYear(user.getBirthYear()).build();

        return userInfoResDto;
    }

    @Override
    public UserExistenceResDto findUserByEmail(String email) {
        return UserExistenceResDto.builder()
                .isUser(userRepository.findByEmail(email) != null)
                .build();
    }

    @Override
    public void updateUserToDeleted(GoogleIdResDto googleIdResDto) {
        User user = userRepository.findByGoogleId(googleIdResDto.getGoogleId());
        user.updateUserToDeleted();
        userRepository.save(user);
    }

}
