package com.study.springSecurity.controller;

import com.study.springSecurity.security.principal.PrincipalUser;
import com.study.springSecurity.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public ResponseEntity<?> get() {
        System.out.println(testService.aopTest());
        testService.aopTest2("이름", 20);
        testService.aopTest3("010-1111-2222", "부산 동래구");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();  // userdetails
        return ResponseEntity.ok(principalUser);
    }
}
