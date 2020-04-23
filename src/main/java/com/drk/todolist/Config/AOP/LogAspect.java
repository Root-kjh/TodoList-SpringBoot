package com.drk.todolist.Config.AOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Around("execution(* com.drk.todolist.Controllers..*.*(..))")
    public Object loggingController(ProceedingJoinPoint pjp) throws Throwable {
        log.info(String.format("[Controller]start - %s / %s",
            pjp.getSignature().getDeclaringTypeName(),
            pjp.getSignature().getName()));
        Object result = pjp.proceed();
        log.info(String.format("[Controller]end - %s / %s",
            pjp.getSignature().getDeclaringTypeName(),
            pjp.getSignature().getName()));
        return result;
    }
}