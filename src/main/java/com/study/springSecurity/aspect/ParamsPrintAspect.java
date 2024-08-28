package com.study.springSecurity.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ParamsPrintAspect {

    @Pointcut("@annotation(com.study.springSecurity.aspect.annotation.ParamsAop)")  // 어노테이션의 위치
    public void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        CodeSignature signature = (CodeSignature) proceedingJoinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();  // 매개변수명
        Object[] args = proceedingJoinPoint.getArgs();  // 매개변수 값

        String infoPrint = String.format("ClassName(%s) MethodName(%s)",
                signature.getDeclaringType().getSimpleName(),  // 클래스명
                signature.getName());  // 메서드명
        StringBuilder linePrint = new StringBuilder();
        for(int i = 0; i < infoPrint.length(); i++) {
            linePrint.append("-");
        }

        log.info("{}", linePrint);
        log.info("{}", infoPrint);
        for(int i = 0; i < paramNames.length; i++) {
            log.info("{} >>>> {}", paramNames[i], args[i]);
        }
        log.info("{}", linePrint);

//        출력결과
//        ------------------------------------------------------
//        ClassName(AuthenticationController) MethodName(signup)
//        dto >>>> ReqSignupDto(username=aaa6, password=1234, checkPassword=1234, name=test)
//        ------------------------------------------------------

        return proceedingJoinPoint.proceed();
    }
}
