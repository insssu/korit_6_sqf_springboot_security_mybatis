package com.study.springSecurity.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class TimePrintAspect {

    @Pointcut("@annotation(com.study.springSecurity.aspect.annotation.TimeAop)")  // 어노테이션의 위치
    public void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        CodeSignature signature = (CodeSignature) proceedingJoinPoint.getSignature();
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        String infoPrint = String.format("ClassName(%s) MethodName(%s)",
                signature.getDeclaringType().getSimpleName(),  // 클래스명
                signature.getName());  // 메서드명
        StringBuilder linePrint = new StringBuilder();
        for(int i = 0; i < infoPrint.length(); i++) {
            linePrint.append("-");
        }

        log.info("{}", linePrint);
        log.info("{}", infoPrint);
        log.info("TotalTime: {}초", stopWatch.getTotalTimeSeconds());
        log.info("{}", linePrint);

//        출력결과
//        -------------------------------------------
//        ClassName(SignupService) MethodName(signup)
//        TotalTime: 0.0866882초
//        -------------------------------------------

        return result;
    }
}
