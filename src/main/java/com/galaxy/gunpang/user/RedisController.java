package com.galaxy.gunpang.user;

import com.galaxy.gunpang.user.annotation.NoAuth;
import com.galaxy.gunpang.user.service.RedisService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Redis", description = "Redis TEST API")
@Slf4j
@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    @NoAuth
    @PostMapping
    public void redisCreateTest(@RequestParam String id, @RequestParam String tokens) {
        redisService.setToken(id, tokens);
    }

    @NoAuth
    @GetMapping
    public String redisReadTest(@RequestParam String id) {
        return redisService.getToken(id);
    }

    @NoAuth
    @PutMapping
    public void redisUpdateTest(@RequestParam String id, @RequestParam String token) {
        redisService.updateToken(id, token);
    }

}