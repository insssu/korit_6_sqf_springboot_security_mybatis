package com.study.springSecurity.aspect;

import com.study.springSecurity.dto.request.ReqSignupDto;
import com.study.springSecurity.exception.ValidException;
import com.study.springSecurity.service.SignupService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

@Slf4j
@Aspect
@Component
public class ValidAspect {

    @Autowired
    private SignupService signupService;

    @Pointcut("@annotation(com.study.springSecurity.aspect.annotation.ValidAop)")  // 어노테이션의 위치
    public void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();  // 매개변수의 값들을 Object로 업캐스팅해서 배열로 들고옴
        BeanPropertyBindingResult bindingResult = null;
        // 가져온 매개변수가 BeanPropertyBindingResult가 아닐수도 있기 때문에 확인하고 넣어줘야함
        // 이 코드에서는 args에 dto랑 BindingResult가 들어있음
        for(Object arg : args) {
            if(arg.getClass() == BeanPropertyBindingResult.class) {
                bindingResult = (BeanPropertyBindingResult) arg;
                break;
            }
        }

        // proceedingJoinPoint 핵심 메서드의 정보가 담겨있음
        // Signature은 해당 클래스의 정보들이 담겨있음 , getName은 메서드명
        switch (proceedingJoinPoint.getSignature().getName()) {
            case "signup":  // 핵심로직의 메서드명이 signup일 경우만 실행
                // 위에 for문에서 같이 처리를 하게 되면 bindingError가 null일수 있기 때문에 분리함
                validSignupDto(args, bindingResult);
                break;
        }

        // 문제가 있으면 이 반복이 실행됨, 에러가 없으면 실행되지 않음
//        for(FieldError fieldError : bindingResult.getFieldErrors()) {
//            System.out.println("getField: " + fieldError.getField());  // 멤버변수 이름
//            System.out.println("getDefaultMessage: " + fieldError.getDefaultMessage());  // 메시지 내용
//        }

        // 예외가 발생했기 때문에 밑이 있는 return 문이 실행되지 않고 예외가 리턴됨
        // 예외가 리턴되면 ControllerAdvice에서 예외를 잡아서 대신 메서드를 실행시킴
        if(bindingResult.hasErrors()) {
            throw new ValidException("유효성 검사 오류", bindingResult.getFieldErrors());
        }

        return proceedingJoinPoint.proceed();
    }

    private void validSignupDto(Object[] args, BeanPropertyBindingResult bindingResult) {
        // @Valid 어노테이션이 할 일을 직접 해주는 것
        for(Object arg : args) {
            if(arg.getClass() == ReqSignupDto.class) {
                ReqSignupDto dto = (ReqSignupDto) arg;
                if(!dto.getPassword().equals(dto.getCheckPassword())) {
                    FieldError fieldError = new FieldError(
                            "checkPassword",
                            "checkPassword",
                            "비밀번호가 일치하지 않습니다.");
                    bindingResult.addError(fieldError);  // 같은 주소를 사용하기 때문에 리턴이 필요없음
                }
                if(signupService.isDuplicatedUsername(dto.getUsername())) {
                    FieldError fieldError = new FieldError(
                            "username",
                            "username",
                            "이미 존재하는 사용자 이름입니다.");
                    bindingResult.addError(fieldError);
                }
                break;
            }
        }
    }
}
