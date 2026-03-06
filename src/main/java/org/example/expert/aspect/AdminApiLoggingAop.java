package org.example.expert.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.expert.domain.common.exception.ServerException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;



@Aspect
@Component
@Slf4j
public class AdminApiLoggingAop {

    @Around("@annotation(org.example.expert.domain.common.annotation.AdminLogging)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        // 현재 요청 정보 가져오기
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new ServerException("요청 정보를 가져올 수 없습니다.");
        }

        HttpServletRequest request = attributes.getRequest();

        long start = System.currentTimeMillis(); // 시작 시간 체크
        Long userId = (Long) request.getAttribute("userId"); // JwtFilter의 set한 userId
        String requestURL = request.getRequestURI(); // 요청 URL
        String methodType = request.getMethod(); // HTTP 메서드타입

        Object[] args = joinPoint.getArgs(); // 메서드 파라미터 (요청 body)
        ObjectMapper objectMapper = new ObjectMapper(); // Java 객체 -> Json 문자열 변환
        String requestBody = objectMapper.writeValueAsString(args); // 요청 본문 로그에 기록

        // 메서드 실행 전 로그
        log.info("요청 전 - userId: {}, 시각: {}, 메서드 타입: {}, 메서드 URL: {}, 요청 본문: {}",
                userId, LocalDateTime.now(), methodType, requestURL, requestBody);

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();

        // result가 ResponseEntity 타입일 시 상태코드를 반환
        if (result instanceof ResponseEntity<?> responseEntity) {
            log.info("요청 후 - userId: {}, 시각: {}, 응답 상태: {}, 처리시간: {}ms",
                    userId, LocalDateTime.now(), responseEntity.getStatusCode(), (end - start));
        } else { // ResponseEntity가 아닐 시 그대로 result
            log.info("요청 후 - userId: {}, 시각: {}, 응답: {}, 처리시간: {}ms",
                    userId, LocalDateTime.now(), result, (end - start));
        }
        return result;
    }
}
