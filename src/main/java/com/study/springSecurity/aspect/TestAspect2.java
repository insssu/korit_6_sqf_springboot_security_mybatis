package com.study.springSecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Order(value = 1)
public class TestAspect2 {

    @Pointcut("@annotation(com.study.springSecurity.aspect.annotation.Test2Aop)")
    private void pointCut() {}

//    여러개의 포인트컷을 잘 사용하지 않음
//    @Pointcut("@annotation(com.study.springSecurity.aspect.annotation.Test2Aop)")
//    private void pointCut2() {}

//    @Around("pointCut() & pointCut2()")  // 두 개의 포인트컷을 실행
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 매개변수로 넘어온 값을 사용가능
//        Signature signature = proceedingJoinPoint.getSignature();
        CodeSignature signature = (CodeSignature) proceedingJoinPoint.getSignature();
        System.out.println(signature.getName());  // 해당 메서드
        System.out.println(signature.getDeclaringTypeName());  // 해당 메서드의 클래스명

        Object[] args = proceedingJoinPoint.getArgs();  // 변수 값
        String[] paramName = signature.getParameterNames();  // 변수명
        for(int i = 0; i < args.length; i++) {
            System.out.println(paramName[i] + ": " + args[i]);
        }
//        System.out.println(Arrays.toString(signature.getParameterTypes()));
//        System.out.println(Arrays.toString(signature.getParameterNames()));
        System.out.println("전처리2");
        Object result = proceedingJoinPoint.proceed();
        System.out.println("후처리2");

        return result;
    }
}
