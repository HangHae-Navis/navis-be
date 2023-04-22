package com.hanghae.navis.common.component;

import com.hanghae.navis.common.annotation.ApiRateLimiter;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.entity.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
@RequiredArgsConstructor
public class ApiRateLimiterAspect {
    private RedisTemplate<String, Integer> redisTemplate;

    @Around("@annotation(apiRateLimiter)")
    @Transactional
    public Object processRequest(ProceedingJoinPoint joinPoint, ApiRateLimiter apiRateLimiter) throws Throwable {
        String key = apiRateLimiter.key();
        int limit = apiRateLimiter.limit();
        int seconds = apiRateLimiter.seconds();

        // Calculate the remaining number of requests for this second
        long second = Instant.now().getEpochSecond();
        String redisKey = String.format("%s:%d", key, second);
        Integer requestCount = redisTemplate.opsForValue().get(redisKey);
        if (requestCount == null) {
            requestCount = 0;
        }
        int remaining = Math.max(0, limit - requestCount);

        // Return error response if the limit has been exceeded
        if (remaining <= 0) {
            throw new CustomException(ExceptionMessage.TOO_MANY_REQUEST);
        }

        // Update the number of requests for this second and set the expiration time
        redisTemplate.opsForValue().increment(redisKey, 1);
        redisTemplate.expire(redisKey, seconds, TimeUnit.SECONDS);

        // Proceed with the actual API request
        Object result = joinPoint.proceed();
        return result;
    }
}


