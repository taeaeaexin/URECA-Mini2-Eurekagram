package com.gram.eureka.eureka_gram_user.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ServiceLoggingAop {
    @Pointcut("execution(* com.gram.eureka.eureka_gram_user.service..*(..))")
    public void serviceMethods() {
    }

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("[aop] 서비스 메서드 실행 전 : {}", joinPoint.getSignature().getName());
    }

    @After("serviceMethods()")
    public void logAfter(JoinPoint joinPoint) {
        log.info("[aop] 서비스 메서드 실행 후 : {}", joinPoint.getSignature().getName());
    }

    // TODO @Around 를 통해 서비스 메서드의 실행 시간을 출력 할 수도 있음
}
