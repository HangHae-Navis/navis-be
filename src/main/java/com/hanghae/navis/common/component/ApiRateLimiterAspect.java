package com.hanghae.navis.common.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.navis.common.annotation.ApiRateLimiter;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.entity.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static com.hanghae.navis.common.entity.ExceptionMessage.TOO_MANY_REQUEST;

@Component
@Aspect
@RequiredArgsConstructor
public class ApiRateLimiterAspect {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(apiRateLimiter)")
    public Object processRequest(ProceedingJoinPoint joinPoint, ApiRateLimiter apiRateLimiter) throws Throwable {
        String key = apiRateLimiter.key();
        int limit = apiRateLimiter.limit();
        int seconds = apiRateLimiter.seconds();

        // Calculate the remaining number of requests for this second
        long second = Instant.now().getEpochSecond();
        String redisKey = String.format("%s:%d", key, second);
        Integer requestCount = (Integer) redisTemplate.opsForValue().get(redisKey);
        if (requestCount == null) {
            requestCount = 0;
        }
        int remaining = Math.max(0, limit - requestCount);

        // Return error response if the limit has been exceeded
        if (remaining <= 0) {
            throw new ApiRateLimiterException();
        }

        // Update the number of requests for this second and set the expiration time
        redisTemplate.opsForValue().increment(redisKey, 1);
        redisTemplate.expire(redisKey, seconds, TimeUnit.SECONDS);

        // Proceed with the actual API request
        Object result = joinPoint.proceed();
        return result;
    }
}
@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS, reason = "요청이 너무 많습니다. 잠시 뒤에 실행 해주세요.")
class ApiRateLimiterException extends RuntimeException {

}

@RestControllerAdvice
class GlobalExceptionHandler {

    @Autowired
    private ApiRateLimiterAspect apiRateLimiterAspect;

    @ExceptionHandler(ApiRateLimiterException.class)
    public ResponseEntity<Message> handleApiRateLimiterException(ApiRateLimiterException ex) {
        throw new CustomException(TOO_MANY_REQUEST);
    }

    @Around("execution(* com.hanghae.navis.*.*(..))")
    public Object processRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        ApiRateLimiter apiRateLimiter = method.getAnnotation(ApiRateLimiter.class);
        if (apiRateLimiter != null) {
            return apiRateLimiterAspect.processRequest(joinPoint, apiRateLimiter);
        }
        return joinPoint.proceed();
    }

}


