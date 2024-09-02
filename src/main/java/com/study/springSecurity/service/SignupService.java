package com.study.springSecurity.service;

import com.study.springSecurity.aspect.annotation.TimeAop;
import com.study.springSecurity.domain.entity.Role;
import com.study.springSecurity.domain.entity.User;
import com.study.springSecurity.dto.request.ReqSignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SignupService {

    // DI 방식1 -> @Autowired 어노테이션 달아줌
//    @Autowired private UserRepository userRepository;
//    @Autowired private RoleRepository roleRepository;
//    @Autowired private BCryptPasswordEncoder passwordEncoder;

    // DI 방식2 -> final을 붙이고 클래스 어노테이션으로 @RequiredArgsConstructor를 달아줌
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @TimeAop
    // 이 메서드를 실행하다가 예외가 발생하면 rollback 시킴
    // 예외는 최상위클래스인 Exception을 넣어줌으로써 어떤 예외가 발생하더라도 롤백 시킴
    // 예외가 발생하지 않으면 정상적으로 commit
    @Transactional(rollbackFor = Exception.class)
    public User signup(ReqSignupDto dto) {
        User user = dto.toEntity(passwordEncoder);
        Role role = roleRepository.findByName("ROLE_USER").orElseGet(
                () -> roleRepository.save(Role.builder().name("ROLE_USER").build())
        );
        user.setRoles(Set.of(role));
        user = userRepository.save(user);  // 데이터를 세이브 하고 리턴 받은 User를 user에 다시 덮어씀

        System.out.println(user);

//        UserRole userRole = UserRole.builder()
//                .user(user)
//                .role(role)
//                .build();
//        userRole = userRoleRepository.save(userRole);
        return user;
    }

    public boolean isDuplicatedUsername(String username) {
        return userRepository.findByUsername(username).isPresent();  // 값이 존재하는지 확인
    }
}
