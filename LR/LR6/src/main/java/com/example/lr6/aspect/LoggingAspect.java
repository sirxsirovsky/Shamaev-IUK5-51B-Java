package com.example.lr6.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.example.lr6.controller..*(..))")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("--> START Method: {} | Args: {}", methodName, Arrays.toString(args));

        Object result = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        log.info("<-- END Method: {} | Time: {} ms | Result: {}", methodName, executionTime, result);

        return result;
    }
}