package com.galaxy.gunpang.bodyComposition;

import com.galaxy.gunpang.bodyComposition.model.dto.BodyCompositionApiReqDto;
import com.galaxy.gunpang.bodyComposition.model.dto.BodyCompositionResDto;
import com.galaxy.gunpang.bodyComposition.service.BodyCompositionService;
import com.galaxy.gunpang.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/body-compositions")
public class BodyCompositionController {
    private static final Logger logger = LoggerFactory.getLogger(BodyCompositionController.class);
    private final BodyCompositionService bodyCompositionService;
    private final UserService userService;

    @Operation(summary = "체성분 저장하기", description = "삼성헬스에서 받아온 체성분 정보를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "체성분 기록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "체성분 기록 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(value = "/samsung", consumes = "application/json;charset=UTF-8")
    public ResponseEntity<?> setBodyCompositionsWithHealthApi(@RequestHeader("Authorization") String token, @RequestBody BodyCompositionApiReqDto bodyCompositionApiReqDto) throws Exception {

        logger.debug("controller");

        Long userId = userService.getIdByToken(token).getId();
        bodyCompositionService.setBodyCompositionsWithHealthApi(userId,bodyCompositionApiReqDto.getWeight(),bodyCompositionApiReqDto.getMuscleMass(),bodyCompositionApiReqDto.getFatMass(),bodyCompositionApiReqDto.getBodyWaterMass());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "체성분 기록 조회", description = "체성분 기록을 조회합니다.")
    @GetMapping("/")
    public ResponseEntity<?> getBodyCompositions(@RequestHeader("Authorization") String token){
        logger.debug("controller");

        Long userId = userService.getIdByToken(token).getId();
        BodyCompositionResDto bodyCompositionResDto = bodyCompositionService.getBodyCompositions(userId);
        return new ResponseEntity<>(bodyCompositionResDto, HttpStatus.OK);

    }
}
