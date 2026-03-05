package org.example.expert.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;



@Aspect
@Component
@Slf4j
public class AdminApiLoggingAop {

    @Around("@annotation(org.example.expert.domain.common.annotation.AdminLogging)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();

        long start = System.currentTimeMillis(); // 시작 시간 체크
        Long userId = (Long) request.getAttribute("userId"); // JwtFilter의 set한 userId
        String requestURL = request.getRequestURI(); // 요청 URL
        String methodType = request.getMethod(); // HTTP 메서드타입
        Object[] args = joinPoint.getArgs(); // 메서드 파라미터 (요청 body)
        String requestBody = Arrays.toString(args); // 요청 body값 문자열 변환

        // 메서드 실행 전 로그
        log.info("요청 전 - userId: {}, 시각: {}, 메서드 타입 {},메서드 URL {}, 요청 본문: {}",
                userId, LocalDateTime.now(), methodType, requestURL, requestBody);

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis(); // 종료시간 체크

        log.info("요청 후 - userId: {}, 시각 {}, 응답: {}, 처리시간 {}ms", userId, LocalDateTime.now(), result, (end-start));

        return result;
    }
}
