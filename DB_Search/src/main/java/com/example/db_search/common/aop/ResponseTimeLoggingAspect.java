package com.example.db_search.common.aop;

import com.example.db_search.common.dto.ApiResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResponseTimeLoggingAspect {

    @Pointcut("within(@com.example.db_search.common.annotation.ResponseTime *)")
    public void responseTime() {}


    @Around("responseTime()")
    public Object wrapWithApiResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        double start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            double duration = (System.currentTimeMillis() - start) / 1000;

            if (result instanceof ResponseEntity<?> responseEntity) {
                Object body = responseEntity.getBody();
                ApiResponse<?> apiResponse = ApiResponse.success(duration, body);
                return ResponseEntity.status(responseEntity.getStatusCode()).body(apiResponse);
            }
            return result;
        } catch (Throwable t) {
            throw t;
        }
    }
}