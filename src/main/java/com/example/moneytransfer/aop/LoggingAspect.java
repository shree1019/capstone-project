package com.example.moneytransfer.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("within(com.example.moneytransfer.controller..*) || within(com.example.moneytransfer.service..*)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        Signature signature = joinPoint.getSignature();
        String methodName = signature.toShortString();

        log.info("Entering {}", methodName);
        try {
            Object result = joinPoint.proceed();
            long durationMs = (System.nanoTime() - start) / 1_000_000;
            log.info("Exiting {} ({} ms)", methodName, durationMs);
            return result;
        } catch (Throwable ex) {
            long durationMs = (System.nanoTime() - start) / 1_000_000;
            log.error("Exception in {} after {} ms: {}", methodName, durationMs, ex.getMessage(), ex);
            throw ex;
        }
    }
}

