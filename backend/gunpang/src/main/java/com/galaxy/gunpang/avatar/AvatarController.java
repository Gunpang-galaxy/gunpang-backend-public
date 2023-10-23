package com.galaxy.gunpang.avatar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/avatars")
@RequiredArgsConstructor
public class AvatarController {
    @GetMapping
    public ResponseEntity<?> get(@RequestParam("test")int test){
        log.debug("[GET] get method test {}", test);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestParam("test")int test){
        log.debug("[POST] post method test {}", test);
        return ResponseEntity.ok().build();
    }
}
