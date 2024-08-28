package com.study.springSecurity.controller;

import com.study.springSecurity.aspect.annotation.ParamsAop;
import com.study.springSecurity.aspect.annotation.ValidAop;
import com.study.springSecurity.dto.request.ReqSigninDto;
import com.study.springSecurity.dto.request.ReqSignupDto;
import com.study.springSecurity.service.SigninService;
import com.study.springSecurity.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private SignupService signupService;

    @Autowired
    private SigninService signinService;

    @ValidAop
    @ParamsAop
    @PostMapping("/signup")  // BindingResult는 @Valid 어노테이션을 쓰면 뒤에 따라옴
    // @Valid는 FieldError를 만들고 Dto에서 @Pattern에 걸려 있는 필드값들을 검사한다.
    // -> 패턴에 맞지 않으면 FieldError에 에러필드를 추가하고 검사가 끝나면 bindingResult에 담아준다.
    // 따라서 @Valid에서 매개변수로 BindingResult를 넣어줘야한다.
    // bindingResult는 인터페이스고 구현된 클래스는 BeanPropertyBindingResult이다.
    public ResponseEntity<?> signup(@Valid @RequestBody ReqSignupDto dto, BindingResult bindingResult) {
        return ResponseEntity.created(null).body(signupService.signup(dto));
    }

    @ValidAop
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody ReqSigninDto dto, BeanPropertyBindingResult bindingResult) {
        signinService.signin(dto);
        return ResponseEntity.ok().body(signinService.signin(dto));
    }

    // 요청을 날리면 tomcat에서 request, response를 만들어서 dispatcheServlet으로 넘겨줌 -> 이후 Spring MVC패턴에 따라 동작
}