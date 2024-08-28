package com.study.springSecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(value = 2)  // AOP실행 우선순위 변경, 숫자가 작을수록 먼저 실행됨
public class TestAspect {

    // 제일 앞에 접근 제한자는 public(default값이 public)
    // 리턴타입이 String이고 com.study.springSecurity.service폴더 안에 있는 모든 클래스에서 aop로 시작하는 모든 메서드
    // (매개변수는 있어도 되고 없어도 됨)
    // 우선순위는 execution > @annotation
    @Pointcut("execution(* com.study.springSecurity.service.*.aop*(..))")
    private void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // 작동방식이 필터랑 흡사함
        System.out.println("전처리");
        Object result = proceedingJoinPoint.proceed();  // 핵심기능 호출
        System.out.println("후처리");

        // 요청 들어옴 -> 해당 메서드 호출(Controller) -> aspect함수 호출(전처리) -> aopTest서비스 호출 -> aspect함수 호출(후처리)
        // -> controller함수 리턴

        return result;
    }
}
