package com.hanghae.navis.common.controller;

import com.hanghae.navis.common.annotation.ApiRateLimiter;
import com.hanghae.navis.common.component.ApiRateLimiterAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Method;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ApiRateLimiterAspect apiRateLimiterAspect;

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