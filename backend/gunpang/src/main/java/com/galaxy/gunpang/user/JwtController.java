package com.galaxy.gunpang.user;

import com.galaxy.gunpang.user.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Jwt", description = "JWT TEST API")
@Slf4j
@RestController
@RequestMapping("/jwts")
@RequiredArgsConstructor
public class JwtController {

    private final JwtUtil jwtUtil;

    @GetMapping("/create-token")
    public String createToken(String googleId, long validateTime){
        return jwtUtil.createToken(googleId, validateTime);
    }

    @GetMapping("/create-access-token")
    public String createAccessToken(String googleId){
        return jwtUtil.createAccessToken(googleId);
    }

    @GetMapping("/create-refresh-token")
    public String createRefreshToken(String googleId){
        return jwtUtil.createRefreshToken(googleId);
    }

    @GetMapping("/parse-token")
    public Claims parseToken(String token){
        return jwtUtil.parseToken(token);
    }

    @GetMapping("/get-googleid")
    public String getGoogleId(String token){
        return jwtUtil.getGoogleIdFromToken(token);
    }

    @GetMapping("/check-expired")
    public boolean checkIsExpired(String token){
        return jwtUtil.checkIsExpired(token);
    }

    @GetMapping("/validate-token")
    public boolean validateToken(String token){
        return jwtUtil.validateToken(token);
    }

}