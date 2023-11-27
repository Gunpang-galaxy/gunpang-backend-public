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
        logger.debug("JwtInterceptor preHandle");
        String uri = request.getRequestURI();
        logger.debug("uri : " + uri.toString());
        logger.debug("request.getMethod() : " + request.getMethod().toString());
        if (request.getMethod().equals("OPTIONS")) {
            logger.debug("request.getMethod().equals(\"OPTIONS\")");
            return true;
        }

        logger.debug("before NoAuth annotation check");
        if (handler instanceof HandlerMethod) {
            logger.debug("handler instanceof HandlerMethod");
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            logger.debug("HandlerMethod handlerMethod = (HandlerMethod) handler");
            Method method = handlerMethod.getMethod();
            logger.debug("Method method = handlerMethod.getMethod()");

            if (method.isAnnotationPresent(NoAuth.class)) {
                logger.debug("if (method.isAnnotationPresent(NoAuth.class))");
                return true;
            }
            logger.debug("end of handler instanceof HandlerMethod");
        }

        logger.debug("before getHeader");
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.debug(bearerToken);
        String token = jwtUtil.removeBearer(bearerToken);
        logger.debug(token);
        logger.debug("after getHeader");

        if (token != null) {
            logger.debug("token != null");
            if (jwtUtil.validateToken(token) == true) {
                logger.debug("jwtUtil.validateToken(token) == true");
                return true;
            }
            logger.debug("end of token != null");
        }

        logger.debug("after token != null");

        throw new InvalidJwtTokenException("유효하지 않은 토큰입니다.");
    }

}
