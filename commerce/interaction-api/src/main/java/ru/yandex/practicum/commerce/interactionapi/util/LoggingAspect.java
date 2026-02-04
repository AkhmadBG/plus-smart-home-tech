package ru.yandex.practicum.commerce.interactionapi.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Around("@annotation(ru.yandex.practicum.commerce.interactionapi.util.Logging)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Entering method: {}", joinPoint.getSignature());
        Object[] args = joinPoint.getArgs();
        log.info("Request Parameters: {}", args);
        Object result = joinPoint.proceed();
        log.info("Exiting method: {} - Response: {}", joinPoint.getSignature(), result);
        return result;
    }
}