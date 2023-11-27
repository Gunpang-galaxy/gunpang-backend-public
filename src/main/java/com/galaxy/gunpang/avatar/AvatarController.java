package com.galaxy.gunpang.avatar;

import com.galaxy.gunpang.avatar.model.dto.*;
import com.galaxy.gunpang.avatar.service.AvatarService;
import com.galaxy.gunpang.user.service.UserService;
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

@Tag(name = "Avatar", description = "아바타 API")
@Slf4j
@RestController
@RequestMapping("/avatars")
@RequiredArgsConstructor
public class AvatarController {

    private final AvatarService avatarService;
    private final UserService userService;

    @Operation(summary = "랜덤 아바타 등록", description = "랜덤 생성한 아바타를 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
            , @ApiResponse(responseCode = "409", description = "이미 키우고 있는 아바타가 존재")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @PostMapping("/gatcha")
    public ResponseEntity<?> addRandom(@RequestHeader("Authorization") String token, @RequestBody AvatarAddReqDto avatarAddReqDto){
        log.debug("[POST] addRandom method {}", avatarAddReqDto);

        Long userId = userService.getIdByToken(token).getId();

        avatarService.addRandomAvatar(userId, avatarAddReqDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "현재 아바타 받아오기", description = "가장 최근에 키운 아바타를 받아옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = AvatarResDto.class)))
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
            , @ApiResponse(responseCode = "404", description = "존재하지 않는 아바타")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @GetMapping("/app-current")
    public ResponseEntity<?> currentApp(@RequestHeader("Authorization") String token){
        Long userId = userService.getIdByToken(token).getId();

        log.debug("[GET] /avatar/app-current : current method, {}", userId);

        AvatarResDto avatarResDto = avatarService.getCurAvatarResDto(userId);

        return ResponseEntity.ok().body(avatarResDto);
    }

    @Operation(summary = "(워치)현재 아바타 받아오기", description = "가장 최근에 키운 아바타를 받아옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = AvatarResDto.class)))
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
            , @ApiResponse(responseCode = "404", description = "존재하지 않는 아바타")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @GetMapping("/watch-current")
    public ResponseEntity<?> currentWatch(@RequestHeader("Authorization") String token){
        Long userId = userService.getIdByToken(token).getId();

        log.debug("[GET] /avatar/watch-current : current method, {}", userId);

        AvatarWatchResDto avatarWatchResDto = avatarService.getCurAvatarWatchResDto(userId);

        return ResponseEntity.ok().body(avatarWatchResDto);
    }

    @Operation(summary = "아바타 상세 조회", description = "아바타를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = AvatarResDto.class)))
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자, 권한이 없는 사용자")
            , @ApiResponse(responseCode = "404", description = "현재 아바타가 존재하지 않음")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @GetMapping("/{avatarId}")
    public ResponseEntity<?> get(@RequestHeader("Authorization") String token, @PathVariable Long avatarId){
        Long userId = userService.getIdByToken(token).getId();

        avatarService.authenticate(avatarId, userId);

        log.debug("[POST] /avatar : get method, {}, {}", avatarId, userId);

        AvatarResDto avatarResDto = avatarService.getAvatarResDto(avatarId, userId);

        if(avatarResDto == null) return ResponseEntity.internalServerError().build();

        return ResponseEntity.ok().body(avatarResDto);
    }
}
