package com.study.springSecurity.init;

import com.study.springSecurity.domain.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleDataLoader implements CommandLineRunner {
    // 컴파일시 자동으로 실행되는 메서드

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {  // args의 자료형은 String 배열이다. 매개변수로 여러개의 String값을 받음
        // role 테이블에 ROLE_USER라는 이름(column)이 없으면 TRUE
        if(roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(Role.builder().name("ROLE_USER").build());
        }
        if(roleRepository.findByName("ROLE_MANAGER").isEmpty()) {
            roleRepository.save(Role.builder().name("ROLE_MANAGER").build());
        }
        if(roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
        }
    }
}