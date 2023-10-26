package com.galaxy.gunpang.user;

import com.galaxy.gunpang.user.model.dto.SignUpReqDto;
import com.galaxy.gunpang.user.model.dto.SignUpResDto;
import com.galaxy.gunpang.user.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 API")
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @Operation(summary = "사용자 회원가입", description = "사용자")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = SignUpResDto.class)))
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @PostMapping
    public void signUp(@RequestBody SignUpReqDto signUpReqDto){
        log.debug("[POST] signUp method {}", signUpReqDto);

        SignUpResDto signUpResDto = userService.addUser(signUpReqDto);
        // TODO : signUpResDto(googleId) 들고 login
    }

    //    @GetMapping
//    public ResponseEntity<?> get(@RequestParam("test")int test){
//        log.debug("[GET] get method test {}", test);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping
//    public ResponseEntity<?> add(@RequestParam("test")int test){
//        log.debug("[POST] post method test {}", test);
//        return ResponseEntity.ok().build();
//    }
}
