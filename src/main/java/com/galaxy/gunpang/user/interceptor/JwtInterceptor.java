package com.galaxy.gunpang.user.interceptor;

import com.galaxy.gunpang.user.annotation.NoAuth;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.galaxy.gunpang.user.exception.InvalidJwtTokenException;
import com.galaxy.gunpang.user.utils.JwtUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            if (method.isAnnotationPresent(NoAuth.class)) {
                return true;
            }
        }

        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = jwtUtil.removeBearer(bearerToken);

        if (token != null) {
            if (jwtUtil.validateToken(token) == true) {
                return true;
            }
        }

        throw new InvalidJwtTokenException("유효하지 않은 토큰입니다.");
    }

}
