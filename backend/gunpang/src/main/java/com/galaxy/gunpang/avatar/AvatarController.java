package com.galaxy.gunpang.avatar;

import com.galaxy.gunpang.avatar.model.dto.AvatarGatchaResDto;
import com.galaxy.gunpang.avatar.service.AvatarService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
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

    @GetMapping
    public ResponseEntity<?> get(@RequestParam("test")int test){
        log.debug("[GET] get method test {}", test);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "아바타 랜덤 생성", description = "랜덤한 아바타를 생성한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = AvatarGatchaResDto.class)))
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    @PostMapping("/gatcha")
    public ResponseEntity<?> addRandom(@RequestParam("userId")Long userId){
        log.debug("[POST] addRandom method test {}", userId);

        AvatarGatchaResDto avatarGatchaResDto = avatarService.addRandomAvatar(userId);

        return ResponseEntity.ok().body(avatarGatchaResDto);
    }
}
