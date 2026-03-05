package org.example.expert.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
public class AdminOwnerCheckInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {

        // JwtFilter가 set해준 값을 가져옴 이 userRole은 claim을 거쳐 String값으로 변환됨
        String userRole = (String) request.getAttribute("userRole");

        if (!UserRole.ADMIN.name().equals(userRole)) { // .name으로 enum타입을 String으로 변환해서 비교
            throw new AuthException("어드민 권한이 없습니다.");
        }

        log.info("요청 시각: {}, URL: {}", LocalDateTime.now(), request.getRequestURI());
        return true;
    }
}
