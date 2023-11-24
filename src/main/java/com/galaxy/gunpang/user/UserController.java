package com.galaxy.gunpang.user;

import com.galaxy.gunpang.notification.NotificationController;
import com.galaxy.gunpang.user.exception.UserAlreadyExistsException;
import com.galaxy.gunpang.user.exception.UserNotFoundException;
import com.galaxy.gunpang.user.model.dto.*;
import com.galaxy.gunpang.user.service.JwtService;
import com.galaxy.gunpang.user.service.RedisService;
import com.galaxy.gunpang.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 API")
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final RedisService redisService;

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Operation(summary = "사용자 로그인", description = "사용자 로그인을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = LogInResDto.class)))
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @GetMapping
    public ResponseEntity<?> logIn(@RequestParam("googleId") String googleId){
        log.debug("[GET] logIn method {}", googleId);
        // 1. googleId로 회원인지 확인
        // 2. 아니면 UserNotFoundException (-> 회원가입 페이지로)
        // 3. 회원이면 로그인 + 토큰 발급
        if (userService.existsByGoogleId(googleId).isUser()) {
            LogInResDto logInResDto = jwtService.createTokens(googleId);
            redisService.updateToken(logInResDto);
            return ResponseEntity.ok().body(logInResDto);
        } else {
            throw new UserNotFoundException(googleId);
        }
    }

    @Operation(summary = "사용자 회원가입", description = "사용자 회원가입을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = SignUpResDto.class)))
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @PostMapping
    public ResponseEntity<?> signUp(@RequestBody SignUpReqDto signUpReqDto){
        log.debug("[POST] signUp method {}", signUpReqDto);

        // email로 회원인지 확인 (google Id는 로그인에서 확인해서 확인하지 않음)
        String userEmail = signUpReqDto.getEmail();
        if (userService.findUserByEmail(userEmail).isUser()) {
            throw new UserAlreadyExistsException(userEmail);
        }

        SignUpResDto signUpResDto = userService.addUser(signUpReqDto);
        return logIn(signUpResDto.getGoogleId());
    }

    @Operation(summary = "사용자 로그아웃", description = "사용자 로그아웃을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content)
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @GetMapping("/logout")
    public ResponseEntity<?> logOut(@RequestHeader("Authorization") String accessToken){
        log.debug("[GET] logOut method {}", accessToken);

        // googleId 추출
        GoogleIdResDto googleIdResDto = jwtService.getGoogleId(accessToken);
        // redis에서 토큰 제거
        redisService.deleteToken(googleIdResDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "사용자 회원탈퇴", description = "사용자 회원탈퇴를 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content)
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @PutMapping("/signout")
    public ResponseEntity<?> signOut(@RequestHeader("Authorization") String accessToken) {
        log.debug("[PUT] signOut method {}", accessToken);

        // googleId 추출
        GoogleIdResDto googleIdResDto = jwtService.getGoogleId(accessToken);
        // redis에서 토큰 제거 (로그아웃)
        redisService.deleteToken(googleIdResDto);
        // 회원탈퇴
        userService.updateUserToDeleted(googleIdResDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "JWT 토큰을 사용하여 UserId 반환", description = "JWT 토큰을 사용하여 UserId를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = UserIdResDto.class)))
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
            , @ApiResponse(responseCode = "500", description = "잘못된 JWT Token")
    })
    @GetMapping("/jwt/get-userid")
    public ResponseEntity<?> getUserIdByToken(@RequestParam("JWTToken") String accessToken){
        log.debug("[GET] getUserIdByToken method {}", accessToken);

        UserIdResDto userIdResDto = userService.getIdByToken(accessToken);

        return ResponseEntity.ok().body(userIdResDto);
    }

    @Operation(summary = "JWT 토큰 검증", description = "JWT 토큰이 유효한지 검증합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = UserExistenceResDto.class)))
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "유효하지 않은 JWT 토큰")
            , @ApiResponse(responseCode = "500", description = "잘못된 JWT Token")
    })
    @GetMapping("/jwt/validate")
    public ResponseEntity<?> validateToken(@RequestParam("JWTToken") String accessToken){
        log.debug("[GET] validateToken method {}", accessToken);

        // 1. 토큰 자체가 유효한지
        TokenValidationResDto tokenValidationResDto = jwtService.validateToken(accessToken);
        if (tokenValidationResDto.isValid()) {
            // 2. 있는 유저인지
            String googleId = jwtService.getGoogleId(accessToken).getGoogleId();
            UserExistenceResDto userExistenceResDto = userService.existsByGoogleId(googleId);
            if (userExistenceResDto.isUser()) {
                return ResponseEntity.ok().body(userExistenceResDto);
            } else {
                throw new UserNotFoundException(googleId);
            }
        } else {
            return ResponseEntity.badRequest().body("유효하지 않은 토큰입니다.");
        }
    }

    @Operation(summary = "JWT 토큰 재발급", description = "refresh token을 이용하여 access token을 갱신합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = AccessTokenResDto.class)))
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
            , @ApiResponse(responseCode = "500", description = "잘못된 JWT Token")
    })
    @GetMapping("/jwt/recreate")
    public ResponseEntity<?> recreateToken(@RequestParam("JWTToken") String accessToken){
        log.debug("[GET] recreateToken method {}", accessToken);

        AccessTokenResDto accessTokenResDto = redisService.updateToken(accessToken);

        return ResponseEntity.ok().body(accessTokenResDto);
    }

    @Operation(summary = "유저 정보 조회", description = "사용자 정보를 받아옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = AccessTokenResDto.class)))
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
            , @ApiResponse(responseCode = "500", description = "잘못된 JWT Token")
    })
    @GetMapping(value = "/info")
    public ResponseEntity getUserInfo (@RequestHeader("Authorization") String token) throws Exception {
        log.debug("[GET] getUserInfo method {}", token);

        Long userId = userService.getIdByToken(token).getId();
        UserInfoResDto userInfoResDto = userService.getUserInfo(userId);
        return ResponseEntity.ok().body(userInfoResDto);
    }

}
