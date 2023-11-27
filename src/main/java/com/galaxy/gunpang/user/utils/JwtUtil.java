package com.galaxy.gunpang.user.utils;

import com.galaxy.gunpang.user.exception.InvalidJwtTokenException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final byte[] jwtSecretBytes;
    private final static Long ACCESS_TOKEN_VALID_TIME = 24 * 60 * 60 * 1000L * 7 * 4; // 4주
    private final static Long REFRESH_TOKEN_VALID_TIME = 24 * 60 * 60 * 1000L * 7 * 4 * 6; // 4주 * 6

    public JwtUtil(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecretBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
    }

    public String createAccessToken(String googleId) {
        return createToken(googleId, ACCESS_TOKEN_VALID_TIME);
    }

    public String createRefreshToken(String googleId) {
        return createToken(googleId, REFRESH_TOKEN_VALID_TIME);
    }

    public String createToken(String googleId, Long validTime) {
        Claims claims = Jwts.claims().setSubject(googleId);

        log.debug("IssuedAt : {}", new Date());
        log.debug("Expiration : {}", new Date(new Date().getTime() + validTime));

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + validTime))
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(jwtSecretBytes))
                .compact();
    }

    public boolean validateToken(String token) throws InvalidJwtTokenException {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecretBytes).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            throw new InvalidJwtTokenException("잘못된 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            throw new InvalidJwtTokenException("지원하지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException | MalformedJwtException e) {
            throw new InvalidJwtTokenException("잘못된 JWT 토큰입니다.");
        } catch (ExpiredJwtException e){
            throw new InvalidJwtTokenException("만료된 JWT 토큰입니다.");
        } catch (Exception e){
            throw new InvalidJwtTokenException("유효하지 않은 JWT 토큰입니다.");
        }
    }

    public String recreateAccessToken(String refreshToken) {
        if (validateToken(refreshToken)) {
            String googleId = getGoogleIdFromToken(refreshToken);
            return createAccessToken(googleId);
        } else {
            throw new InvalidJwtTokenException("유효하지 않은 리프레시 토큰입니다.");
        }
    }

    public String getGoogleIdFromToken(String token) {
        return String.valueOf(parseToken(token).getSubject());
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Base64.getEncoder().encodeToString(jwtSecretBytes))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean checkIsExpired(String token) {
        Date currentTime = new Date();
        Date expirationTime = parseToken(token).getExpiration();
        return (expirationTime.before(currentTime));
    }

    public Date createExpiration(long expirationTime) {
        long now = (new Date()).getTime();
        return new Date(now + expirationTime * 60000); // 분 -> 밀리초 하기 위해 * 60000
    }

    public String removeBearer(String bearerToken){
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.split(" ")[1];
        }
        return bearerToken;
    }

    public Long getRefreshTokenValidTimeAsDay() {
        return (REFRESH_TOKEN_VALID_TIME / (24 * 60 * 60 * 1000L));
    }
}
