package com.galaxy.gunpang.user;

import com.galaxy.gunpang.user.exception.UserNotFoundException;
import com.galaxy.gunpang.user.model.dto.LogInResDto;
import com.galaxy.gunpang.user.model.dto.SignUpReqDto;
import com.galaxy.gunpang.user.model.dto.SignUpResDto;
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

    @Operation(summary = "사용자 로그인", description = "사용자 로그인을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = LogInResDto.class)))
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @GetMapping
    public LogInResDto logIn(@RequestParam("googleId") String googleId){
        log.debug("[GET] logIn method {}", googleId);
        // 1. googleId로 회원인지 확인
        // 2. 아니면 UserNotFoundException (-> 회원가입 페이지로)
        // 3. 회원이면 로그인 + 토큰 발급
        if (userService.existsByGoogleId(googleId)) {
            LogInResDto logInResDto = jwtService.createTokens(googleId);
            redisService.setTokens(logInResDto);
            return logInResDto;
        } else {
            throw new UserNotFoundException(googleId);
        }
    }

    @Operation(summary = "사용자 회원가입", description = "사용자 회원가입을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = SignUpResDto.class)))
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @PostMapping
    public LogInResDto signUp(@RequestBody SignUpReqDto signUpReqDto){
        log.debug("[POST] signUp method {}", signUpReqDto);

        SignUpResDto signUpResDto = userService.addUser(signUpReqDto);
        return logIn(signUpResDto.getGoogleId());
    }

}
